package nl.tno.oorti.test.omtcodec.errorbeans;

/**
 *
 * @author bergtwvd
 */
public enum EnumType {

	E1(1),
	E2(2),
	E5(4); // not defined in FOM

	private final int value;

	private EnumType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public int[] getValues() {
		return new int[]{value};
	}
}
