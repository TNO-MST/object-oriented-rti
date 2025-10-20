package nl.tno.oorti.ooencoder.omtcodec;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import nl.tno.omt.EnumeratedDataType.Enumerator;
import nl.tno.omt.EnumeratedDataTypesType.EnumeratedData;
import nl.tno.omt.VariantRecordDataType.Alternative;
import nl.tno.omt.VariantRecordDataTypesType.VariantRecordData;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.omt.helpers.OmtJavaMapping;
import nl.tno.oorti.EnumFunctions;
import nl.tno.oorti.accessor.Accessor;
import nl.tno.oorti.accessor.ClassUtils;
import nl.tno.oorti.ooencoder.exceptions.InvalidClassStructure;
import nl.tno.oorti.ooencoder.exceptions.InvalidType;
import nl.tno.oorti.ooencoder.exceptions.InvalidValue;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * @author bergtwvd
 */
class HLAvariantRecordCodec extends HLAdataElementCodec implements OmtDatatypeCodec {

  static final String HLA_OTHER_ENUM = "HLAother";
  static final String HLA_UNKNOWN_ENUM = "HLAunknown";

  class AccessorCodec {

    Accessor accessor;
    OmtDatatypeCodec codec;

    AccessorCodec(Accessor accessor, OmtDatatypeCodec codec) {
      this.accessor = accessor;
      this.codec = codec;
    }
  }

  final Class clazz;
  final VariantRecordData dt;
  final Constructor enclosingCtor;
  final Accessor discriminantAccessor;
  final OmtDatatypeCodec discriminantCodec;
  final Object hlaUnknownEnum;
  final Map<Object, AccessorCodec> accessorCodecMap;
  final AccessorCodec hlaOtherAccessorCodec;
  final int maxVariantOctetBoundary;
  final int octetBoundary;

  /**
   * This constructor defines an addition argument that indicates what record encoding shall be
   * used. If true, RPRextendableRecord encoding is used, otherwise HLAvariantRecord.
   *
   * @param codecFactory
   * @param type
   * @param dt
   * @throws OOcodecException
   */
  HLAvariantRecordCodec(OmtCodecFactory codecFactory, Type type, VariantRecordData dt)
      throws OOcodecException {
    if (type == null) {
      throw new InvalidClassStructure("Missing Type");
    }

    if (dt == null) {
      throw new InvalidType("Missing OMT datatype");
    }

    this.clazz = (Class) type;
    this.dt = dt;

    if (!this.clazz.getSimpleName().equals(dt.getName().getValue())) {
      throw new InvalidType(
          "Expected class " + dt.getName().getValue() + ", but got " + this.clazz.getSimpleName());
    }

    try {
      // set the enclosing constructor, if any
      this.enclosingCtor =
          (this.clazz.getEnclosingClass() == null)
              ? null
              : this.clazz.getConstructor(this.clazz.getEnclosingClass());
    } catch (NoSuchMethodException | SecurityException ex) {
      throw new InvalidClassStructure(ex.getMessage(), ex);
    }

    // create map of accessor/codecs; there will be one for each member field
    this.accessorCodecMap = new HashMap<>();

    // get discriminant field and type
    String discriminantName = OmtJavaMapping.toJavaName(dt.getDiscriminant().getValue());
    Field discriminantField = ClassUtils.getField(this.clazz, discriminantName);
    if (discriminantField == null) {
      throw new InvalidClassStructure(
          "Unknown field " + discriminantField + " for class " + clazz.getSimpleName());
    }

    Class discriminantClazz = discriminantField.getType();

    this.hlaUnknownEnum = getEnumValue(discriminantClazz, HLA_UNKNOWN_ENUM);

    try {
      // create an accessor and codec for the discriminant
      this.discriminantAccessor =
          codecFactory.getAccessorFactory().createAccessor(discriminantField);
    } catch (ReflectiveOperationException ex) {
      throw new InvalidClassStructure(ex.getMessage(), ex);
    }
    this.discriminantCodec =
        codecFactory.createDatatype(discriminantClazz, dt.getDataType().getValue());

    // get the enumerated data of the enum datatype
    EnumeratedData enumData =
        OmtFunctions.getEnumeratedDataByName(
            codecFactory.getModules(), dt.getDataType().getValue());

    // Store the AccessorCodec for the HLAother alternative here while running through the list of
    // alternatives
    AccessorCodec tmpHlaOtherAccessCodec = null;

    // loop over all of the alternatives
    for (Alternative alternative : dt.getAlternative()) {
      // create an accessor for this alternative
      String alternativeName = OmtJavaMapping.toJavaName(alternative.getName().getValue());
      Field field = ClassUtils.getField(this.clazz, alternativeName);
      if (field == null) {
        throw new InvalidClassStructure(
            "Unknown field " + alternativeName + " for class " + clazz.getSimpleName());
      }

      Accessor accessor;
      try {
        accessor = codecFactory.getAccessorFactory().createAccessor(field);
      } catch (ReflectiveOperationException ex) {
        throw new InvalidClassStructure(ex.getMessage(), ex);
      }

      // create a codec for this alternative
      OmtDatatypeCodec codec =
          codecFactory.createDatatype(field.getGenericType(), alternative.getDataType().getValue());

      AccessorCodec accessorCodec = new AccessorCodec(accessor, codec);

      // For this alternative, add for each enumerator the accessorCodec to the lookup map.
      // The format for enumerator values is: 'HLAother' | <name> | '[' <name> .. <name> ']' |
      // <name> { ',' <name> }.
      // The range requires a lookup in the enumeration part of the FOM.
      String enumerator = alternative.getEnumerator().getValue().trim();

      if (enumerator.equals(HLA_OTHER_ENUM)) {
        tmpHlaOtherAccessCodec = accessorCodec;
      } else if (enumerator.startsWith("[")) {
        if (!enumerator.endsWith("]")) {
          throw new InvalidType(
              "Ill defined enumerator value "
                  + enumerator
                  + " in alternative "
                  + alternative.getName().getValue());
        }

        String enumeratorNameRange[] =
            enumerator.substring(1, enumerator.length() - 1).split("\\.\\.");
        if (enumeratorNameRange.length != 2) {
          throw new InvalidType(
              "Ill defined enumerator value "
                  + enumerator
                  + " in alternative "
                  + alternative.getName().getValue());
        }

        enumeratorNameRange[0] = enumeratorNameRange[0].trim();
        enumeratorNameRange[1] = enumeratorNameRange[1].trim();

        Iterator<Enumerator> it = enumData.getEnumerator().iterator();
        while (it.hasNext()) {
          Enumerator entry = it.next();
          if (entry.getName().getValue().equals(enumeratorNameRange[0])) {
            // found the start
            try {
              Object enumeratorValue =
                  EnumFunctions.valueOf(
                      discriminantClazz,
                      OmtJavaMapping.toJavaName(entry.getName().getValue().trim()));
              this.accessorCodecMap.put(enumeratorValue, accessorCodec);
            } catch (IllegalArgumentException ex) {
              throw new InvalidClassStructure(ex.getMessage(), ex);
            }

            // while not at the end process the rest
            while (!entry.getName().getValue().equals(enumeratorNameRange[1]) && it.hasNext()) {
              entry = it.next();
              try {
                Object enumeratorValue =
                    EnumFunctions.valueOf(
                        discriminantClazz,
                        OmtJavaMapping.toJavaName(entry.getName().getValue().trim()));
                this.accessorCodecMap.put(enumeratorValue, accessorCodec);
              } catch (IllegalArgumentException ex) {
                throw new InvalidClassStructure(ex.getMessage(), ex);
              }
            }

            // break out of the loop, since we are done with the range
            break;
          }
        }
      } else {
        String enumeratorNames[] = alternative.getEnumerator().getValue().split(",");
        for (String enumeratorName : enumeratorNames) {
          try {
            Object enumeratorValue =
                EnumFunctions.valueOf(
                    discriminantClazz, OmtJavaMapping.toJavaName(enumeratorName.trim()));
            this.accessorCodecMap.put(enumeratorValue, accessorCodec);
          } catch (IllegalArgumentException ex) {
            throw new InvalidClassStructure(ex.getMessage(), ex);
          }
        }
      }
    }

    this.hlaOtherAccessorCodec = tmpHlaOtherAccessCodec;

    /** Calculate octet boundary */
    int maxOctetBoundary = 1;
    for (AccessorCodec variant : this.accessorCodecMap.values()) {
      maxOctetBoundary = Math.max(maxOctetBoundary, variant.codec.getOctetBoundary());
    }

    this.maxVariantOctetBoundary = maxOctetBoundary;
    this.octetBoundary =
        Math.max(this.maxVariantOctetBoundary, discriminantCodec.getOctetBoundary());
  }

  private Object getEnumValue(Class clazz, String name) {
    try {
      return EnumFunctions.valueOf(clazz, name);
    } catch (IllegalArgumentException ex) {
      return null;
    }
  }

  @Override
  public int getOctetBoundary() {
    return this.octetBoundary;
  }

  @Override
  public int getEncodedLength(int position, Object value) throws OOcodecException {
    try {
      // save start position
      int offset = position;

      Object discriminant = this.discriminantAccessor.get(value);

      // add the encoded length of the discriminant
      position = this.discriminantCodec.getEncodedLength(position, discriminant);

      AccessorCodec accessorCodec = this.accessorCodecMap.get(discriminant);
      if (accessorCodec != null) {
        // ... plus any padding that comes after the discriminant
        position += this.paddingSize(position - offset, this.maxVariantOctetBoundary);

        // ... plus the encoded length of the variant
        position =
            accessorCodec.codec.getEncodedLength(position, accessorCodec.accessor.get(value));
      } else {
        if (this.hlaOtherAccessorCodec == null || discriminant.equals(hlaUnknownEnum)) {
          throw new InvalidValue(
              "Unspecified enumerator for discriminant " + discriminant.toString());
        } else {
          // ... plus any padding that comes after the discriminant
          position += this.paddingSize(position - offset, this.maxVariantOctetBoundary);

          // ... plus the encoded length of the variant
          position =
              hlaOtherAccessorCodec.codec.getEncodedLength(
                  position, hlaOtherAccessorCodec.accessor.get(value));
        }
      }

      return position;
    } catch (ReflectiveOperationException ex) {
      throw new InvalidClassStructure(ex.getMessage(), ex);
    }
  }

  @Override
  public void encode(ByteArrayWrapper byteWrapper, Object value) throws OOcodecException {
    try {
      int offset = byteWrapper.getPos();
      Object discriminant = this.discriminantAccessor.get(value);
      this.discriminantCodec.encode(byteWrapper, discriminant);

      AccessorCodec accessorCodec = this.accessorCodecMap.get(discriminant);
      if (accessorCodec != null) {
        byteWrapper.putPadding(
            this.paddingSize(byteWrapper.getPos() - offset, this.maxVariantOctetBoundary));
        accessorCodec.codec.encode(byteWrapper, accessorCodec.accessor.get(value));
      } else {
        if (this.hlaOtherAccessorCodec == null || discriminant.equals(hlaUnknownEnum)) {
          throw new InvalidValue(
              "Unspecified enumerator for discriminant " + discriminant.toString());
        } else {
          byteWrapper.putPadding(
              this.paddingSize(byteWrapper.getPos() - offset, this.maxVariantOctetBoundary));
          hlaOtherAccessorCodec.codec.encode(
              byteWrapper, hlaOtherAccessorCodec.accessor.get(value));
        }
      }
    } catch (ReflectiveOperationException ex) {
      throw new InvalidClassStructure(ex.getMessage(), ex);
    }
  }

  @Override
  public Object decode(ByteArrayWrapper byteWrapper, Object value, Object outer)
      throws OOcodecException {
    try {
      // create new Java Class instance if none provided
      if (value == null) {
        value = createNewObect(this.clazz, outer, this.enclosingCtor);
      }

      int offset = byteWrapper.getPos();

      // get the discriminant value
      Object discriminant = this.discriminantCodec.decode(byteWrapper, null, null);

      // set the discriminant value in the Java Class instance
      this.discriminantAccessor.set(value, discriminant);

      // get the value for the selected variant, if present
      AccessorCodec accessorCodec = accessorCodecMap.get(discriminant);
      if (accessorCodec != null) {
        // consume padding
        byteWrapper.advance(
            this.paddingSize(byteWrapper.getPos() - offset, this.maxVariantOctetBoundary));

        accessorCodec.accessor.set(
            value,
            accessorCodec.codec.decode(byteWrapper, accessorCodec.accessor.get(value), value));
      } else {
        // if the discriminant is HLAunknown then throw an exception, otherwise use the HLAother
        // alternative if specified
        if (this.hlaOtherAccessorCodec == null || discriminant.equals(hlaUnknownEnum)) {
          throw new InvalidValue(
              "Unspecified enumerator for discriminant " + discriminant.toString());
        } else {
          // consume padding
          byteWrapper.advance(
              this.paddingSize(byteWrapper.getPos() - offset, this.maxVariantOctetBoundary));

          hlaOtherAccessorCodec.accessor.set(
              value,
              hlaOtherAccessorCodec.codec.decode(
                  byteWrapper, hlaOtherAccessorCodec.accessor.get(value), value));
        }
      }

      return value;
    } catch (ReflectiveOperationException ex) {
      throw new InvalidClassStructure(ex.getMessage(), ex);
    }
  }

  @Override
  public String toString() {
    StringBuilder variantsBuilder = new StringBuilder();
    for (Entry<Object, AccessorCodec> entry : this.accessorCodecMap.entrySet()) {
      if (!variantsBuilder.isEmpty()) {
        variantsBuilder.append(',');
      }

      variantsBuilder
          .append('[')
          .append('"')
          .append(EnumFunctions.name(entry.getKey()))
          .append('"')
          .append(',')
          .append(entry.getValue().codec.toString())
          .append(']');
    }

    return new StringBuilder()
        .append("{")
        .append("\"type\" : ")
        .append('"')
        .append(clazz.getTypeName())
        .append('"')
        .append(",")
        .append("\"name\" : ")
        .append('"')
        .append(dt.getName().getValue())
        .append('"')
        .append(",")
        .append("\"discriminant\" : ")
        .append(this.discriminantCodec.toString())
        .append(",")
        .append("\"variants\" : ")
        .append('[')
        .append(variantsBuilder.toString())
        .append(']')
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
}
