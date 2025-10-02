package nl.tno.oorti.accessor;

/**
 * @author bergtwvd
 */
public interface Getter {

  public Object get(Object theObject) throws ReflectiveOperationException;

  public Class getType();
}
