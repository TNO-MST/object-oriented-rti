package nl.tno.oorti.ooencoder;

import nl.tno.omt.ObjectModelType;
import nl.tno.oorti.EncodingType;
import nl.tno.oorti.accessor.AccessorFactory;
import nl.tno.oorti.accessor.AccessorFactoryFactory;
import nl.tno.oorti.ooencoder.omtcodec.OmtCodecFactory;
import nl.tno.oorti.ooencoder.omtcodec.RPRcodecFactory;

/**
 * Factory for creating an OOencoder.
 *
 * @author bergtwvd
 */
public class OOencoderFactoryFactory {

  /**
   * Get an OOencoderFactory for the given modules, using RPR encoders and the LAMBDA access method
   * for Java Bean properties.
   *
   * @param modules
   * @return OOencoderFactory
   */
  public static OOencoderFactory getOOencoderFactory(ObjectModelType[] modules) {
    return new OOencoderFactoryImpl(
        new RPRcodecFactory(AccessorFactoryFactory.getAccessorFactory(), modules));
  }

  /**
   * Get an OOencoderFactory for the given modules, for the given encoding type, and for the given
   * accessor factory for Java Bean properties.
   *
   * @param encodingType: encoding type for the Java Bean properties
   * @param accessorFactory: accessor factory for the Java Bean properties
   * @param modules: FOM modules
   * @return OOencoderFactory
   */
  public static OOencoderFactory getOOencoderFactory(
      EncodingType encodingType, AccessorFactory accessorFactory, ObjectModelType[] modules) {
    switch (encodingType) {
      case STANDARD:
        return new OOencoderFactoryImpl(new OmtCodecFactory(accessorFactory, modules));
      default:
      case RPR:
        return new OOencoderFactoryImpl(new RPRcodecFactory(accessorFactory, modules));
    }
  }
}
