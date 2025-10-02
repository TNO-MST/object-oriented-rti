package nl.tno.oorti.ooencoder.omtcodec;

import java.lang.reflect.Type;
import nl.tno.omt.BasicDataRepresentationsType.BasicData;
import nl.tno.omt.helpers.OmtMimConstants;
import nl.tno.oorti.ooencoder.exceptions.ByteWrapperOutOfBoundsException;
import nl.tno.oorti.ooencoder.exceptions.InvalidClassStructure;
import nl.tno.oorti.ooencoder.exceptions.InvalidType;
import nl.tno.oorti.ooencoder.exceptions.InvalidValue;

/**
 * @author bergtwvd
 */
class HLAoctetCodec implements OmtBasicDatatypeCodec {

  static final int OCTETBOUNDARY = 1;
  static final int ENCODEDLENGTH = 1;

  HLAoctetCodec(OmtCodecFactory codecFactory, Type type, BasicData dt)
      throws InvalidType, InvalidClassStructure {
    Class clazz = (Class) type;

    /** Perform sanity checks */
    if (type == null) {
      throw new InvalidClassStructure("Missing Type");
    }

    if (!clazz.equals(Byte.class) && !clazz.equals(byte.class)) {
      throw new InvalidType("Expected Byte class, but got " + clazz.getSimpleName());
    }
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append("{")
        .append("\"type\" : ")
        .append('"')
        .append(Byte.class.getTypeName())
        .append('"')
        .append(",")
        .append("\"name\" : ")
        .append('"' + OmtMimConstants.HLAOCTET + '"')
        .append(",")
        .append("\"octetBoundary\" : ")
        .append(OCTETBOUNDARY)
        .append("}")
        .toString();
  }

  @Override
  public final int getOctetBoundary() {
    return OCTETBOUNDARY;
  }

  @Override
  public final int getEncodedLength(int position, Object value) {
    return position + ENCODEDLENGTH;
  }

  @Override
  public final void encode(ByteArrayWrapper byteWrapper, Object value)
      throws ByteWrapperOutOfBoundsException {
    byteWrapper.put((byte) value);
  }

  @Override
  public final Object decode(ByteArrayWrapper byteWrapper, Object value, Object outer)
      throws ByteWrapperOutOfBoundsException {
    return byteWrapper.get();
  }

  @Override
  public String toString(Object value) {
    // We assume a byte is unsigned. We go through the Integer string creation to ensure no sign is
    // generated.
    return Integer.toString(((byte) value) & 0xff);
  }

  @Override
  public Object parse(String value) throws InvalidValue {
    try {
      // We assume that a byte is unsigned. We go through the Integer parsing, and then
      // cast the result to a byte.
      return Integer.valueOf(value).byteValue();
    } catch (NumberFormatException ex) {
      throw new InvalidValue(ex.getMessage(), ex);
    }
  }
}
