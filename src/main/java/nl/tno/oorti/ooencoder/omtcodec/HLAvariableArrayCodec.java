package nl.tno.oorti.ooencoder.omtcodec;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
class HLAvariableArrayCodec extends HLAdataElementCodec implements OmtDatatypeCodec {

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

  HLAvariableArrayCodec(OmtCodecFactory codecFactory, Type type) throws OOcodecException {
    this(codecFactory, type, null);
  }

  HLAvariableArrayCodec(OmtCodecFactory codecFactory, Type type, ArrayData dt)
      throws OOcodecException {

    if (type == null) {
      throw new InvalidClassStructure("Missing Type");
    }

    if (dt != null) {
      if (!dt.getEncoding().getValue().equals(OmtMimConstants.HLAVARIABLEARRAY)) {
        throw new InvalidType(
            "Expected encoding HLAvariableArray, but got " + dt.getEncoding().getValue());
      }
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

      // get the component type and name
      Type componentType = pt.getActualTypeArguments()[0];
      String componentDataType = (dt == null) ? null : dt.getDataType().getValue();

      // create accessor the array component type
      this.arrayAccessor = ArrayAccessor.create(List.class);

      // create codec for the array component type
      this.componentCodec = codecFactory.createDatatype((Type) componentType, componentDataType);

      this.componentName = type.getTypeName();
    } else {
      Class clazz = (Class) type;

      if (!clazz.isArray() && !clazz.equals(String.class)) {
        throw new InvalidClassStructure(
            "Expected Array or String class, but got " + clazz.getSimpleName());
      }

      /** Set the properties */
      if (clazz.isArray()) {
        this.datatype = DataType.ARRAY;

        // get the component type and name
        Class componentType = clazz.getComponentType();
        String componentDataTypeName = (dt == null) ? null : dt.getDataType().getValue();

        // get component name
        this.componentName = clazz.getComponentType().getSimpleName();

        // create codec for the array component type
        this.componentCodec =
            codecFactory.createDatatype((Class) componentType, componentDataTypeName);

        // create accessor the array component type
        this.arrayAccessor = ArrayAccessor.create(componentType);
      } else { // String
        this.componentName = char.class.getSimpleName();
        this.arrayAccessor = null;

        // we only support Char Arrays
        if (dt == null) {
          this.datatype = DataType.UNICODESTRING;
          this.componentCodec = codecFactory.createDatatype(char.class);
        } else if (dt.getDataType().getValue().equals(OmtMimConstants.HLAUNICODECHAR)) {
          this.datatype = DataType.UNICODESTRING;
          this.componentCodec = codecFactory.createDatatype(char.class);
        } else if (dt.getDataType().getValue().equals(OmtMimConstants.HLAASCIICHAR)) {
          this.datatype = DataType.ASCIISTRING;
          this.componentCodec = codecFactory.createDatatype(byte.class);
        } else {
          throw new InvalidType(
              "Expected datatype HLAunicodeChar or HLAASCIIchar, but got "
                  + dt.getDataType().getValue());
        }
      }
    }

    /** Set the remaining properties */
    if (dt != null) {
      this.isDynamic = dt.getCardinality().getValue().equals(OmtMimConstants.DYNAMIC);
      int v[] = geCardinality(dt);
      this.minCardinality = v[0];
      this.maxCardinality = v[1];
    } else {
      this.isDynamic = true;
      this.minCardinality = 0;
      this.maxCardinality = Integer.MAX_VALUE;
    }

    /** The octetBoundary is the max of the array size and the component boundary */
    this.octetBoundary = Math.max(4, this.componentCodec.getOctetBoundary());
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
    return this.octetBoundary;
  }

  @Override
  public final int getEncodedLength(int position, Object value) throws OOcodecException {
    switch (this.datatype) {
      default:
      case ARRAY:
        // add length of the size element
        position += 4;

        int cardinality = arrayAccessor.length(value);
        if (cardinality > 0) {
          // /... add padding for the size element
          position += this.paddingSize(4, this.octetBoundary);

          for (int i = 0; i < cardinality - 1; i++) {
            int offset = position;

            // ... add size of component i
            position = componentCodec.getEncodedLength(position, arrayAccessor.get(value, i));

            // ... add padding for the component
            position += this.paddingSize(position - offset, componentCodec.getOctetBoundary());
          }

          // ... and add the size of the last element
          position =
              componentCodec.getEncodedLength(position, arrayAccessor.get(value, cardinality - 1));
        }

        return position;

      case UNICODESTRING:
        return position + 4 + ((String) value).length() * 2;

      case ASCIISTRING:
        return position + 4 + ((String) value).length();
    }
  }

  @Override
  public final void encode(ByteArrayWrapper byteWrapper, Object value) throws OOcodecException {
    switch (this.datatype) {
      default:
      case ARRAY:
        {
          int length = arrayAccessor.length(value);

          if (!this.isDynamic) {
            // check that length is within defined cardinality
            if (length < this.minCardinality || length > this.maxCardinality) {
              throw new InvalidValue(
                  "Array "
                      + this.componentName
                      + ": "
                      + length
                      + " outside range ["
                      + this.minCardinality
                      + ","
                      + this.maxCardinality
                      + "]");
            }
          }

          // add cardinality
          byteWrapper.putInt(length);

          // add elements
          if (length > 0) {
            // add padding
            byteWrapper.putPadding(this.paddingSize(4, this.octetBoundary));

            for (int i = 0; i < length - 1; i++) {
              // offset of record within byteWrapper
              int offset = byteWrapper.getPos();

              // encode i-th element
              componentCodec.encode(byteWrapper, arrayAccessor.get(value, i));

              // add padding
              byteWrapper.putPadding(
                  this.paddingSize(
                      byteWrapper.getPos() - offset, componentCodec.getOctetBoundary()));
            }

            // encode last element
            componentCodec.encode(byteWrapper, arrayAccessor.get(value, length - 1));
          }

          break;
        }

      case UNICODESTRING:
        {
          String unicodeString = (String) value;
          int length = unicodeString.length();

          if (!this.isDynamic) {
            // check that length is within defined cardinality
            if (length < this.minCardinality || length > this.maxCardinality) {
              throw new InvalidValue(
                  "Array "
                      + this.componentName
                      + ": "
                      + length
                      + " outside range ["
                      + this.minCardinality
                      + ","
                      + this.maxCardinality
                      + "]");
            }
          }

          byteWrapper.putInt(length);
          byteWrapper.putUnicodeString(unicodeString);
          break;
        }

      case ASCIISTRING:
        {
          String asciiString = (String) value;
          int length = asciiString.length();

          if (!this.isDynamic) {
            // check that length is within defined cardinality
            if (length < this.minCardinality || length > this.maxCardinality) {
              throw new InvalidValue(
                  "Array "
                      + this.componentName
                      + ": "
                      + length
                      + " outside range ["
                      + this.minCardinality
                      + ","
                      + this.maxCardinality
                      + "]");
            }
          }

          byteWrapper.putInt(length);
          byteWrapper.putAsciiString(asciiString);
          break;
        }
    }
  }

  @Override
  public final Object decode(ByteArrayWrapper byteWrapper, Object value, Object outer)
      throws OOcodecException {

    // get the size
    int cardinality = byteWrapper.getInt();

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

    switch (this.datatype) {
      default:
      case ARRAY:
        {
          // create new Java Class instance if none provided or if lenght is different
          if (value == null || cardinality != arrayAccessor.length(value)) {
            value = arrayAccessor.newInstance(cardinality);
          }

          if (cardinality > 0) {
            // consume padding
            byteWrapper.advance(this.paddingSize(4, this.octetBoundary));

            // NOTE: access array from index 0 onwards to work properly for List type
            for (int i = 0; i < cardinality - 1; i++) {
              // offset of record within byteWrapper
              int offset = byteWrapper.getPos();

              // get the existing i-th element
              Object vi = arrayAccessor.get(value, i);

              // decode the i-th element
              vi = componentCodec.decode(byteWrapper, vi, outer);

              // set the i-th element
              arrayAccessor.set(value, i, vi);

              // consume padding
              byteWrapper.advance(
                  this.paddingSize(
                      byteWrapper.getPos() - offset, componentCodec.getOctetBoundary()));
            }

            // the last element
            Object vi = arrayAccessor.get(value, cardinality - 1);
            vi = componentCodec.decode(byteWrapper, vi, outer);
            arrayAccessor.set(value, cardinality - 1, vi);
          }

          return value;
        }

      case UNICODESTRING:
        return byteWrapper.getUnicodeString(cardinality);

      case ASCIISTRING:
        return byteWrapper.getAsciiString(cardinality);
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
        .append(dt == null ? "null" : '"' + dt.getName().getValue() + '"')
        .append(",")
        .append("\"cardinality\" : ")
        .append(dt == null ? "null" : '"' + dt.getCardinality().getValue() + '"')
        .append(",")
        .append("\"component\" : ")
        .append('"')
        .append(this.componentCodec.toString())
        .append('"')
        .append(",")
        .append("\"encoding\" : ")
        .append(dt == null ? "null" : '"' + dt.getEncoding().getValue() + '"')
        .append(",")
        .append("\"octetBoundary\" : ")
        .append(octetBoundary)
        .append("}")
        .toString();
  }
}
