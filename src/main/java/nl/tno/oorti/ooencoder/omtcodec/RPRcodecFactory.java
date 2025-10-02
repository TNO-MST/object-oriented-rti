package nl.tno.oorti.ooencoder.omtcodec;

import nl.tno.omt.ObjectModelType;
import nl.tno.oorti.accessor.AccessorFactory;

/**
 * @author bergtwvd
 */
public class RPRcodecFactory extends OmtCodecFactory {

  public RPRcodecFactory(AccessorFactory accessorFactory, ObjectModelType[] modules) {
    super(accessorFactory, modules);
    this.register();
  }

  private void register() {
    // basic datatypes
    this.registerBasicDatatype("RPRunsignedInteger8BE", RPRunsignedInteger8BECodec.class);
    this.registerBasicDatatype("RPRunsignedInteger16BE", RPRunsignedInteger16BECodec.class);
    this.registerBasicDatatype("RPRunsignedInteger32BE", RPRunsignedInteger32BECodec.class);
    this.registerBasicDatatype("RPRunsignedInteger64BE", RPRunsignedInteger64BECodec.class);

    // encodings
    this.registerEncoding("RPRpaddingTo32Array", RPRpaddingTo32ArrayCodec.class, true);
    this.registerEncoding("RPRpaddingTo64Array", RPRpaddingTo64ArrayCodec.class, true);
    this.registerEncoding("RPRextendedVariantRecord", RPRextendedVariantRecordCodec.class);
    this.registerEncoding("RPRlengthlessArray", RPRlengthlessArrayCodec.class);
    this.registerEncoding("RPRnullTerminatedArray", RPRnullTerminatedArrayCodec.class);
  }
}
