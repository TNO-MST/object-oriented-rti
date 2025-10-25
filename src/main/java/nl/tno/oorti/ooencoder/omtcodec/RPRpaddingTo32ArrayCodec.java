package nl.tno.oorti.ooencoder.omtcodec;

import java.lang.reflect.Type;
import nl.tno.omt.ArrayDataTypesType.ArrayData;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * @author bergtwvd
 */
class RPRpaddingTo32ArrayCodec extends HLAdataElementCodec implements OmtDatatypeCodec {

  RPRpaddingTo32ArrayCodec(OmtCodecFactory codecFactory, Type type, ArrayData dt) {}
  
  @Override
  public final int getOctetBoundary() {
    return 1;
  }

  @Override
  public final int getEncodedLength(int position, Object value) {
    return position + paddingSize(position, 4);
  }

  @Override
  public final void encode(ByteArrayWrapper byteWrapper, Object value) throws OOcodecException {
    byteWrapper.putPadding(paddingSize(byteWrapper.getPos(), 4));
  }

  @Override
  public final Object decode(ByteArrayWrapper byteWrapper, Object value, Object outer)
      throws OOcodecException {
    byteWrapper.advance(paddingSize(byteWrapper.getPos(), 4));
    return null;
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
        .append('"')
        .append("RPRpaddingTo32Array")
        .append('"')
        .append("}")
        .toString();
  }
}
