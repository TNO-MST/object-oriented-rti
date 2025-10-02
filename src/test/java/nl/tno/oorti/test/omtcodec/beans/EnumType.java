package nl.tno.oorti.test.omtcodec.beans;

/**
 *
 * @author bergtwvd
 */
public enum EnumType {

  HLAunknown(0),
	E1(1),
	E2(2),
	E5(5),
	E10(10,11,12);

	private final int[] values;

	private EnumType(int... values) {
		this.values = values;
	}
	
	public int getValue() {
		return values[0];
	}

	public int[] getValues() {
		return values;
	}
}
