package nl.tno.oorti.accessor;

/**
 * @author bergtwvd
 */
public interface BiGetter {

  public Object get(Object theObject, Object value) throws ReflectiveOperationException;

  public Class getType();
}
