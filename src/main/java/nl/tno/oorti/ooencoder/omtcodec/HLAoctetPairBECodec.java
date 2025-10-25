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
class HLAoctetPairBECodec implements OmtBasicDatatypeCodec {

  static final int OCTETBOUNDARY = 2;
  static final int ENCODEDLENGTH = 2;

  HLAoctetPairBECodec(OmtCodecFactory codecFactory, Type type, BasicData dt)
      throws InvalidType, InvalidClassStructure {
    Class clazz = (Class) type;

    if (type == null) {
      throw new InvalidClassStructure("Missing Type");
    }

    if (!clazz.equals(Character.class) && !clazz.equals(char.class)) {
      throw new InvalidType("Expected Character class, but got " + clazz.getSimpleName());
    }
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append("{")
        .append("\"type\" : ")
        .append('"')
        .append(Character.class.getTypeName())
        .append('"')
        .append(",")
        .append("\"name\" : ")
        .append('"' + OmtMimConstants.HLAOCTETPAIRBE + '"')
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
    byteWrapper.putShort((short) (char) value);
  }

  @Override
  public final Object decode(ByteArrayWrapper byteWrapper, Object value, Object outer)
      throws ByteWrapperOutOfBoundsException {
    return (char) byteWrapper.getShort();
  }

  @Override
  public String toString(Object value) {
    return Integer.toString(Short.toUnsignedInt((short) (char) value));
  }

  @Override
  public Object parse(String value) throws InvalidValue {
    try {
      int v = Integer.parseUnsignedInt(value);
      if (v < 0 || v > 0xffff) {
        throw new InvalidValue("Value " + value + " out of range");
      }
      return (char) v;
    } catch (NumberFormatException ex) {
      throw new InvalidValue(ex.getMessage(), ex);
    }
  }
}
