package nl.tno.oorti.ooencoder.omtcodec;

import nl.tno.oorti.accessor.ClassUtils;
import nl.tno.oorti.accessor.Accessor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import nl.tno.omt.FixedRecordDataType;
import nl.tno.omt.FixedRecordDataTypesType.FixedRecordData;
import nl.tno.omt.helpers.OmtJavaMapping;
import nl.tno.oorti.ooencoder.exceptions.InvalidClassStructure;
import nl.tno.oorti.ooencoder.exceptions.InvalidType;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * @author bergtwvd
 */
class HLAfixedRecordCodec extends HLAdataElementCodec implements OmtDatatypeCodec {

  final Type type;
  final FixedRecordData dt;
  final Constructor enclosingCtor;
  final Accessor[] memberAccessors;
  final OmtDatatypeCodec[] memberFieldCodecs;
  final String[] memberNames; // for toString
  final OmtCodecFactory codecFactory;
  final int octetBoundary;

  HLAfixedRecordCodec(OmtCodecFactory codecFactory, Type type, FixedRecordData dt)
      throws OOcodecException {

    if (type == null) {
      throw new InvalidClassStructure("Missing Type");
    }

    if (dt == null) {
      throw new InvalidType("Missing OMT datatype");
    }

    this.type = type;
    this.dt = dt;

    Class clazz = (Class) type;

    if (!clazz.getSimpleName().equals(dt.getName().getValue())) {
      throw new InvalidType(
          "Expected class " + dt.getName().getValue() + ", but got " + clazz.getSimpleName());
    }

    /** Set properties */
    this.codecFactory = codecFactory;

    try {
      // set the enclosing constructor, if any
      this.enclosingCtor =
          (clazz.getEnclosingClass() == null)
              ? null
              : clazz.getConstructor(clazz.getEnclosingClass());
    } catch (NoSuchMethodException | SecurityException ex) {
      throw new InvalidClassStructure(ex.getMessage(), ex);
    }

    // get number of fields in record
    int sz = dt.getField().size();

    // member field accessors
    this.memberAccessors = new Accessor[sz];

    // member field codecs
    this.memberFieldCodecs = new OmtDatatypeCodec[sz];

    // member names
    this.memberNames = new String[sz];

    // create accessor and codec for each attribute
    List<FixedRecordDataType.Field> list = dt.getField();
    int index = 0;
    for (FixedRecordDataType.Field recordField : list) {
      String memberfieldName = OmtJavaMapping.toJavaName(recordField.getName().getValue());
      memberNames[index] = memberfieldName;

      // if this field has a padding datatype, then this field is a padding field and
      // the presence of the Java field is optional
      String dataTypeName = recordField.getDataType().getValue();
      if (this.codecFactory.hasPaddingEncoding(dataTypeName)) {
        // no accessor is used for a padding field
        // SET THE ACCESSOR FOR THIS FIELD TO NULL to indicate this
        memberAccessors[index] = null;

        // get the codec for the padding field; the Java Type is assumed to be an array of bytes
        Type paddingType = byte[].class;
        OmtDatatypeCodec codec = codecFactory.createDatatype(paddingType, dataTypeName);
        memberFieldCodecs[index] = codec;

        index++;

        // check if a padding field happens to be present
        Field field = ClassUtils.getField(clazz, memberfieldName);
        if (field == null) {
          // no field present; skip further checks
          continue;
        }

        // ... if so, check if the Java Types match
        if (!field.getGenericType().equals(paddingType)) {
          throw new InvalidClassStructure(
              "Type "
                  + field.getGenericType().getTypeName()
                  + " of field "
                  + memberfieldName
                  + " does not match with expected type "
                  + paddingType.getTypeName());
        }
      } else {
        // create an accessor for the member field
        Field field = ClassUtils.getField(clazz, memberfieldName);
        if (field == null) {
          throw new InvalidClassStructure(
              ("Unknown field " + memberfieldName + " for class " + clazz.getSimpleName()));
        }

        try {
          memberAccessors[index] = codecFactory.getAccessorFactory().createAccessor(field);
        } catch (ReflectiveOperationException ex) {
          throw new InvalidClassStructure(ex.getMessage(), ex);
        }

        // create a coded for the member field
        OmtDatatypeCodec codec = codecFactory.createDatatype(field.getGenericType(), dataTypeName);
        memberFieldCodecs[index] = codec;

        index++;
      }
    }

    /** Determine octetBounndary */
    int maxOctetBoundary = 1;
    for (OmtDatatypeCodec codec : this.memberFieldCodecs) {
      maxOctetBoundary = Math.max(maxOctetBoundary, codec.getOctetBoundary());
    }
    this.octetBoundary = maxOctetBoundary;
  }

  @Override
  public final int getOctetBoundary() {
    return this.octetBoundary;
  }

  @Override
  public final int getEncodedLength(int position, Object value) throws OOcodecException {
    try {
      if (memberAccessors.length > 0) {
        for (int i = 0; i < memberFieldCodecs.length - 1; i++) {
          // add size of component i
          OmtDatatypeCodec codec = memberFieldCodecs[i];
          Accessor accessor = this.memberAccessors[i];
          position =
              codec.getEncodedLength(position, accessor == null ? null : accessor.get(value));

          // add padding
          position += this.paddingSize(position, memberFieldCodecs[i + 1].getOctetBoundary());
        }

        // add size of last component
        OmtDatatypeCodec codec = memberFieldCodecs[memberFieldCodecs.length - 1];
        Accessor accessor = this.memberAccessors[memberFieldCodecs.length - 1];
        position = codec.getEncodedLength(position, accessor == null ? null : accessor.get(value));
      }

      return position;
    } catch (ReflectiveOperationException ex) {
      throw new InvalidClassStructure(ex.getMessage(), ex);
    }
  }

  @Override
  public final void encode(ByteArrayWrapper byteWrapper, Object value) throws OOcodecException {
    try {
      if (memberAccessors.length > 0) {
        for (int i = 0; i < memberAccessors.length - 1; i++) {
          OmtDatatypeCodec codec = memberFieldCodecs[i];
          Accessor accessor = memberAccessors[i];
          codec.encode(byteWrapper, accessor == null ? null : accessor.get(value));

          // add padding
          byteWrapper.putPadding(
              this.paddingSize(byteWrapper.getPos(), memberFieldCodecs[i + 1].getOctetBoundary()));
        }

        OmtDatatypeCodec codec = memberFieldCodecs[memberAccessors.length - 1];
        Accessor accessor = memberAccessors[memberAccessors.length - 1];
        codec.encode(byteWrapper, accessor == null ? null : accessor.get(value));
      }
    } catch (ReflectiveOperationException ex) {
      throw new InvalidClassStructure(ex.getMessage(), ex);
    }
  }

  @Override
  public final Object decode(ByteArrayWrapper byteWrapper, Object value, Object outer)
      throws OOcodecException {
    try {
      // create new Java Class instance if none provided
      if (value == null) {
        value = createNewObect((Class) this.type, outer, this.enclosingCtor);
      }

      if (memberAccessors.length > 0) {
        for (int i = 0; i < memberAccessors.length - 1; i++) {
          OmtDatatypeCodec codec = memberFieldCodecs[i];
          Accessor accessor = memberAccessors[i];
          if (accessor != null) {
            accessor.set(value, codec.decode(byteWrapper, accessor.get(value), value));
          }

          // consume padding
          int padding =
              this.paddingSize(byteWrapper.getPos(), memberFieldCodecs[i + 1].getOctetBoundary());
          byteWrapper.advance(padding);
        }

        OmtDatatypeCodec codec = memberFieldCodecs[memberAccessors.length - 1];
        Accessor accessor = memberAccessors[memberAccessors.length - 1];
        if (accessor != null) {
          accessor.set(value, codec.decode(byteWrapper, accessor.get(value), value));
        }
      }

      return value;
    } catch (ReflectiveOperationException ex) {
      throw new InvalidClassStructure(ex.getMessage(), ex);
    }
  }

  @Override
  public String toString() {
    StringBuilder fieldBuilder = new StringBuilder();
    for (int i = 0; i < this.memberNames.length; i++) {
      if (this.memberAccessors[i] == null) {
        continue;
      }

      if (!fieldBuilder.isEmpty()) {
        fieldBuilder.append(',');
      }

      fieldBuilder
          .append('[')
          .append('"')
          .append(this.memberNames[i])
          .append('"')
          .append(',')
          .append(this.memberFieldCodecs[i].toString())
          .append(']');
    }

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
        .append("\"fields\" : ")
        .append('[')
        .append(fieldBuilder.toString())
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
