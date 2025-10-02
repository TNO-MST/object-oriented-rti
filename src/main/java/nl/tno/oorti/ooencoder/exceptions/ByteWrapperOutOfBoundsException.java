package nl.tno.oorti.ooencoder.exceptions;

/**
 * @author bergtwvd
 */
public class ByteWrapperOutOfBoundsException extends OOcodecException {

  public ByteWrapperOutOfBoundsException(String msg) {
    super(msg);
  }

  public ByteWrapperOutOfBoundsException(String message, Throwable cause) {
    super(message, cause);
  }

  public ByteWrapperOutOfBoundsException(int index) {
    super("Array index out of range: " + index);
  }
}
