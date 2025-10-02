package nl.tno.oorti.accessor;

/**
 * @author bergtwvd
 */
public interface Accessor {

  /**
   * @param theObject Get object attribute value
   * @return The attribute value
   * @throws java.lang.ReflectiveOperationException
   */
  public Object get(Object theObject) throws ReflectiveOperationException;

  /**
   * @param theObject Set object attribute value
   * @param value The attribute value
   * @throws java.lang.ReflectiveOperationException
   */
  public void set(Object theObject, Object value) throws ReflectiveOperationException;

  /**
   * @return Type of attribute
   */
  public Class getType();
}
