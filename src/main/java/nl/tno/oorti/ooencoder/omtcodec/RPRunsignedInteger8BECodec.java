package nl.tno.oorti.ooencoder.omtcodec;

import java.lang.reflect.Type;
import nl.tno.omt.BasicDataRepresentationsType.BasicData;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * @author bergtwvd
 */
class RPRunsignedInteger8BECodec extends HLAoctetCodec {

  RPRunsignedInteger8BECodec(OmtCodecFactory codecFactory, Type type, BasicData dt)
      throws OOcodecException {
    super(codecFactory, type, dt);
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
        .append('"' + "RPRunsignedInteger8BE" + '"')
        .append(",")
        .append("\"octetBoundary\" : ")
        .append(OCTETBOUNDARY)
        .append("}")
        .toString();
  }
}
