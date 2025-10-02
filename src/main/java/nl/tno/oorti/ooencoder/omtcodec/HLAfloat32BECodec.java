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
class HLAfloat32BECodec implements OmtBasicDatatypeCodec {

  static final int OCTETBOUNDARY = 4;
  static final int ENCODEDLENGTH = 4;

  HLAfloat32BECodec(OmtCodecFactory codecFactory, Type type, BasicData dt)
      throws InvalidType, InvalidClassStructure {
    Class clazz = (Class) type;

    /** Perform sanity checks */
    if (type == null) {
      throw new InvalidClassStructure("Missing Type");
    }

    if (!clazz.equals(Float.class) && !clazz.equals(float.class)) {
      throw new InvalidType("Expected Float class, but got " + clazz.getSimpleName());
    }
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append("{")
        .append("\"type\" : ")
        .append('"')
        .append(Float.class.getTypeName())
        .append('"')
        .append(",")
        .append("\"name\" : ")
        .append('"' + OmtMimConstants.HLAFLOAT32BE + '"')
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
    byteWrapper.putFloat((float) value);
  }

  @Override
  public final Object decode(ByteArrayWrapper byteWrapper, Object value, Object outer)
      throws ByteWrapperOutOfBoundsException {
    return byteWrapper.getFloat();
  }

  @Override
  public String toString(Object value) {
    return Float.toString((float) value);
  }

  @Override
  public Object parse(String value) throws InvalidValue {
    try {
      return Float.valueOf(value);
    } catch (NumberFormatException ex) {
      throw new InvalidValue(ex.getMessage(), ex);
    }
  }
}
