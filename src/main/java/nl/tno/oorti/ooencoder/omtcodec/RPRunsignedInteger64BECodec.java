package nl.tno.oorti.ooencoder.omtcodec;

import java.lang.reflect.Type;
import nl.tno.omt.BasicDataRepresentationsType.BasicData;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * @author bergtwvd
 */
class RPRunsignedInteger64BECodec extends HLAunsignedInteger64BECodec {

  RPRunsignedInteger64BECodec(OmtCodecFactory codecFactory, Type type, BasicData dt)
      throws OOcodecException {
    super(codecFactory, type, dt);
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
        .append('"' + "RPRunsignedInteger64BE" + '"')
        .append(",")
        .append("\"octetBoundary\" : ")
        .append(OCTETBOUNDARY)
        .append("}")
        .toString();
  }
}
