package nl.tno.oorti.ooencoder.omtcodec;

import java.lang.reflect.Type;
import nl.tno.omt.VariantRecordDataTypesType;
import nl.tno.oorti.ooencoder.exceptions.InvalidClassStructure;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * RPRextendedVariantRecordCodec is an extension of HLAvariantRecordCodec, using the
 * RPRextendedVariantRecord encoding.
 *
 * @author bergtwvd
 */
class RPRextendedVariantRecordCodec extends HLAvariantRecordCodec {

  RPRextendedVariantRecordCodec(
      OmtCodecFactory codecFactory, Type type, VariantRecordDataTypesType.VariantRecordData dt)
      throws OOcodecException {
    super(codecFactory, type, dt);
  }

  @Override
  public int getEncodedLength(int position, Object value) throws OOcodecException {
    try {
      Object discriminant = this.discriminantAccessor.get(value);

      // add the encoded length of the discriminant
      position = this.discriminantCodec.getEncodedLength(position, discriminant);

      // ... plus the extension length
      position += 4;

      AccessorCodec accessorCodec = this.accessorCodecMap.get(discriminant);
      if (accessorCodec != null) {
        // ... plus the encoded length of the variant
        position =
            accessorCodec.codec.getEncodedLength(position, accessorCodec.accessor.get(value));
      }
      // else the encoded length of the variant is assumed to be zero

      return position;
    } catch (ReflectiveOperationException ex) {
      throw new InvalidClassStructure(ex.getMessage(), ex);
    }
  }

  @Override
  public void encode(ByteArrayWrapper byteWrapper, Object value) throws OOcodecException {
    try {
      Object discriminant = this.discriminantAccessor.get(value);

      // encode the discriminant
      this.discriminantCodec.encode(byteWrapper, discriminant);

      // encode the variant if the discriminant exists ...
      AccessorCodec accessorCodec = accessorCodecMap.get(discriminant);
      if (accessorCodec != null) {
        // get the variant
        Object variant = accessorCodec.accessor.get(value);

        // encode the variant size
        int offset = byteWrapper.getPos();
        int variantLength = accessorCodec.codec.getEncodedLength(offset, variant) - offset;
        byteWrapper.putInt(variantLength);

        // encode the variant
        accessorCodec.codec.encode(byteWrapper, variant);
      } else {
        // assume zero variant size
        byteWrapper.putInt(0);
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

      // get the discriminant value
      Object discriminant = this.discriminantCodec.decode(byteWrapper, null, null);

      // set the discriminant value in the Java Class instance
      this.discriminantAccessor.set(value, discriminant);

      // get the variant size
      int variantLength = byteWrapper.getInt();

      AccessorCodec accessorCodec = accessorCodecMap.get(discriminant);
      if (accessorCodec != null) {
        // decode the variant
        accessorCodec.accessor.set(
            value,
            accessorCodec.codec.decode(byteWrapper, accessorCodec.accessor.get(value), value));
      } else {
        // consume variant
        byteWrapper.advance(variantLength);
      }

      return value;
    } catch (ReflectiveOperationException ex) {
      throw new InvalidClassStructure(ex.getMessage(), ex);
    }
  }
}
