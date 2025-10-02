package nl.tno.oorti.accessor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import nl.tno.omt.helpers.OmtJavaMapping;

/**
 * @author bergtwvd
 */
class MethodAccessorFactory implements AccessorFactory {

  @Override
  public Accessor createAccessor(Field field) throws NoSuchMethodException {
    Class clazz = field.getDeclaringClass();
    String name = field.getName();

    Method getter = clazz.getMethod(OmtJavaMapping.toJavaGetterName(name));
    Method setter = clazz.getMethod(OmtJavaMapping.toJavaSetterName(name), getter.getReturnType());

    return new MethodAccessor(getter, setter, getter.getReturnType());
  }

  @Override
  public Getter createGetter(Class clazz, String methodName) throws NoSuchMethodException {
    Method getter = clazz.getMethod(methodName);
    return new MethodGetter(getter, getter.getReturnType());
  }

  @Override
  public BiGetter createBiGetter(Class clazz, String methodName, Class argumentType)
      throws NoSuchMethodException {
    Method biGetter = clazz.getMethod(methodName, argumentType);
    return new MethodBiGetter(biGetter, biGetter.getReturnType());
  }

  private class MethodGetter implements Getter {

    final Method getter;
    final Class type;

    private MethodGetter(Method getter, Class type) {
      this.getter = getter;
      this.type = type;
    }

    @Override
    public Object get(Object theObject)
        throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
      return getter.invoke(theObject);
    }

    @Override
    public Class getType() {
      return type;
    }
  }

  private class MethodBiGetter implements BiGetter {

    Method biGetter;
    Class type;

    private MethodBiGetter(Method biGetter, Class type) {
      this.biGetter = biGetter;
      this.type = type;
    }

    @Override
    public Object get(Object theObject, Object value)
        throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
      return biGetter.invoke(theObject, value);
    }

    @Override
    public Class getType() {
      return type;
    }
  }

  private class MethodAccessor implements Accessor {

    final Method getter;
    final Method setter;
    final Class clazz;

    private MethodAccessor(Method getter, Method setter, Class clazz) {
      this.getter = getter;
      this.setter = setter;
      this.clazz = clazz;
    }

    @Override
    public Object get(Object theObject)
        throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
      return getter.invoke(theObject);
    }

    @Override
    public void set(Object theObject, Object value)
        throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
      setter.invoke(theObject, value);
    }

    @Override
    public Class getType() {
      return clazz;
    }
  }
}
