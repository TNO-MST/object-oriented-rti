package nl.tno.oorti.ooencoder;

import java.lang.reflect.Type;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * Factory interface to create an `OOencoder` object.
 *
 * @author bergtwvd
 */
public interface OOencoderFactory {

  /**
   * @param type: the Java Type for which an `OOencoder` object is to be created. The type must
   *     match with the datatype provided in the HLA FOM modules.
   * @param datatypeName: the associated HLA FOM datatype to de-conflict naming conflicts.
   * @return OOencoder object
   * @throws OOcodecException
   */
  public OOencoder createOOencoder(Type type, String datatypeName) throws OOcodecException;

  /**
   * @param type: the Java Type for which an `OOencoder` object is to be created. The type must
   *     match with the datatype provided in the HLA FOM modules.
   * @return OOencoder object
   * @throws OOcodecException
   */
  public OOencoder createOOencoder(Type type) throws OOcodecException;
}
