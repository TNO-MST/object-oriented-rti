package nl.tno.oorti.ooencoder.exceptions;

/**
 * Super class for all encoder/decoder exceptions.
 *
 * @author bergtwvd
 */
public class OOcodecException extends Exception {

  public OOcodecException(String msg) {
    super(msg);
  }

  public OOcodecException(String message, Throwable cause) {
    super(message, cause);
  }
}
