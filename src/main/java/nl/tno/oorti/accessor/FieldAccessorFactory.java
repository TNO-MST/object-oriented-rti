package nl.tno.oorti.accessor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author bergtwvd
 */
class FieldAccessorFactory implements AccessorFactory {

  @Override
  public Accessor createAccessor(Field field) throws NoSuchFieldException {

    // only look for public fields
    if (!Modifier.isPublic(field.getModifiers())) {
      throw new NoSuchFieldException("Field " + field.getName() + " is not public");
    }

    return new FieldAccessor(field);
  }

  @Override
  public Getter createGetter(Class clazz, String methodName) throws NoSuchMethodException {
    // only look for public methods
    Method getter = clazz.getMethod(methodName);
    return new FieldGetter(getter, getter.getReturnType());
  }

  @Override
  public BiGetter createBiGetter(Class clazz, String methodName, Class argumentType)
      throws NoSuchMethodException {
    // only look for public methods
    Method biGetter = clazz.getMethod(methodName, argumentType);
    return new FieldBiGetter(biGetter, biGetter.getReturnType());
  }

  private class FieldGetter implements Getter {

    Method getter;
    Class type;

    private FieldGetter(Method getter, Class type) {
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

  private class FieldBiGetter implements BiGetter {

    Method biGetter;
    Class type;

    private FieldBiGetter(Method biGetter, Class type) {
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

  private class FieldAccessor implements Accessor {

    Field field;
    Class clazz;

    private FieldAccessor(Field field) {
      this.field = field;
      this.clazz = field.getType();
    }

    @Override
    public Object get(Object theObject) throws IllegalArgumentException, IllegalAccessException {
      return field.get(theObject);
    }

    @Override
    public void set(Object theObject, Object value)
        throws IllegalArgumentException, IllegalAccessException {
      field.set(theObject, value);
    }

    @Override
    public Class getType() {
      return clazz;
    }
  }
}
