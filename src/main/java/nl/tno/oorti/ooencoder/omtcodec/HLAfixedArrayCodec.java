package nl.tno.oorti.ooencoder.omtcodec;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;
import nl.tno.oorti.accessor.ArrayAccessor;
import nl.tno.omt.ArrayDataTypesType.ArrayData;
import nl.tno.omt.helpers.OmtMimConstants;
import nl.tno.oorti.ooencoder.exceptions.InvalidClassStructure;
import nl.tno.oorti.ooencoder.exceptions.InvalidType;
import nl.tno.oorti.ooencoder.exceptions.InvalidValue;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * @author bergtwvd
 */
class HLAfixedArrayCodec extends HLAdataElementCodec implements OmtDatatypeCodec {

  enum DataType {
    ARRAY,
    UNICODESTRING,
    ASCIISTRING,
    UUID
  };

  final Type type;
  final ArrayData dt;
  final DataType datatype;
  final OmtDatatypeCodec componentCodec;
  final String componentName;
  final ArrayAccessor arrayAccessor;
  final int cardinality;
  final int octetBoundary;

  HLAfixedArrayCodec(OmtCodecFactory codecFactory, Type type) throws OOcodecException {
    this(codecFactory, type, null);
  }

  HLAfixedArrayCodec(OmtCodecFactory codecFactory, Type type, ArrayData dt)
      throws OOcodecException {

    if (type == null) {
      throw new InvalidClassStructure("Missing Type");
    }

    if (dt != null) {
      if (!dt.getEncoding().getValue().equals(OmtMimConstants.HLAFIXEDARRAY)) {
        throw new InvalidType(
            "Expected encoding HLAfixedArray, but got " + dt.getEncoding().getValue());
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

      /** Set the properties */
      this.datatype = DataType.ARRAY;

      // get the component type and name
      Type componentType = pt.getActualTypeArguments()[0];
      String componentDataTypeName = (dt == null) ? null : dt.getDataType().getValue();

      // create accessor the array component type
      this.arrayAccessor = ArrayAccessor.create(List.class);

      // create codec for the array component type
      this.componentCodec = codecFactory.createDatatype(componentType, componentDataTypeName);
      this.octetBoundary = this.componentCodec.getOctetBoundary();

      this.componentName = type.getTypeName();

      try {
        this.cardinality = (dt == null) ? 0 : Integer.parseInt(dt.getCardinality().getValue());
      } catch (NumberFormatException ex) {
        throw new InvalidType(ex.getMessage(), ex);
      }
    } else {
      Class clazz = (Class) type;

      if (!clazz.isArray() && !clazz.equals(String.class) && !clazz.equals(UUID.class)) {
        throw new InvalidClassStructure(
            "Expected Array, String or UUID class, but got " + clazz.getSimpleName());
      }

      /** Set the properties */
      Class componentClazz;
      String componentDataTypeName;

      if (clazz.isArray()) {
        this.datatype = DataType.ARRAY;
        componentClazz = clazz.getComponentType();
        componentDataTypeName = (dt == null) ? null : dt.getDataType().getValue();

        // create accessor for the array component
        this.arrayAccessor = ArrayAccessor.create(componentClazz);

        try {
          this.cardinality = (dt == null) ? 0 : Integer.parseInt(dt.getCardinality().getValue());
        } catch (NumberFormatException ex) {
          throw new InvalidType(ex.getMessage(), ex);
        }
      } else if (clazz.equals(UUID.class)) {
        if (dt == null || dt.getDataType().getValue().equals(OmtMimConstants.HLABYTE)) {
          this.datatype = DataType.UUID;
          componentClazz = byte.class;
          componentDataTypeName = OmtMimConstants.HLABYTE;

          // no accessor for UUID
          this.arrayAccessor = null;

          // check that cardinality matches the UUID
          try {
            this.cardinality = (dt == null) ? 16 : Integer.parseInt(dt.getCardinality().getValue());
          } catch (NumberFormatException ex) {
            throw new InvalidType(ex.getMessage(), ex);
          }

          if (this.cardinality != 16) {
            throw new InvalidType(
                "Expected array cardinality 16 for UUID, but got " + this.cardinality);
          }
        } else {
          throw new InvalidType(
              "Expected datatype HLAbyte, but got " + dt.getDataType().getValue());
        }
      } else { // String
        if (dt == null || dt.getDataType().getValue().equals(OmtMimConstants.HLAUNICODECHAR)) {
          this.datatype = DataType.UNICODESTRING;
          componentClazz = char.class;
          componentDataTypeName = OmtMimConstants.HLAUNICODECHAR;
        } else if (dt.getDataType().getValue().equals(OmtMimConstants.HLAASCIICHAR)) {
          this.datatype = DataType.ASCIISTRING;
          componentClazz = byte.class;
          componentDataTypeName = OmtMimConstants.HLAASCIICHAR;
        } else {
          throw new InvalidType(
              "Expected datatype HLAunicodeChar or HLAASCIIchar, but got "
                  + dt.getDataType().getValue());
        }

        // no accessor for String
        this.arrayAccessor = null;

        try {
          this.cardinality = (dt == null) ? 0 : Integer.parseInt(dt.getCardinality().getValue());
        } catch (NumberFormatException ex) {
          throw new InvalidType(ex.getMessage(), ex);
        }
      }

      // create codec for the array component type
      this.componentCodec = codecFactory.createDatatype(componentClazz, componentDataTypeName);
      this.octetBoundary = this.componentCodec.getOctetBoundary();
      this.componentName = componentClazz.getSimpleName();
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
        // check that the cardinality matches
        if (arrayAccessor.length(value) != this.cardinality) {
          throw new InvalidValue(
              "Array "
                  + this.componentName
                  + ": cardinality "
                  + cardinality
                  + " defined but got "
                  + arrayAccessor.length(value));
        }

        if (this.cardinality > 0) {
          for (int i = 0; i < this.cardinality - 1; i++) {
            int offset = position;

            // add size of component i
            position = componentCodec.getEncodedLength(position, arrayAccessor.get(value, i));

            // add padding
            position += this.paddingSize(position - offset, this.octetBoundary);
          }

          // add size of last component
          position =
              componentCodec.getEncodedLength(
                  position, arrayAccessor.get(value, this.cardinality - 1));
        }

        return position;

      case UNICODESTRING:
        // the value must be a String
        String unicodeString = (String) value;

        // check that the cardinality matches
        if (unicodeString.length() != this.cardinality) {
          throw new InvalidValue(
              "Array of char: cardinality "
                  + this.cardinality
                  + " defined but got "
                  + unicodeString.length());
        }

        return position + this.cardinality * 2;

      case ASCIISTRING:
        // the value must be a String
        String asciiString = (String) value;

        // check that the cardinality matches
        if (asciiString.length() != this.cardinality) {
          throw new InvalidValue(
              "Array of char: cardinality "
                  + this.cardinality
                  + " defined but got "
                  + asciiString.length());
        }

        return position + this.cardinality;

      case UUID:
        // the value must be a UUID
        return position + 16;
    }
  }

  @Override
  public final void encode(ByteArrayWrapper byteWrapper, Object value) throws OOcodecException {
    switch (this.datatype) {
      default:
      case ARRAY:
        // check that the cardinality matches
        if (arrayAccessor.length(value) != this.cardinality) {
          throw new InvalidValue(
              "Array "
                  + this.componentName
                  + ": cardinality "
                  + cardinality
                  + " defined but got "
                  + arrayAccessor.length(value));
        }

        // encode elements
        if (this.cardinality > 0) {
          for (int i = 0; i < this.cardinality - 1; i++) {
            // record current position
            int offset = byteWrapper.getPos();

            // encode element i
            componentCodec.encode(byteWrapper, arrayAccessor.get(value, i));

            // add padding
            byteWrapper.putPadding(
                this.paddingSize(byteWrapper.getPos() - offset, this.octetBoundary));
          }

          // encode last element
          componentCodec.encode(byteWrapper, arrayAccessor.get(value, this.cardinality - 1));
        }

        break;

      case UNICODESTRING:
        String unicodeString = (String) value;

        // check that the cardinality matches
        if (unicodeString.length() != this.cardinality) {
          throw new InvalidValue(
              "Array of char: cardinality "
                  + this.cardinality
                  + " defined but got "
                  + unicodeString.length());
        }

        byteWrapper.putUnicodeString(unicodeString);
        break;

      case ASCIISTRING:
        // the value must be a String
        String asciiString = (String) value;

        // check that the cardinality matches
        if (asciiString.length() != this.cardinality) {
          throw new InvalidValue(
              "Array of char: cardinality "
                  + this.cardinality
                  + " defined but got "
                  + asciiString.length());
        }

        byteWrapper.putAsciiString(asciiString);
        break;

      case UUID:
        byteWrapper.putUuid((UUID) value);
        break;
    }
  }

  @Override
  public final Object decode(ByteArrayWrapper byteWrapper, Object value, Object outer)
      throws OOcodecException {
    switch (this.datatype) {
      default:
      case ARRAY:
        // create new Java Class instance if none provided or if lenght is different
        if (value == null || this.cardinality != arrayAccessor.length(value)) {
          value = arrayAccessor.newInstance(this.cardinality);
        }

        if (this.cardinality > 0) {
          for (int i = 0; i < this.cardinality - 1; i++) {
            // record current position
            int offset = byteWrapper.getPos();

            // decode the i-th element
            Object vi = componentCodec.decode(byteWrapper, arrayAccessor.get(value, i), outer);

            // set the i-th element
            arrayAccessor.set(value, i, vi);

            // consume padding
            byteWrapper.advance(
                this.paddingSize(byteWrapper.getPos() - offset, this.octetBoundary));
          }

          // decode the last element
          Object vi =
              componentCodec.decode(
                  byteWrapper, arrayAccessor.get(value, this.cardinality - 1), outer);

          // set the last element
          arrayAccessor.set(value, this.cardinality - 1, vi);
        }

        return value;

      case UNICODESTRING:
        return byteWrapper.getUnicodeString(this.cardinality);

      case ASCIISTRING:
        return byteWrapper.getAsciiString(this.cardinality);

      case UUID:
        return byteWrapper.getUuid();
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
        .append(this.cardinality)
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
        .append(this.octetBoundary)
        .append("}")
        .toString();
  }
}
