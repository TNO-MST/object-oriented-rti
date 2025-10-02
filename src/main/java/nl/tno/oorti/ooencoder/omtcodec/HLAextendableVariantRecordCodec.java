package nl.tno.oorti.ooencoder.omtcodec;

import java.lang.reflect.Type;
import nl.tno.omt.VariantRecordDataTypesType.VariantRecordData;
import nl.tno.oorti.ooencoder.exceptions.InvalidClassStructure;
import nl.tno.oorti.ooencoder.exceptions.InvalidType;
import nl.tno.oorti.ooencoder.exceptions.InvalidValue;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * HLAextendableVariantRecordCodec is an extension of HLAvariantRecordCodec, using the
 * HLAextendableVariantRecord encoding.
 *
 * @author bergtwvd
 */
class HLAextendableVariantRecordCodec extends HLAvariantRecordCodec {

  static final int ENCODED_LENGTH_SIZE = 4;
  static final int VARIANT_OCTET_BOUNDARY = 8;

  HLAextendableVariantRecordCodec(OmtCodecFactory codecFactory, Type type, VariantRecordData dt)
      throws OOcodecException {
    super(codecFactory, type, dt);

    // ensure that the octetBoundary is not more than VARIANT_OCTET_BOUNDARY
    if (this.octetBoundary > VARIANT_OCTET_BOUNDARY) {
      throw new InvalidType(
          "Octectboundary "
              + this.octetBoundary
              + " in "
              + dt.getName().getValue()
              + " greater than maximum "
              + VARIANT_OCTET_BOUNDARY);
    }
  }

  @Override
  public int getEncodedLength(int position, Object value) throws OOcodecException {
    try {
      // save start position
      int offset = position;

      Object discriminant = this.discriminantAccessor.get(value);

      // add the encoded length of the discriminant
      position = this.discriminantCodec.getEncodedLength(position, discriminant);

      // ... plus any padding that comes after the discriminant
      position += this.paddingSize(position - offset, ENCODED_LENGTH_SIZE);

      // ... plus the ENCODED_LENGTH_SIZE bytes for the encoded length of a variant
      position += ENCODED_LENGTH_SIZE;

      AccessorCodec accessorCodec = this.accessorCodecMap.get(discriminant);
      if (accessorCodec != null) {
        // ... plus any padding that comes after the encoded length
        position += this.paddingSize(position - offset, VARIANT_OCTET_BOUNDARY);

        // ... plus the encoded length of the variant
        position =
            accessorCodec.codec.getEncodedLength(position, accessorCodec.accessor.get(value));
      } else {
        throw new InvalidValue(
            "Unspecified enumerator for discriminant " + discriminant.toString());
      }

      return position;
    } catch (ReflectiveOperationException ex) {
      throw new InvalidClassStructure(ex.getMessage(), ex);
    }
  }

  @Override
  public int getOctetBoundary() {
    return VARIANT_OCTET_BOUNDARY;
  }

  @Override
  public void encode(ByteArrayWrapper byteWrapper, Object value) throws OOcodecException {
    try {
      // save start position
      int offset = byteWrapper.getPos();

      Object discriminant = this.discriminantAccessor.get(value);

      // encode the discriminant
      this.discriminantCodec.encode(byteWrapper, discriminant);

      // add padding
      byteWrapper.putPadding(this.paddingSize(byteWrapper.getPos() - offset, ENCODED_LENGTH_SIZE));

      // encode the variant if the discriminant exists ...
      AccessorCodec accessorCodec = accessorCodecMap.get(discriminant);
      if (accessorCodec != null) {
        // determine and encode the variant length
        int variantPadding =
            this.paddingSize(
                byteWrapper.getPos() + ENCODED_LENGTH_SIZE - offset, VARIANT_OCTET_BOUNDARY);
        int offsetVariant = byteWrapper.getPos() + ENCODED_LENGTH_SIZE + variantPadding;

        // encode the variant length, followed by the padding, followed by the variant itself
        Object variant = accessorCodec.accessor.get(value);
        int variantLength =
            accessorCodec.codec.getEncodedLength(offsetVariant, variant) - offsetVariant;
        byteWrapper.putInt(variantLength);
        byteWrapper.putPadding(variantPadding);
        accessorCodec.codec.encode(byteWrapper, variant);
      } else {
        throw new InvalidValue(
            "Unspecified enumerator for discriminant " + discriminant.toString());
      }
    } catch (ReflectiveOperationException ex) {
      throw new InvalidClassStructure(ex.getMessage(), ex);
    }
  }

  @Override
  public Object decode(ByteArrayWrapper byteWrapper, Object value, Object outer)
      throws OOcodecException {
    try {
      // save start position
      int offset = byteWrapper.getPos();

      // create new Java object if none provided
      if (value == null) {
        value = createNewObect(this.clazz, outer, this.enclosingCtor);
      }

      // get the discriminant value, which may be HLAunknown
      // NOTE that the HLAunknown enumerator does not exist in the accessorCodecMap and the variant
      // is skipped
      Object discriminant = this.discriminantCodec.decode(byteWrapper, null, null);

      // set the discriminant value in the Java object
      this.discriminantAccessor.set(value, discriminant);

      // consume padding
      byteWrapper.advance(this.paddingSize(byteWrapper.getPos() - offset, ENCODED_LENGTH_SIZE));

      // get the variant size
      int variantLength = byteWrapper.getInt();
      if (variantLength != 0) {
        // consume padding
        byteWrapper.advance(
            this.paddingSize(byteWrapper.getPos() - offset, VARIANT_OCTET_BOUNDARY));

        AccessorCodec accessorCodec = accessorCodecMap.get(discriminant);
        if (accessorCodec != null) {
          // decode the variant
          accessorCodec.accessor.set(
              value,
              accessorCodec.codec.decode(byteWrapper, accessorCodec.accessor.get(value), value));
        } else {
          // the enum must be HLAunknown; consume the variant
          byteWrapper.advance(variantLength);
        }
      }

      return value;
    } catch (ReflectiveOperationException ex) {
      throw new InvalidClassStructure(ex.getMessage(), ex);
    }
  }
}
