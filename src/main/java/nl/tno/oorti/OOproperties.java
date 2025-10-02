package nl.tno.oorti;

/**
 * This class defines configuration properties for the OORTI factory.
 *
 * @author bergtwvd
 */
public class OOproperties {

  /** Use this encoding type to encode/decode Java Bean properties. */
  private final EncodingType encodingType;

  /** Use this accessor type to access Java Bean properties. */
  private final AccessorType accessorType;

  /**
   * Use in-place copy for array and structural data types; yields better performance, but the
   * application must be prepared to work in this mode.
   */
  private final boolean useInPlaceCopy;

  /**
   * Use the current FDD from the RTI, instead of the provided FOM modules in the RTI joinFederation
   * call.
   */
  private final boolean useRtiForCurrentFdd;

  public OOproperties() {
    this(EncodingType.RPR, AccessorType.LAMBDA, false, false);
  }

  public OOproperties(boolean useInPlaceCopy, boolean useRtiForCurrentFdd) {
    this(EncodingType.RPR, AccessorType.LAMBDA, useInPlaceCopy, useRtiForCurrentFdd);
  }

  public OOproperties(
      EncodingType encodingType,
      AccessorType accessorType,
      boolean useInPlaceCopy,
      boolean useRtiForCurrentFdd) {
    this.encodingType = encodingType;
    this.accessorType = accessorType;
    this.useInPlaceCopy = useInPlaceCopy;
    this.useRtiForCurrentFdd = useRtiForCurrentFdd;
  }

  public EncodingType getEncodingType() {
    return encodingType;
  }

  public AccessorType getAccessorType() {
    return accessorType;
  }

  public boolean isUseInPlaceCopy() {
    return useInPlaceCopy;
  }

  public boolean isUseRtiForCurrentFdd() {
    return useRtiForCurrentFdd;
  }
}
