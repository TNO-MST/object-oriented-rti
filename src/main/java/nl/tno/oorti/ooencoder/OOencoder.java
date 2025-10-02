package nl.tno.oorti.ooencoder;

import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * Encoder interface to encode or decode an object.
 *
 * @author bergtwvd
 * @param <T>: type of object.
 */
public interface OOencoder<T> {

  /**
   * Decode object from bytes, where object is the value of a Java Class attribute.
   *
   * @param b: Bytes to be decoded
   * @param value: Java Class attribute value to be used for storing the decoded value or null when
   *     a new object instance is to be returned; the value is only used with complex data types
   *     (records, arrays)
   * @param outer: Enclosing Java Class instance for nested classes or null for none
   * @return Decoded object
   * @throws OOcodecException On error.
   */
  public T decode(byte[] b, T value, Object outer) throws OOcodecException;

  public T decode(byte[] b, T value) throws OOcodecException;

  public T decode(byte[] b) throws OOcodecException;

  /**
   * Encode object to bytes.
   *
   * @param value The object to be encoded
   * @return Encoded object as bytes
   * @throws OOcodecException On error.
   */
  public byte[] encode(T value) throws OOcodecException;
}
