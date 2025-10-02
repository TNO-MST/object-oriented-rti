package nl.tno.oorti.ooencoder.exceptions;

/**
 * Exception class for errors related to an OMT FOM datatype.
 *
 * @author bergtwvd
 */
public class InvalidType extends OOcodecException {

  public InvalidType(String msg) {
    super(msg);
  }

  public InvalidType(String message, Throwable cause) {
    super(message, cause);
  }
}
