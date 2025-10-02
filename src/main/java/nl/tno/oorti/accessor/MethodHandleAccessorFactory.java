package nl.tno.oorti.accessor;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import nl.tno.omt.helpers.OmtJavaMapping;

/**
 * @author bergtwvd
 */
class MethodHandleAccessorFactory implements AccessorFactory {

  // the lookup factory for method handle look ups
  final Lookup lookup;

  public MethodHandleAccessorFactory() {
    this(null);
  }

  public MethodHandleAccessorFactory(Lookup lookup) {
    this.lookup = lookup == null ? MethodHandles.lookup() : lookup;
  }

  @Override
  public Accessor createAccessor(Field field) throws NoSuchMethodException, IllegalAccessException {
    Class clazz = field.getDeclaringClass();
    String name = field.getName();

    Method getterMethod = clazz.getMethod(OmtJavaMapping.toJavaGetterName(name));
    MethodHandle getter = lookup.unreflect(getterMethod);

    Method setterMethod =
        clazz.getMethod(OmtJavaMapping.toJavaSetterName(name), getterMethod.getReturnType());
    MethodHandle setter = lookup.unreflect(setterMethod);

    return new MethodHandleAccessor(getter, setter, getterMethod.getReturnType());
  }

  @Override
  public Getter createGetter(Class clazz, String methodName)
      throws NoSuchMethodException, IllegalAccessException {
    Method getterMethod = clazz.getMethod(methodName);
    MethodHandle getter = lookup.unreflect(getterMethod);
    return new MethodHandleGetter(getter, getterMethod.getReturnType());
  }

  @Override
  public BiGetter createBiGetter(Class clazz, String methodName, Class argumentType)
      throws NoSuchMethodException, IllegalAccessException {
    Method biGetterMethod = clazz.getMethod(methodName, argumentType);
    MethodHandle bigetter = lookup.unreflect(biGetterMethod);
    return new MethodHandleBiGetter(bigetter, biGetterMethod.getReturnType());
  }

  private class MethodHandleGetter implements Getter {

    private final MethodHandle getter;
    private final Class type;

    private MethodHandleGetter(MethodHandle getter, Class type) {
      this.getter = getter;
      this.type = type;
    }

    @Override
    public Object get(Object theObject) throws ReflectiveOperationException {
      try {
        return getter.invoke(theObject);
      } catch (Throwable ex) {
        throw new ReflectiveOperationException(ex);
      }
    }

    @Override
    public Class getType() {
      return type;
    }
  }

  private class MethodHandleBiGetter implements BiGetter {

    private final MethodHandle biGetter;
    private final Class type;

    private MethodHandleBiGetter(MethodHandle biGetter, Class type) {
      this.biGetter = biGetter;
      this.type = type;
    }

    @Override
    public Object get(Object theObject, Object value) throws ReflectiveOperationException {
      try {
        return biGetter.invoke(theObject, value);
      } catch (Throwable ex) {
        throw new ReflectiveOperationException(ex);
      }
    }

    @Override
    public Class getType() {
      return type;
    }
  }

  private class MethodHandleAccessor implements Accessor {

    private final MethodHandle getter;
    private final MethodHandle setter;
    private final Class type;

    private MethodHandleAccessor(MethodHandle getter, MethodHandle setter, Class type) {
      this.getter = getter;
      this.setter = setter;
      this.type = type;
    }

    @Override
    public Object get(Object theObject) throws ReflectiveOperationException {
      try {
        return getter.invoke(theObject);
      } catch (Throwable ex) {
        throw new ReflectiveOperationException(ex);
      }
    }

    @Override
    public void set(Object theObject, Object value) throws ReflectiveOperationException {
      try {
        setter.invoke(theObject, value);
      } catch (Throwable ex) {
        throw new ReflectiveOperationException(ex);
      }
    }

    @Override
    public Class getType() {
      return type;
    }
  }
}
