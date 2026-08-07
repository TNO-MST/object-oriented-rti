package nl.tno.oorti;

/**
 * Defines the interface of a parameter provided by the OORTI.
 *
 * @author bergtwvd
 */
public interface OOparameter {

  public String getName();

  public Object getValue(Object theInteraction) throws ReflectiveOperationException;

  public void setValue(Object theInteraction, Object value) throws ReflectiveOperationException;

  public Object getCookie();
  
  public void setCookie(Object cookie);
}
