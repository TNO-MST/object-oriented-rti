package nl.tno.oorti.ooencoder.omtcodec;

import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * Interface for basic data types. This interface extends the standard interface for data types with
 * additional methods to generate and parse string values of the given basic data type.
 *
 * @author bergtwvd
 */
public interface OmtBasicDatatypeCodec extends OmtDatatypeCodec {

  String toString(Object value);

  Object parse(String value) throws OOcodecException;
}
