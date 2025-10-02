package nl.tno.oorti.ooencoder.omtcodec;

import java.lang.reflect.Type;
import nl.tno.omt.BasicDataRepresentationsType.BasicData;
import nl.tno.omt.helpers.OmtMimConstants;
import nl.tno.oorti.ooencoder.exceptions.InvalidClassStructure;
import nl.tno.oorti.ooencoder.exceptions.InvalidType;
import nl.tno.oorti.ooencoder.exceptions.InvalidValue;

/**
 * @author bergtwvd
 */
class HLAunsignedInteger64BECodec extends HLAinteger64BECodec {

  HLAunsignedInteger64BECodec(OmtCodecFactory codecFactory, Type type, BasicData dt)
      throws InvalidType, InvalidClassStructure {
    super(codecFactory, type, dt);
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append("{")
        .append("\"type\" : ")
        .append('"')
        .append(Short.class.getTypeName())
        .append('"')
        .append(",")
        .append("\"name\" : ")
        .append('"' + OmtMimConstants.HLAUNSIGNEDINTEGER64BE + '"')
        .append(",")
        .append("\"octetBoundary\" : ")
        .append(OCTETBOUNDARY)
        .append("}")
        .toString();
  }

  @Override
  public String toString(Object value) {
    return Long.toUnsignedString((long) value);
  }

  @Override
  public Object parse(String value) throws InvalidValue {
    try {
      return Long.parseUnsignedLong(value);
    } catch (NumberFormatException ex) {
      throw new InvalidValue(ex.getMessage(), ex);
    }
  }
}
