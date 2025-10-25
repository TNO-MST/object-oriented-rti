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
class HLAinteger64BECodec implements OmtBasicDatatypeCodec {

  static final int OCTETBOUNDARY = 8;
  static final int ENCODEDLENGTH = 8;

  HLAinteger64BECodec(OmtCodecFactory codecFactory, Type type, BasicData dt)
      throws InvalidType, InvalidClassStructure {
    Class clazz = (Class) type;

    if (type == null) {
      throw new InvalidClassStructure("Missing Type");
    }

    if (!clazz.equals(Long.class) && !clazz.equals(long.class)) {
      throw new InvalidType("Expected Long class, but got " + clazz.getSimpleName());
    }
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append("{")
        .append("\"type\" : ")
        .append('"')
        .append(Long.class.getTypeName())
        .append('"')
        .append(",")
        .append("\"name\" : ")
        .append('"' + OmtMimConstants.HLAINTEGER64BE + '"')
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
    byteWrapper.putLong((long) value);
  }

  @Override
  public final Object decode(ByteArrayWrapper byteWrapper, Object value, Object outer)
      throws ByteWrapperOutOfBoundsException {
    return byteWrapper.getLong();
  }

  @Override
  public String toString(Object value) {
    return Long.toString((long) value);
  }

  @Override
  public Object parse(String value) throws InvalidValue {
    try {
      return Long.valueOf(value);
    } catch (NumberFormatException ex) {
      throw new InvalidValue(ex.getMessage(), ex);
    }
  }
}
