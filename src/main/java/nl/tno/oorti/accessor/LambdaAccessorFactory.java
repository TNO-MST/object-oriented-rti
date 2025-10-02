package nl.tno.oorti.accessor;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaConversionException;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import nl.tno.oorti.accessor.consumer.ObjBooleanConsumer;
import nl.tno.oorti.accessor.consumer.ObjByteConsumer;
import nl.tno.oorti.accessor.consumer.ObjCharConsumer;
import nl.tno.oorti.accessor.consumer.ObjFloatConsumer;
import nl.tno.oorti.accessor.consumer.ObjShortConsumer;
import nl.tno.omt.helpers.OmtJavaMapping;

/**
 * Lambda implementation of the AccessorFactory.
 *
 * @author bergtwvd
 */
class LambdaAccessorFactory implements AccessorFactory {

  // the lookup factory for method handle look ups
  final Lookup lookup;

  public LambdaAccessorFactory() {
    this(null);
  }

  /**
   * Constructs a LAMBDA Accessor factory. If a lookup context is provided then this context is used
   * to retrieve class method handles. Otherwise, the context lookup of the caller's class is used.
   * The context lookup is used for working with dynamic classes that are unknown to the Java
   * Application class loader or Java System class loader.
   *
   * <p>The LAMBDA Accessor factory uses the LambdaMetafactory to generate `Function` and
   * `BiFunction` getter, and `BiConsumer` setter implementations in order to get or set data in an
   * object instance.
   *
   * <p>Using the `privateLookupIn` method for the private context lookup, the LambdaMetafactory
   * invocation looks like:
   *
   * <pre>
   * var lookup = MethodHandles.privateLookupIn(
   * 	targetClass,
   * 	MethodHandles.lookup()); // allow non-public access
   *
   * var getter = lookup.unreflect(someGetterMethod);
   *
   * LambdaMetafactory.metafactory(
   * 	lookup,
   * 	"apply",
   * 	methodType(Function.class),
   * 	methodType(Object.class, Object.class),
   * 	getter,
   * 	getter.type())
   * </pre>
   *
   * This works well for classes from the same ClassLoader. However, once you try to generate
   * lambdas with implementations loaded from a different ClassLoader, you run into a check in the
   * AbstractValidatingLambdaMetafactory constructor:
   *
   * <pre>
   * if (!caller.hasFullPrivilegeAccess()) {
   * throw new LambdaConversionException(String.format(
   * "Invalid caller: %s",
   * caller.lookupClass().getName()));
   * }
   * </pre>
   *
   * The `privateLookupIn` call seems to drop MODULE privilege access when looking across
   * ClassLoaders. This appears to be because the "unnamed module" differs between ClassLoaders.
   * This already happens when just using the classpath (i.e. without the use of modulepath), where
   * we would not expect module restrictions to be in play.
   *
   * <p>When the target class is public with public methods, and the target class is exported form
   * the module (or the module is declared open), then we can use the `MethodHandles.lookup()`
   * method to get a context lookup with the proper access privilege for the public classes and
   * class methods.
   *
   * <p>Alternatively, since we are creating classes from our own ClassLoader, we can get our hands
   * on the proper class loading context. So, another way to get a lookup object with sufficient
   * privileges is to inject another class via our own ClassLoader, which creates the lookup object
   * (in the proper context) and allows our calling code to retrieve it. This mechanism is
   * implemented in the BeanCompiler. The lookup object provided by the BeanCompier can be passed in
   * the constructor of the LAMBDA Accessor factory and thereby avoiding the
   * LambdaConversionException.
   *
   * @param lookup: optional lookup object (nullable).
   */
  public LambdaAccessorFactory(Lookup lookup) {
    /**
     * If no lookup is provided then get a lookup object for creating method handles.
     *
     * <p>MethodHandles provides three methods to get a lookup object: (1) publicLookup, (2)
     * privateLookupIn, (3) lookup. Assuming that the provided clazz has public getters and setters,
     * then the hasFullPrivilegeAccess result of the returned lookup object is with each method: (1)
     * publicLookup=false, (2) privateLookupIn=false, (3) lookup=true.
     *
     * <p>Therefore we must use the third method in order for the call to
     * LambdaMetafactory.metafactory to succeed. When using the first two methods the call to
     * LambdaMetafactory.metafactory fails with a LambdaConversionException.
     */
    this.lookup = lookup == null ? MethodHandles.lookup() : lookup;
  }

  @Override
  public Accessor createAccessor(Field field) throws ReflectiveOperationException {
    Class clazz = field.getDeclaringClass();
    String name = field.getName();

    Method getterMethod = clazz.getMethod(OmtJavaMapping.toJavaGetterName(name));
    Function getterFunction = createGetterFunction(getterMethod);

    Method setterMethod =
        clazz.getMethod(OmtJavaMapping.toJavaSetterName(name), getterMethod.getReturnType());
    BiConsumer setterFunction = createSetterFunction(setterMethod);

    return new LambdaAccessor(getterFunction, setterFunction, getterMethod.getReturnType());
  }

  @Override
  public Getter createGetter(Class clazz, String methodName) throws ReflectiveOperationException {
    Method getterMethod = clazz.getMethod(methodName);
    Function getterFunction = createGetterFunction(getterMethod);
    return new LambdaGetter(getterFunction, getterMethod.getReturnType());
  }

  @Override
  public BiGetter createBiGetter(Class clazz, String methodName, Class argumentType)
      throws ReflectiveOperationException {
    Method biGetterMethod = clazz.getMethod(methodName, argumentType);
    BiFunction biGetterFunction = createBiGetterFunction(biGetterMethod);
    return new LambdaBiGetter(biGetterFunction, biGetterMethod.getReturnType());
  }

  private Function createGetterFunction(Method getterMethod) throws ReflectiveOperationException {
    MethodHandle getter = lookup.unreflect(getterMethod);

    try {
      CallSite site =
          LambdaMetafactory.metafactory(
              lookup,
              "apply",
              MethodType.methodType(Function.class),
              MethodType.methodType(
                  Object.class,
                  Object.class), // signature of method Function.apply after clazz erasure
              getter,
              getter.type()); // actual signature of getter

      return (Function) site.getTarget().invokeExact();
    } catch (Throwable ex) {
      throw new ReflectiveOperationException(ex);
    }
  }

  private BiFunction createBiGetterFunction(Method biGetterMethod)
      throws ReflectiveOperationException {
    MethodHandle bigetter = this.lookup.unreflect(biGetterMethod);

    try {
      CallSite site =
          LambdaMetafactory.metafactory(
              lookup,
              "apply",
              MethodType.methodType(BiFunction.class),
              MethodType.methodType(
                  Object.class,
                  new Class<?>[] {
                    Object.class, Object.class
                  }), // signature of method BiFunction.apply after clazz erasure
              bigetter,
              bigetter.type()); // actual signature of getter

      return (BiFunction) site.getTarget().invokeExact();
    } catch (Throwable ex) {
      throw new ReflectiveOperationException(ex);
    }
  }

  private BiConsumer createSetterFunction(Method setterMethod) throws ReflectiveOperationException {
    MethodHandle setter = this.lookup.unreflect(setterMethod);
    Class<?> valueType = setterMethod.getParameterTypes()[0];

    try {
      if (valueType.isPrimitive()) {
        if (valueType == double.class) {
          ObjDoubleConsumer consumer =
              (ObjDoubleConsumer)
                  createSetterCallSite(setter, ObjDoubleConsumer.class, double.class)
                      .getTarget()
                      .invokeExact();
          return (a, b) -> consumer.accept(a, (double) b);
        } else if (valueType == int.class) {
          ObjIntConsumer consumer =
              (ObjIntConsumer)
                  createSetterCallSite(setter, ObjIntConsumer.class, int.class)
                      .getTarget()
                      .invokeExact();
          return (a, b) -> consumer.accept(a, (int) b);
        } else if (valueType == long.class) {
          ObjLongConsumer consumer =
              (ObjLongConsumer)
                  createSetterCallSite(setter, ObjLongConsumer.class, long.class)
                      .getTarget()
                      .invokeExact();
          return (a, b) -> consumer.accept(a, (long) b);
        } else if (valueType == byte.class) {
          ObjByteConsumer consumer =
              (ObjByteConsumer)
                  createSetterCallSite(setter, ObjByteConsumer.class, byte.class)
                      .getTarget()
                      .invokeExact();
          return (a, b) -> consumer.accept(a, (byte) b);
        } else if (valueType == float.class) {
          ObjFloatConsumer consumer =
              (ObjFloatConsumer)
                  createSetterCallSite(setter, ObjFloatConsumer.class, float.class)
                      .getTarget()
                      .invokeExact();
          return (a, b) -> consumer.accept(a, (float) b);
        } else if (valueType == short.class) {
          ObjShortConsumer consumer =
              (ObjShortConsumer)
                  createSetterCallSite(setter, ObjShortConsumer.class, short.class)
                      .getTarget()
                      .invokeExact();
          return (a, b) -> consumer.accept(a, (short) b);
        } else if (valueType == char.class) {
          ObjCharConsumer consumer =
              (ObjCharConsumer)
                  createSetterCallSite(setter, ObjCharConsumer.class, char.class)
                      .getTarget()
                      .invokeExact();
          return (a, b) -> consumer.accept(a, (char) b);
        } else if (valueType == boolean.class) {
          ObjBooleanConsumer consumer =
              (ObjBooleanConsumer)
                  createSetterCallSite(setter, ObjBooleanConsumer.class, boolean.class)
                      .getTarget()
                      .invokeExact();
          return (a, b) -> consumer.accept(a, (boolean) b);
        } else {
          // Real code needs to support short, char, boolean, byte, and float according to pattern
          // above
          throw new ReflectiveOperationException(
              "Type is not supported yet: " + valueType.getName());
        }
      } else {
        return (BiConsumer)
            createSetterCallSite(setter, BiConsumer.class, Object.class).getTarget().invokeExact();
      }
    } catch (Throwable ex) {
      throw new ReflectiveOperationException(ex);
    }
  }

  private CallSite createSetterCallSite(
      MethodHandle setter, Class<?> interfaceType, Class<?> valueType)
      throws LambdaConversionException {
    return LambdaMetafactory.metafactory(
        lookup,
        "accept",
        MethodType.methodType(interfaceType),
        MethodType.methodType(
            void.class,
            Object.class,
            valueType), // signature of method SomeConsumer.accept after type erasure
        setter,
        setter.type());
  }

  private class LambdaGetter implements Getter {

    private final Function getterFunction;
    private final Class type;

    private LambdaGetter(Function getterFunction, Class type) {
      this.getterFunction = getterFunction;
      this.type = type;
    }

    @Override
    public Object get(Object theObject) {
      return getterFunction.apply(theObject);
    }

    @Override
    public Class getType() {
      return type;
    }
  }

  private class LambdaBiGetter implements BiGetter {

    private final BiFunction biGetterFunction;
    private final Class type;

    private LambdaBiGetter(BiFunction biGetterFunction, Class type) {
      this.biGetterFunction = biGetterFunction;
      this.type = type;
    }

    @Override
    public Object get(Object theObject, Object value) {
      return biGetterFunction.apply(theObject, value);
    }

    @Override
    public Class getType() {
      return type;
    }
  }

  private class LambdaAccessor implements Accessor {

    private final Function getterFunction;
    private final BiConsumer setterFunction;
    private final Class clazz;

    private LambdaAccessor(Function getterFunction, BiConsumer setterFunction, Class clazz) {
      this.getterFunction = getterFunction;
      this.setterFunction = setterFunction;
      this.clazz = clazz;
    }

    @Override
    public Object get(Object theObject) {
      return getterFunction.apply(theObject);
    }

    @Override
    public void set(Object theObject, Object value) {
      setterFunction.accept(theObject, value);
    }

    @Override
    public Class getType() {
      return clazz;
    }
  }
}
