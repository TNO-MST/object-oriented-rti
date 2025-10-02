package nl.tno.oorti.ooencoder.exceptions;

/**
 * Exception class for errors related to the structure of a Java Bean class.
 *
 * @author bergtwvd
 */
public class InvalidClassStructure extends OOcodecException {

  public InvalidClassStructure(String msg) {
    super(msg);
  }

  public InvalidClassStructure(String message, Throwable cause) {
    super(message, cause);
  }
}
