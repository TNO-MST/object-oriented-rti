package nl.tno.oorti;

/**
 * Defines the interface of an attribute provided by the OORTI.
 *
 * @author bergtwvd
 */
public interface OOattribute {

  public String getName();
  
  public Object getValue(Object theObject) throws ReflectiveOperationException;
  
  public void setValue(Object theObject, Object value) throws ReflectiveOperationException;

  public Object getCookie();

  public void setCookie(Object cookie);
}
