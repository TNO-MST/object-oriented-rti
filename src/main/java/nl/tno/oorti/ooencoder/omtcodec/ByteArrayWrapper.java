package nl.tno.oorti.ooencoder.omtcodec;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import nl.tno.oorti.ooencoder.exceptions.ByteWrapperOutOfBoundsException;

/** Utility class for managing data in byte arrays. */
public class ByteArrayWrapper {

  /** The current position (or index) in the buffer. */
  private int _pos;

  /** The length of the buffer. */
  private int _limit;

  /** The backing byte array. */
  private byte[] _buffer;

  /** Construct a <code>ByteArrayWrapper</code> backed by a zero-length byte array. */
  public ByteArrayWrapper() {
    this(0);
  }

  /**
   * Construct a <code>ByteArrayWrapper</code> backed by a byte array with the specified <code>
   * length</code>.
   *
   * @param length length of the backing byte array
   */
  public ByteArrayWrapper(int length) {
    this(new byte[length]);
  }

  /**
   * Construct a <code>ByteArrayWrapper</code> backed by the specified byte array. (Changes to the
   * ByteWrapper will write through to the specified byte array.)
   *
   * @param buffer backing byte array
   */
  public ByteArrayWrapper(byte[] buffer) {
    this._buffer = buffer;
    this._limit = buffer.length;
    this._pos = 0;
  }

  /**
   * Verify that <code>length</code> bytes can be read.
   *
   * @param length number of byte to verify
   * @throws ByteWrapperOutOfBoundsException
   * @throws ArrayIndexOutOfBoundsException if <code>length</code> bytes can not be read
   */
  public final void verify(int length) throws ByteWrapperOutOfBoundsException {
    if (length < 0) {
      throw new ByteWrapperOutOfBoundsException(length);
    }
    if (_pos + length > _limit) {
      throw new ByteWrapperOutOfBoundsException(
          "Array index out of range, where pos="
              + _pos
              + " plus requested length="
              + length
              + " and limit="
              + _limit);
    }
  }

  /**
   * Writes <code>value</code> to the ByteArrayWrapper as a big-endian 64-bit integer. The current
   * position is increased by 8.
   *
   * @param value value to write
   * @throws ByteWrapperOutOfBoundsException if the bytes can not be written
   */
  public final void putLong(long value) throws ByteWrapperOutOfBoundsException {
    verify(8);
    _buffer[_pos++] = (byte) ((value >> 56) & 0xFFL);
    _buffer[_pos++] = (byte) ((value >> 48) & 0xFFL);
    _buffer[_pos++] = (byte) ((value >> 40) & 0xFFL);
    _buffer[_pos++] = (byte) ((value >> 32) & 0xFFL);
    _buffer[_pos++] = (byte) ((value >> 24) & 0xFFL);
    _buffer[_pos++] = (byte) ((value >> 16) & 0xFFL);
    _buffer[_pos++] = (byte) ((value >> 8) & 0xFFL);
    _buffer[_pos++] = (byte) ((value) & 0xFFL);
  }

  /**
   * Reads the next eight bytes of the ByteArrayWrapper as a big-endian 64-bit integer. The current
   * position is increased by 8.
   *
   * @return decoded value
   * @throws ByteWrapperOutOfBoundsException if the bytes can not be read
   */
  public final long getLong() throws ByteWrapperOutOfBoundsException {
    verify(8);
    return ((_buffer[_pos++] & 0xFFL) << 56)
        | ((_buffer[_pos++] & 0xFFL) << 48)
        | ((_buffer[_pos++] & 0xFFL) << 40)
        | ((_buffer[_pos++] & 0xFFL) << 32)
        | ((_buffer[_pos++] & 0xFFL) << 24)
        | ((_buffer[_pos++] & 0xFFL) << 16)
        | ((_buffer[_pos++] & 0xFFL) << 8)
        | (_buffer[_pos++] & 0xFFL);
  }

  /**
   * Writes <code>value</code> to the ByteArrayWrapper as a big-endian 32-bit integer. The current
   * position is increased by 4.
   *
   * @param value value to write
   * @throws ByteWrapperOutOfBoundsException if the bytes can not be written
   */
  public final void putInt(int value) throws ByteWrapperOutOfBoundsException {
    verify(4);
    _buffer[_pos++] = (byte) ((value >> 24) & 0xFF);
    _buffer[_pos++] = (byte) ((value >> 16) & 0xFF);
    _buffer[_pos++] = (byte) ((value >> 8) & 0xFF);
    _buffer[_pos++] = (byte) ((value) & 0xFF);
  }

  /**
   * Reads the next four bytes of the ByteArrayWrapper as a big-endian 32-bit integer. The current
   * position is increased by 4.
   *
   * @return decoded value
   * @throws ByteWrapperOutOfBoundsException if the bytes can not be read
   */
  public final int getInt() throws ByteWrapperOutOfBoundsException {
    verify(4);
    return ((_buffer[_pos++] & 0xFF) << 24)
        | ((_buffer[_pos++] & 0xFF) << 16)
        | ((_buffer[_pos++] & 0xFF) << 8)
        | ((_buffer[_pos++] & 0xFF));
  }

  /**
   * Writes <code>value</code> to the ByteArrayWrapper as a big-endian 16-bit integer. The current
   * position is increased by 2.
   *
   * @param value value to write
   * @throws ByteWrapperOutOfBoundsException if the bytes can not be written
   */
  public final void putShort(short value) throws ByteWrapperOutOfBoundsException {
    verify(2);
    _buffer[_pos++] = (byte) ((value >> 8) & (short) 0xFF);
    _buffer[_pos++] = (byte) ((value) & (short) 0xFF);
  }

  /**
   * Reads the next two bytes of the ByteArrayWrapper as a big-endian 16-bit integer. The current
   * position is increased by 2.
   *
   * @return decoded value
   * @throws ByteWrapperOutOfBoundsException if the bytes can not be read
   */
  public final short getShort() throws ByteWrapperOutOfBoundsException {
    verify(2);
    return (short) (((_buffer[_pos++] & 0xFF) << 8) | (_buffer[_pos++] & 0xFF));
  }

  /**
   * Writes <code>value</code> to the ByteArrayWrapper as a big-endian 32-bit float. The current
   * position is increased by 4.
   *
   * @param value value to write
   * @throws ByteWrapperOutOfBoundsException if the bytes can not be written
   */
  public final void putFloat(float value) throws ByteWrapperOutOfBoundsException {
    verify(4);
    int intBits = Float.floatToIntBits(value);
    _buffer[_pos++] = (byte) ((intBits >> 24) & 0xFF);
    _buffer[_pos++] = (byte) ((intBits >> 16) & 0xFF);
    _buffer[_pos++] = (byte) ((intBits >> 8) & 0xFF);
    _buffer[_pos++] = (byte) ((intBits) & 0xFF);
  }

  /**
   * Reads the next four bytes of the ByteArrayWrapper as a big-endian 32-bit float. The current
   * position is increased by 4.
   *
   * @return decoded value
   * @throws ByteWrapperOutOfBoundsException if the bytes can not be read
   */
  public final float getFloat() throws ByteWrapperOutOfBoundsException {
    verify(4);
    int intBits =
        (_buffer[_pos++] & 0xFF) << 24
            | (_buffer[_pos++] & 0xFF) << 16
            | (_buffer[_pos++] & 0xFF) << 8
            | (_buffer[_pos++] & 0xFF);
    return Float.intBitsToFloat(intBits);
  }

  /**
   * Writes <code>value</code> to the ByteArrayWrapper as a big-endian 64-bit float. The current
   * position is increased by 8.
   *
   * @param value value to write
   * @throws ByteWrapperOutOfBoundsException if the bytes can not be written
   */
  public final void putDouble(double value) throws ByteWrapperOutOfBoundsException {
    verify(8);
    long intBits = Double.doubleToLongBits(value);
    _buffer[_pos++] = (byte) ((intBits >> 56) & 0xFFL);
    _buffer[_pos++] = (byte) ((intBits >> 48) & 0xFFL);
    _buffer[_pos++] = (byte) ((intBits >> 40) & 0xFFL);
    _buffer[_pos++] = (byte) ((intBits >> 32) & 0xFFL);
    _buffer[_pos++] = (byte) ((intBits >> 24) & 0xFFL);
    _buffer[_pos++] = (byte) ((intBits >> 16) & 0xFFL);
    _buffer[_pos++] = (byte) ((intBits >> 8) & 0xFFL);
    _buffer[_pos++] = (byte) ((intBits) & 0xFFL);
  }

  /**
   * Reads the next four bytes of the ByteArrayWrapper as a big-endian 64-bit float. The current
   * position is increased by 8.
   *
   * @return decoded value
   * @throws ByteWrapperOutOfBoundsException if the bytes can not be read
   */
  public final double getDouble() throws ByteWrapperOutOfBoundsException {
    verify(8);
    long intBits =
        (_buffer[_pos++] & 0xFFL) << 56
            | (_buffer[_pos++] & 0xFFL) << 48
            | (_buffer[_pos++] & 0xFFL) << 40
            | (_buffer[_pos++] & 0xFFL) << 32
            | (_buffer[_pos++] & 0xFFL) << 24
            | (_buffer[_pos++] & 0xFFL) << 16
            | (_buffer[_pos++] & 0xFFL) << 8
            | (_buffer[_pos++] & 0xFFL);
    return Double.longBitsToDouble(intBits);
  }

  /**
   * Writes <code>value</code> to the ByteArrayWrapper as a UUID 128-bit value. The current position
   * is increased by 16.
   *
   * @param value value to write
   * @throws ByteWrapperOutOfBoundsException if the bytes can not be written
   */
  public final void putUuid(UUID value) throws ByteWrapperOutOfBoundsException {
    verify(16);

    long lsb = value.getLeastSignificantBits();
    long msb = value.getMostSignificantBits();

    _buffer[_pos++] = ((byte) ((msb >>> 56) & 0xFF));
    _buffer[_pos++] = ((byte) ((msb >>> 48) & 0xFF));
    _buffer[_pos++] = ((byte) ((msb >>> 40) & 0xFF));
    _buffer[_pos++] = ((byte) ((msb >>> 32) & 0xFF));
    _buffer[_pos++] = ((byte) ((msb >>> 24) & 0xFF));
    _buffer[_pos++] = ((byte) ((msb >>> 16) & 0xFF));
    _buffer[_pos++] = ((byte) ((msb >>> 8) & 0xFF));
    _buffer[_pos++] = ((byte) ((msb) & 0xFF));

    _buffer[_pos++] = ((byte) ((lsb >>> 56) & 0xFF));
    _buffer[_pos++] = ((byte) ((lsb >>> 48) & 0xFF));
    _buffer[_pos++] = ((byte) ((lsb >>> 40) & 0xFF));
    _buffer[_pos++] = ((byte) ((lsb >>> 32) & 0xFF));
    _buffer[_pos++] = ((byte) ((lsb >>> 24) & 0xFF));
    _buffer[_pos++] = ((byte) ((lsb >>> 16) & 0xFF));
    _buffer[_pos++] = ((byte) ((lsb >>> 8) & 0xFF));
    _buffer[_pos++] = ((byte) ((lsb) & 0xFF));
  }

  /**
   * Reads the next 16 bytes of the ByteArrayWrapper as a UUID 128-bit value. The current position
   * is increased by 16.
   *
   * @return decoded value
   * @throws ByteWrapperOutOfBoundsException if the bytes can not be read
   */
  public final UUID getUuid() throws ByteWrapperOutOfBoundsException {
    verify(16);

    long msb =
        (_buffer[_pos++] & 0xFFL) << 56
            | (_buffer[_pos++] & 0xFFL) << 48
            | (_buffer[_pos++] & 0xFFL) << 40
            | (_buffer[_pos++] & 0xFFL) << 32
            | (_buffer[_pos++] & 0xFFL) << 24
            | (_buffer[_pos++] & 0xFFL) << 16
            | (_buffer[_pos++] & 0xFFL) << 8
            | (_buffer[_pos++] & 0xFFL);

    long lsb =
        (_buffer[_pos++] & 0xFFL) << 56
            | (_buffer[_pos++] & 0xFFL) << 48
            | (_buffer[_pos++] & 0xFFL) << 40
            | (_buffer[_pos++] & 0xFFL) << 32
            | (_buffer[_pos++] & 0xFFL) << 24
            | (_buffer[_pos++] & 0xFFL) << 16
            | (_buffer[_pos++] & 0xFFL) << 8
            | (_buffer[_pos++] & 0xFFL);

    return new UUID(msb, lsb);
  }

  /**
   * Writes <code>value</code> to the ByteArrayWrapper as a series of UNICODE characters. The
   * current position is increased by 2 times the length of the string.
   *
   * @param value value to write
   * @throws ByteWrapperOutOfBoundsException if the bytes can not be written
   */
  public final void putUnicodeString(String value) throws ByteWrapperOutOfBoundsException {
    verify(2 * value.length());

    for (int i = 0; i < value.length(); i++) {
      short c = (short) value.charAt(i);
      _buffer[_pos++] = (byte) ((c >> 8) & (short) 0xFF);
      _buffer[_pos++] = (byte) ((c) & (short) 0xFF);
    }
  }

  /**
   * Reads the next length number of UNICODE characters. The current position is increased by 2
   * times the length.
   *
   * @param length
   * @return decoded value
   * @throws ByteWrapperOutOfBoundsException if the bytes can not be read
   */
  public final String getUnicodeString(int length) throws ByteWrapperOutOfBoundsException {
    verify(2 * length);

    char[] c = new char[length];
    for (int i = 0; i < length; i++) {
      c[i] = (char) ((_buffer[_pos++] << 8) + _buffer[_pos++]);
    }

    return new String(c);
  }

  /**
   * Writes <code>value</code> to the ByteArrayWrapper as a series of ASCII characters. The current
   * position is increased by the length of the string.
   *
   * @param value value to write
   * @throws ByteWrapperOutOfBoundsException if the bytes can not be written
   */
  public final void putAsciiString(String value) throws ByteWrapperOutOfBoundsException {
    verify(value.length());

    for (int i = 0; i < value.length(); i++) {
      short c = (short) value.charAt(i);
      _buffer[_pos++] = (byte) (c & (short) 0xFF);
    }
  }

  /**
   * Reads the next length number of ASCII characters. The current position is increased by the
   * length.
   *
   * @param length
   * @return decoded value
   * @throws ByteWrapperOutOfBoundsException if the bytes can not be read
   */
  public final String getAsciiString(int length) throws ByteWrapperOutOfBoundsException {
    verify(length);

    int pos = this._pos;
    this.advance(length);

    return new String(this.array(), pos, length, StandardCharsets.US_ASCII);
  }

  /**
   * Writes <code>byte</code> to the ByteArrayWrapper and advances the current position by 1.
   *
   * @param value byte to write
   * @throws ByteWrapperOutOfBoundsException if the bytes can not be written
   */
  public final void put(byte value) throws ByteWrapperOutOfBoundsException {
    verify(1);
    _buffer[_pos++] = value;
  }

  /**
   * Reads the next byte of the ByteArrayWrapper. The current position is increased by 1.
   *
   * @return decoded value
   * @throws ByteWrapperOutOfBoundsException if the bytes can not be read
   */
  public final byte get() throws ByteWrapperOutOfBoundsException {
    verify(1);
    return _buffer[_pos++];
  }

  /**
   * Writes a byte array to the ByteArrayWrapper and advances the current position by the size of
   * the byte array.
   *
   * @param src byte array to write
   * @throws ByteWrapperOutOfBoundsException if the bytes can not be written
   */
  public void put(byte[] src) throws ByteWrapperOutOfBoundsException {
    verify(src.length);
    System.arraycopy(src, 0, _buffer, _pos, src.length);
    _pos += src.length;
  }

  /**
   * Adds <code>length</code> padding bytes to the ByteArrayWrapper. The current position is
   * increased by the length.
   *
   * @param length padding bytes to add
   * @throws ByteWrapperOutOfBoundsException if the bytes can not be written
   */
  public final void putPadding(int length) throws ByteWrapperOutOfBoundsException {
    verify(length);
    for (int i = 0; i < length; i++) {
      _buffer[_pos++] = (byte) 0;
    }
  }

  /**
   * Returns the backing array.
   *
   * @return the backing byte array
   */
  public final byte[] array() {
    return _buffer;
  }

  /**
   * Returns the current position.
   *
   * @return the current position within the byte array
   * @see #array()
   */
  public final int getPos() {
    return _pos;
  }

  /**
   * Returns the number of remaining bytes in the byte array.
   *
   * @return the number of remaining bytes in the byte array
   */
  public final int remaining() {
    return _limit - _pos;
  }

  /**
   * Advances the current position by <code>n</code>.
   *
   * @param n number of positions to advance
   * @throws ByteWrapperOutOfBoundsException if the position can not be advanced
   */
  public final void advance(int n) throws ByteWrapperOutOfBoundsException {
    verify(n);
    _pos += n;
  }
}
