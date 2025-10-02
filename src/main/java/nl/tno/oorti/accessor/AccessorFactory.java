package nl.tno.oorti.accessor;

import java.lang.reflect.Field;

/**
 * @author bergtwvd
 */
public interface AccessorFactory {

  /**
   * Create a get/set Accessor for the given Class field.`
   *
   * @param field to access
   * @return A get/set accessor
   * @throws java.lang.ReflectiveOperationException
   */
  public Accessor createAccessor(Field field) throws ReflectiveOperationException;

  /**
   * Create a get by method Accessor for the given Java Class and method name.
   *
   * @param clazz reference
   * @param methodName of method to use
   * @return A Getter
   * @throws java.lang.ReflectiveOperationException
   */
  public Getter createGetter(Class clazz, String methodName) throws ReflectiveOperationException;

  /**
   * Create a biGetter for the given Java Class, method name and argument type.
   *
   * @param clazz reference
   * @param methodName of method to use
   * @param argumentType of the method
   * @return A biGetter
   * @throws java.lang.ReflectiveOperationException
   */
  public BiGetter createBiGetter(Class clazz, String methodName, Class argumentType)
      throws ReflectiveOperationException;
}
