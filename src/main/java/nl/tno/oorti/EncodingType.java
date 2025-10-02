package nl.tno.oorti;

/**
 * The EncodingType enumeration defines the HLA FOM encoders, representations, and datatypes used in
 * the encoding and decoding of Java Beans.
 *
 * <ul>
 *   <li>STANDARD: use the standard HLA FOM encoders, representations, and datatypes as defined in
 *       the HLA MIM.
 *   <li>RPR: RPR extends STANDARD with RPR FOM encoders, representations, and datatypes.
 * </ul>
 *
 * @author bergtwvd
 */
public enum EncodingType {
  STANDARD,
  RPR
}
