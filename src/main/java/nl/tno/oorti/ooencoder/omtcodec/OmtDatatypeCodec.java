package nl.tno.oorti.ooencoder.omtcodec;

import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * @author bergtwvd
 */
public interface OmtDatatypeCodec {
  /**
   * Get the octet boundary of the Type.
   *
   * @return octet boundary (must be >= 1).
   */
  public int getOctetBoundary();

  /**
   * Get the encoded length of the provided object in number of bytes. The length calculation starts
   * from the provided position in the byte array. Some encodings require the position in the byte
   * array to determine number of padding bytes to add. The returned value reflects the final
   * position plus one.
   *
   * @param position Byte array position to start length calculating from.
   * @param value The object to determine the encoded length for.
   * @return The final position of the encoded object, plus one.
   * @throws OOcodecException
   */
  public int getEncodedLength(int position, Object value) throws OOcodecException;

  /**
   * Encode the provided object in the byte wrapper.
   *
   * @param byteWrapper Byte wrapper to encode object in.
   * @param value The object to encode.
   * @throws OOcodecException
   */
  public void encode(ByteArrayWrapper byteWrapper, Object value) throws OOcodecException;

  /**
   * Decode the provide byte wrapper.
   *
   * @param byteWrapper Byte wrapper to decode object from.
   * @param value Existing object for in place copy of decoded data; use null to create a new
   *     object. Only applicable to structured types.
   * @param outer Outer object in case of a nested class; use null if decoded object is not in a
   *     nested class.
   * @return Decoded object.
   * @throws OOcodecException
   */
  public Object decode(ByteArrayWrapper byteWrapper, Object value, Object outer)
      throws OOcodecException;
}
