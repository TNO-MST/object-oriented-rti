package nl.tno.oorti.ooencoder.omtcodec;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import nl.tno.omt.ArrayDataTypesType.ArrayData;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.omt.helpers.OmtMimConstants;
import nl.tno.oorti.ooencoder.exceptions.InvalidClassStructure;
import nl.tno.oorti.ooencoder.exceptions.InvalidType;
import nl.tno.oorti.ooencoder.exceptions.InvalidValue;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * @author bergtwvd
 */
class RPRnullTerminatedArrayCodec implements OmtDatatypeCodec {

  static final int OCTETBOUNDARY = 1;

  final Type type;
  final ArrayData dt;
  final int minCardinality, maxCardinality;
  final boolean isDynamic;

  RPRnullTerminatedArrayCodec(OmtCodecFactory codecFactory, Type type, ArrayData dt)
      throws OOcodecException {

    if (type == null) {
      throw new InvalidClassStructure("Missing Type");
    }

    if (dt == null) {
      throw new InvalidType("Missing OMT datatype");
    }

    if (!type.equals(String.class)) {
      throw new InvalidClassStructure("Expected String class, but got " + type.getTypeName());
    }

    // we only support HLAASCIIchar
    if (!dt.getDataType().getValue().equals(OmtMimConstants.HLAASCIICHAR)) {
      throw new InvalidType(
          "Expected component type HLAASCIIchar, but got " + dt.getDataType().getValue());
    }

    this.type = type;
    this.dt = dt;
    this.isDynamic = dt.getCardinality().getValue().equals(OmtMimConstants.DYNAMIC);
    int v[] = this.geCardinality(dt);
    this.minCardinality = v[0];
    this.maxCardinality = v[1];
  }

  final int[] geCardinality(ArrayData dt) throws InvalidType {
    int[] v = OmtFunctions.geCardinality(dt);
    if (v == null) {
      throw new InvalidType("Incorrectly formatted cardinality " + dt.getCardinality().getValue());
    } else {
      return v;
    }
  }

  @Override
  public final int getOctetBoundary() {
    return OCTETBOUNDARY;
  }

  @Override
  public final int getEncodedLength(int position, Object value) {
    return position + ((String) value).length() + 1;
  }

  @Override
  public final void encode(ByteArrayWrapper byteWrapper, Object value) throws OOcodecException {
    String asciiString = ((String) value);

    if (asciiString.length() < this.minCardinality || asciiString.length() > this.maxCardinality) {
      throw new InvalidValue(
          "Array: "
              + asciiString.length()
              + " outside range ["
              + this.minCardinality
              + ","
              + this.maxCardinality
              + "]");
    }

    byteWrapper.put(asciiString.getBytes(StandardCharsets.US_ASCII));
    byteWrapper.put((byte) 0);
  }

  @Override
  @SuppressWarnings("empty-statement")
  public final Object decode(ByteArrayWrapper byteWrapper, Object value, Object outer)
      throws OOcodecException {
    int start = byteWrapper.getPos();
    while (byteWrapper.get() != 0)
      ;
    int end = byteWrapper.getPos();

    String sval =
        new String(byteWrapper.array(), start, end - start - 1, StandardCharsets.US_ASCII);

    if (sval.length() < this.minCardinality || sval.length() > this.maxCardinality) {
      throw new InvalidValue(
          "Array: "
              + sval.length()
              + " outside range ["
              + this.minCardinality
              + ","
              + this.maxCardinality
              + "]");
    }

    return sval;
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append("{")
        .append("\"type\" : ")
        .append('"')
        .append(type.getTypeName())
        .append('"')
        .append(",")
        .append("\"name\" : ")
        .append('"')
        .append(dt.getName().getValue())
        .append('"')
        .append(",")
        .append("\"cardinality\" : ")
        .append('"')
        .append(dt.getCardinality().getValue())
        .append('"')
        .append(",")
        .append("\"encoding\" : ")
        .append('"')
        .append(dt.getEncoding().getValue())
        .append('"')
        .append(",")
        .append("\"octetBoundary\" : ")
        .append(OCTETBOUNDARY)
        .append("}")
        .toString();
  }
}
