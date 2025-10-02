package nl.tno.oorti.test.omtcodec.beans;

/**
 *
 * @author bergtwvd
 */
public class VariantRecordType1 {
	
	EnumType Discriminant;
	int A1;
	double A2;
	FixedRecordType1 A5;

	public EnumType getDiscriminant() {
		return Discriminant;
	}

	public void setDiscriminant(EnumType Discriminant) {
		this.Discriminant = Discriminant;
	}

	public int getA1() {
		return A1;
	}

	public void setA1(int A1) {
		this.A1 = A1;
	}

	public double getA2() {
		return A2;
	}

	public void setA2(double A2) {
		this.A2 = A2;
	}

	public FixedRecordType1 getA5() {
		return A5;
	}

	public void setA5(FixedRecordType1 A5) {
		this.A5 = A5;
	}
	
}
