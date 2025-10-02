package nl.tno.oorti.ooencoder;

import java.lang.reflect.Type;
import nl.tno.oorti.ooencoder.omtcodec.OmtCodecFactory;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * Factory for creating an OOencoder.
 *
 * @author bergtwvd
 */
class OOencoderFactoryImpl implements OOencoderFactory {

  private final OmtCodecFactory codecFactory;

  OOencoderFactoryImpl(OmtCodecFactory codecFactory) {
    this.codecFactory = codecFactory;
  }

  @Override
  public OOencoder createOOencoder(Type type, String datatypeName) throws OOcodecException {
    return new OOencoderImpl(this.codecFactory.createDatatype(type, datatypeName));
  }

  @Override
  public OOencoder createOOencoder(Type type) throws OOcodecException {
    return new OOencoderImpl(this.codecFactory.createDatatype(type));
  }
}
