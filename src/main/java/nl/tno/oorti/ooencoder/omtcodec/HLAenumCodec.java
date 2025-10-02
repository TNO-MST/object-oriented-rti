package nl.tno.oorti.ooencoder.omtcodec;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import nl.tno.omt.EnumeratedDataType.Enumerator;
import nl.tno.omt.EnumeratedDataTypesType.EnumeratedData;
import nl.tno.omt.HLAString;
import nl.tno.omt.helpers.OmtJavaMapping;
import nl.tno.omt.helpers.OmtMimConstants;
import nl.tno.oorti.accessor.ArrayAccessor;
import nl.tno.oorti.accessor.Getter;
import nl.tno.oorti.ooencoder.exceptions.InvalidClassStructure;
import nl.tno.oorti.ooencoder.exceptions.InvalidType;
import nl.tno.oorti.ooencoder.exceptions.InvalidValue;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * @author bergtwvd
 */
class HLAenumCodec implements OmtDatatypeCodec {

  static final String HLA_UNKNOWN_ENUM = "HLAunknown";

  enum DataType {
    ENUM,
    BOOLEAN
  };

  final Type type;
  final EnumeratedData dt;
  final DataType datatype;
  final Getter getter;
  final OmtBasicDatatypeCodec codec;
  final Map<Object, Object> value2constant;
  final int octetBoundary;
  final Object unknownEnum;

  HLAenumCodec(OmtCodecFactory codecFactory, Type type, EnumeratedData dt) throws OOcodecException {

    if (type == null) {
      throw new InvalidClassStructure("Missing Type");
    }

    if (dt == null) {
      throw new InvalidType("Missing OMT datatype");
    }

    this.type = type;
    this.dt = dt;

    Class clazz = (Class) type;

    // The type must be either a Boolean type or an Enum type.

    if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
      // if enumerated data is provided then ensure the datatype name is HLABOOLEAN
      if (!dt.getName().getValue().equals(OmtMimConstants.HLABOOLEAN)) {
        throw new InvalidType("Expected boolean, but got " + clazz.getSimpleName());
      }

      this.datatype = DataType.BOOLEAN;
      this.getter = null;

      // we know by definition that HLABOOLEAN is represented as an integer
      this.codec = codecFactory.createBasicDataTypeCodec(int.class, OmtMimConstants.HLAINTEGER32BE);

      this.octetBoundary = this.codec.getOctetBoundary();
      this.value2constant = null;
      this.unknownEnum = null;
    } else if (clazz.isEnum()) {
      // Enumerated data must be provided. Ensure that the datatype name matches.
      if (!clazz.getSimpleName().equals(dt.getName().getValue())) {
        throw new InvalidType(
            "Expected enum " + dt.getName().getValue() + ", but got " + clazz.getSimpleName());
      }

      this.datatype = DataType.ENUM;
      this.value2constant = new HashMap<>();

      // create a getter for getting the first value of an enumerator
      try {
        this.getter = codecFactory.getAccessorFactory().createGetter(clazz, "getValue");
      } catch (ReflectiveOperationException ex) {
        throw new InvalidClassStructure(ex.getMessage(), ex);
      }

      // create a codec for encoding/decoding the enumerator value
      this.codec =
          codecFactory.createBasicDataTypeCodec(
              getter.getType(), dt.getRepresentation().getValue());
      this.octetBoundary = this.codec.getOctetBoundary();

      // create a getter for getting all values of an enumerator
      final Getter arrayGetter;
      try {
        arrayGetter = codecFactory.getAccessorFactory().createGetter(clazz, "getValues");
      } catch (ReflectiveOperationException ex) {
        throw new InvalidClassStructure(ex.getMessage(), ex);
      }
      ArrayAccessor accessor = ArrayAccessor.create(arrayGetter.getType().componentType());

      // the HLA_UNKNOWN_ENUM enumerator, if present, will be stored here
      Object unknownEnumerator = null;

      // for each constant in the Java Enum do the following
      for (Object constant : clazz.getEnumConstants()) {
        String enumeratorName = OmtJavaMapping.toOmtName(constant.toString());
        if (enumeratorName.equals(HLA_UNKNOWN_ENUM)) {
          unknownEnumerator = constant;
          continue;
        }

        Enumerator enumerator = this.getEnumerator(enumeratorName);
        if (enumerator == null) {
          throw new InvalidClassStructure("Enum constant " + enumeratorName + " not defined");
        }

        Object values;
        try {
          values = arrayGetter.get(constant);
        } catch (ReflectiveOperationException ex) {
          throw new InvalidClassStructure(ex.getMessage(), ex);
        }

        // create a mapping from constant value to the associated Enum constant for later lookup
        for (int i = 0; i < accessor.length(values); i++) {
          Object value = accessor.get(values, i);

          if (!this.isValidEnumeratorValue(enumerator, value)) {
            throw new InvalidClassStructure(
                "Enum constant "
                    + enumeratorName
                    + " with value "
                    + value.toString()
                    + " not defined");
          }

          value2constant.put(value, constant);
        }
      }

      // NOTE that we do not check that all FOM enum values are defined in the Java Class;
      // there may be many and we capture this situation when the lookup fails an enum value is
      // received

      // NOTE that the unknownEnum is null if there is no HLA_UNKNOWN_ENUM
      // defined in the enumeration
      this.unknownEnum = unknownEnumerator;
    } else {
      throw new InvalidClassStructure(
          "Expected enum "
              + dt.getName().getValue()
              + " or boolean, but got "
              + clazz.getSimpleName());
    }
  }

  private Enumerator getEnumerator(String enumeratorName) {
    for (Enumerator enumerator : dt.getEnumerator()) {
      if (enumerator.getName().getValue().equals(enumeratorName)) {
        return enumerator;
      }
    }

    return null;
  }

  private boolean isValidEnumeratorValue(Enumerator enumerator, Object value) {
    String valueString = this.codec.toString(value);
    for (HLAString s : enumerator.getValue()) {
      if (s.getValue().equals(valueString)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public final int getOctetBoundary() {
    return this.octetBoundary;
  }

  @Override
  public final int getEncodedLength(int position, Object value) throws OOcodecException {
    if (value == null) {
      throw new InvalidValue("Invalid null value for enumerator");
    }

    // not allowed to encode the HLA_UNKNOWN_ENUM
    if (value == this.unknownEnum) {
      throw new InvalidValue("Invalid enumerator " + HLA_UNKNOWN_ENUM);
    }

    switch (this.datatype) {
      default:
      case ENUM:
        {
          try {
            // take the first value of the enumerator
            return this.codec.getEncodedLength(position, getter.get(value));
          } catch (ReflectiveOperationException ex) {
            throw new InvalidClassStructure(ex.getMessage(), ex);
          }
        }

      case BOOLEAN:
        return this.codec.getEncodedLength(position, ((Boolean) value) ? (int) 1 : (int) 0);
    }
  }

  @Override
  public final void encode(ByteArrayWrapper byteWrapper, Object value) throws OOcodecException {
    if (value == null) {
      throw new InvalidValue("Invalid null value for enumerator");
    }

    if (value == this.unknownEnum) {
      throw new InvalidValue("Invalid enumerator " + HLA_UNKNOWN_ENUM);
    }

    switch (this.datatype) {
      default:
      case ENUM:
        {
          try {
            // take the first value of the enumerator
            codec.encode(byteWrapper, getter.get(value));
          } catch (ReflectiveOperationException ex) {
            throw new InvalidClassStructure(ex.getMessage(), ex);
          }
        }
        break;

      case BOOLEAN:
        codec.encode(byteWrapper, ((Boolean) value) ? (int) 1 : (int) 0);
        break;
    }
  }

  @Override
  public final Object decode(ByteArrayWrapper byteWrapper, Object value, Object outer)
      throws OOcodecException {
    switch (this.datatype) {
      default:
      case ENUM:
        // lookup the associated enum constant
        Object value2 = codec.decode(byteWrapper, null, null);
        Object constant = value2constant.get(value2);

        // return unknown if constant is not known
        if (constant == null) {
          if (this.unknownEnum == null) {
            throw new InvalidValue("No enumerator found for value " + value2.toString());
          } else return this.unknownEnum;
        } else return constant;
      case BOOLEAN:
        return ((int) codec.decode(byteWrapper, null, null) != (int) 0);
    }
  }

  @Override
  public final String toString() {
    return new StringBuilder()
        .append("{")
        .append("\"type\" : ")
        .append('"')
        .append(this.type.getTypeName())
        .append('"')
        .append(",")
        .append("\"name\" : ")
        .append('"')
        .append(dt.getName().getValue())
        .append('"')
        .append(",")
        .append("\"representation\" : ")
        .append('"')
        .append(dt.getRepresentation().getValue())
        .append('"')
        .append(",")
        .append("\"octetBoundary\" : ")
        .append(octetBoundary)
        .append("}")
        .toString();
  }
}
