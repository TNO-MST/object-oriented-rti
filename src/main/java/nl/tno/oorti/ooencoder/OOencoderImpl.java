package nl.tno.oorti.ooencoder;

import java.util.logging.Level;
import java.util.logging.Logger;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;
import nl.tno.oorti.ooencoder.omtcodec.ByteArrayWrapper;
import nl.tno.oorti.ooencoder.omtcodec.OmtDatatypeCodec;

/**
 * OOencoder implementation.
 *
 * @author bergtwvd
 */
class OOencoderImpl implements OOencoder {

  private final OmtDatatypeCodec codec;

  OOencoderImpl(OmtDatatypeCodec codec) {
    this.codec = codec;
  }

  @Override
  public Object decode(byte[] b, Object value, Object outer) throws OOcodecException {
    ByteArrayWrapper byteWrapper = new ByteArrayWrapper(b);
    value = this.codec.decode(byteWrapper, value, outer);

    if (byteWrapper.remaining() != 0) {
      Logger.getLogger(OOencoderImpl.class.getName())
          .log(
              Level.WARNING,
              "Not all bytes have been decoded for object type {0}, total {1}, remaining {2}",
              new Object[] {
                value.getClass().getSimpleName(),
                byteWrapper.array().length,
                byteWrapper.remaining()
              });
    }

    return value;
  }

  @Override
  public Object decode(byte[] b, Object value) throws OOcodecException {
    return this.decode(b, value, null);
  }

  @Override
  public Object decode(byte[] b) throws OOcodecException {
    return this.decode(b, null, null);
  }

  @Override
  public byte[] encode(Object value) throws OOcodecException {
    ByteArrayWrapper byteWrapper = new ByteArrayWrapper(this.codec.getEncodedLength(0, value));
    this.codec.encode(byteWrapper, value);

    if (byteWrapper.remaining() != 0) {
      Logger.getLogger(OOencoderImpl.class.getName())
          .log(
              Level.WARNING,
              "Not all bytes have been used for object type {0}, total {1}, remaining {2}",
              new Object[] {value.getClass(), byteWrapper.array().length, byteWrapper.remaining()});
    }

    return byteWrapper.array();
  }

  @Override
  public String toString() {
    return this.codec.toString();
  }
}
