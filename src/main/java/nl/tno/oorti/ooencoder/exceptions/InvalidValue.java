package nl.tno.oorti.ooencoder.exceptions;

/**
 * Exception class for errors related to a value.
 *
 * @author bergtwvd
 */
public class InvalidValue extends OOcodecException {

  public InvalidValue(String msg) {
    super(msg);
  }

  public InvalidValue(String message, Throwable cause) {
    super(message, cause);
  }
}
