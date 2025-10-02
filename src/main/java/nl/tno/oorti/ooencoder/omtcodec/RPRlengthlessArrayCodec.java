package nl.tno.oorti.ooencoder.omtcodec;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import nl.tno.omt.ArrayDataTypesType.ArrayData;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.omt.helpers.OmtMimConstants;
import nl.tno.oorti.accessor.ArrayAccessor;
import nl.tno.oorti.ooencoder.exceptions.InvalidClassStructure;
import nl.tno.oorti.ooencoder.exceptions.InvalidType;
import nl.tno.oorti.ooencoder.exceptions.InvalidValue;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * @author bergtwvd
 */
class RPRlengthlessArrayCodec implements OmtDatatypeCodec {

  enum DataType {
    ARRAY,
    UNICODESTRING,
    ASCIISTRING
  };

  final Type type;
  final ArrayData dt;
  final OmtDatatypeCodec componentCodec;
  final String componentName;
  final ArrayAccessor arrayAccessor;
  final DataType datatype;
  final int minCardinality, maxCardinality;
  final boolean isDynamic;
  final int octetBoundary;

  RPRlengthlessArrayCodec(OmtCodecFactory codecFactory, Type type, ArrayData dt)
      throws OOcodecException {

    if (type == null) {
      throw new InvalidClassStructure("Missing Type");
    }

    if (dt == null) {
      throw new InvalidType("Missing OMT datatype");
    }

    this.type = type;
    this.dt = dt;

    if (type instanceof ParameterizedType pt) {
      if (pt.getActualTypeArguments().length != 1) {
        throw new InvalidClassStructure(
            "Type "
                + pt.getTypeName()
                + " has not the expected number of type arguments (found "
                + pt.getActualTypeArguments().length
                + ")");
      }

      if (!pt.getRawType().getTypeName().equals(List.class.getName())) {
        throw new InvalidClassStructure(
            "Type " + pt.getTypeName() + " found, but Class List expected");
      }

      this.datatype = DataType.ARRAY;

      // get the component type and datatype name
      Type componentType = pt.getActualTypeArguments()[0];
      String componentDataTypeName = dt.getDataType().getValue();

      // create codec for the array component type
      this.componentCodec = codecFactory.createDatatype(componentType, componentDataTypeName);
      this.octetBoundary = this.componentCodec.getOctetBoundary();
      this.componentName = pt.getTypeName();

      // create accessor for the array component type
      this.arrayAccessor = ArrayAccessor.create(List.class);
    } else {
      Class clazz = (Class) type;

      // Ensure class is an array or a String
      if (!clazz.isArray() && !clazz.equals(String.class)) {
        throw new InvalidClassStructure(
            "Expected Array or String class, but got " + clazz.getSimpleName());
      }

      // get the component type and datatype name
      Class componentType;
      String componentDataTypeName;

      if (clazz.isArray()) {
        this.datatype = DataType.ARRAY;
        componentType = clazz.getComponentType();
        componentDataTypeName = dt.getDataType().getValue();
      } else { // String
        switch (dt.getDataType().getValue()) {
          case OmtMimConstants.HLAUNICODECHAR -> {
            this.datatype = DataType.UNICODESTRING;
            componentType = char.class;
            componentDataTypeName = OmtMimConstants.HLAUNICODECHAR;
          }
          case OmtMimConstants.HLAASCIICHAR -> {
            this.datatype = DataType.ASCIISTRING;
            componentType = byte.class;
            componentDataTypeName = OmtMimConstants.HLAASCIICHAR;
          }
          default ->
              throw new InvalidType(
                  "Expected datatype HLAunicodeChar or HLAASCIIchar, but got "
                      + dt.getDataType().getValue());
        }
      }

      // get component name
      this.componentName = componentType.getSimpleName();

      // create codec for the array component type
      this.componentCodec = codecFactory.createDatatype(componentType, componentDataTypeName);
      this.octetBoundary = this.componentCodec.getOctetBoundary();

      // create accessor for the array component type
      this.arrayAccessor = ArrayAccessor.create(componentType);
    }

    /** Set the remaining properties */
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
        .append("\"component\" : ")
        .append('"')
        .append(this.componentCodec.toString())
        .append('"')
        .append(",")
        .append("\"encoding\" : ")
        .append('"')
        .append(dt.getEncoding().getValue())
        .append('"')
        .append(",")
        .append("\"octetBoundary\" : ")
        .append(octetBoundary)
        .append("}")
        .toString();
  }

  private int getPadding(int size, int v) {
    int e = size % v;
    return (e == 0) ? 0 : v - e;
  }

  @Override
  public final int getOctetBoundary() {
    return this.octetBoundary;
  }

  @Override
  public final int getEncodedLength(int position, Object value) throws OOcodecException {
    switch (this.datatype) {
      default:
      case ARRAY:
        int cardinality = arrayAccessor.length(value);
        if (cardinality > 0) {
          for (int i = 0; i < cardinality - 1; i++) {
            int offset = position;

            // add size of component i
            position = componentCodec.getEncodedLength(position, arrayAccessor.get(value, i));

            // add padding
            position += getPadding(position - offset, this.getOctetBoundary());
          }

          // add size of last component
          position =
              componentCodec.getEncodedLength(position, arrayAccessor.get(value, cardinality - 1));
        }
        return position;
      case UNICODESTRING:
        return position + ((String) value).length() * 2;
      case ASCIISTRING:
        return position + ((String) value).length();
    }
  }

  @Override
  public final void encode(ByteArrayWrapper byteWrapper, Object value) throws OOcodecException {
    switch (this.datatype) {
      default:
      case ARRAY:
        int cardinality = arrayAccessor.length(value);

        if (!this.isDynamic) {
          // check that length is within defined cardinality
          if (cardinality < this.minCardinality || cardinality > this.maxCardinality) {
            throw new InvalidValue(
                "Array "
                    + this.componentName
                    + ": "
                    + cardinality
                    + " outside range ["
                    + this.minCardinality
                    + ","
                    + this.maxCardinality
                    + "]");
          }
        }

        if (cardinality > 0) {
          for (int i = 0; i < cardinality - 1; i++) {
            int offset = byteWrapper.getPos();
            componentCodec.encode(byteWrapper, arrayAccessor.get(value, i));
            byteWrapper.putPadding(
                this.getPadding(byteWrapper.getPos() - offset, this.getOctetBoundary()));
          }
          componentCodec.encode(byteWrapper, arrayAccessor.get(value, cardinality - 1));
        }

        break;

      case UNICODESTRING:
        String unicodeString = ((String) value);
        int unicodeStringLength = unicodeString.length();

        if (!this.isDynamic) {
          // check that length is within defined cardinality
          if (unicodeStringLength < this.minCardinality
              || unicodeStringLength > this.maxCardinality) {
            throw new InvalidValue(
                "Array "
                    + this.componentName
                    + ": "
                    + unicodeStringLength
                    + " outside range ["
                    + this.minCardinality
                    + ","
                    + this.maxCardinality
                    + "]");
          }
        }

        for (int i = 0; i < unicodeStringLength; i++) {
          byteWrapper.putShort((short) unicodeString.charAt(i));
        }

        break;

      case ASCIISTRING:
        String asciiString = (String) value;
        int asciiStringLength = asciiString.length();

        if (!this.isDynamic) {
          // check that length is within defined cardinality
          if (asciiStringLength < this.minCardinality || asciiStringLength > this.maxCardinality) {
            throw new InvalidValue(
                "Array "
                    + this.componentName
                    + ": "
                    + asciiStringLength
                    + " outside range ["
                    + this.minCardinality
                    + ","
                    + this.maxCardinality
                    + "]");
          }
        }

        for (int i = 0; i < asciiStringLength; i++) {
          byteWrapper.put((byte) asciiString.charAt(i));
        }

        break;
    }
  }

  @Override
  public final Object decode(ByteArrayWrapper byteWrapper, Object value, Object outer)
      throws OOcodecException {
    int cardinality;

    switch (this.datatype) {
      default:
      case ARRAY:
        List<Object> list = new ArrayList<>();

        // offset of record within byteWrapper
        int offset = byteWrapper.getPos();

        while (byteWrapper.remaining() > 0) {
          list.add(this.componentCodec.decode(byteWrapper, null, outer));

          if (byteWrapper.remaining() > 0) {
            // consume padding
            byteWrapper.advance(
                this.getPadding(byteWrapper.getPos() - offset, this.getOctetBoundary()));
          }
        }

        if (!this.isDynamic) {
          // check that length is within defined cardinality
          if (list.size() < this.minCardinality || list.size() > this.maxCardinality) {
            throw new InvalidValue(
                "Array "
                    + this.componentName
                    + ": "
                    + list.size()
                    + " outside range ["
                    + this.minCardinality
                    + ","
                    + this.maxCardinality
                    + "]");
          }
        }

        // create new Java Class instance
        value = arrayAccessor.newInstance(list.size());

        for (int i = 0; i < list.size(); i++) {
          arrayAccessor.set(value, i, list.get(i));
        }

        return value;

      case UNICODESTRING:
        cardinality = byteWrapper.remaining() / 2;

        if (!this.isDynamic) {
          // check that length is within defined cardinality
          if (cardinality < this.minCardinality || cardinality > this.maxCardinality) {
            throw new InvalidValue(
                "Array "
                    + this.componentName
                    + ": "
                    + cardinality
                    + " outside range ["
                    + this.minCardinality
                    + ","
                    + this.maxCardinality
                    + "]");
          }
        }

        char[] c = new char[cardinality];
        for (int i = 0; i < cardinality; i++) {
          c[i] = (char) byteWrapper.getShort();
        }

        return new String(c);

      case ASCIISTRING:
        cardinality = byteWrapper.remaining();

        if (!this.isDynamic) {
          // check that length is within defined cardinality
          if (cardinality < this.minCardinality || cardinality > this.maxCardinality) {
            throw new InvalidValue(
                "Array "
                    + this.componentName
                    + ": "
                    + cardinality
                    + " outside range ["
                    + this.minCardinality
                    + ","
                    + this.maxCardinality
                    + "]");
          }
        }

        String result =
            new String(
                byteWrapper.array(),
                byteWrapper.getPos(),
                byteWrapper.remaining(),
                StandardCharsets.US_ASCII);
        byteWrapper.advance(cardinality);
        return result;
    }
  }
}
