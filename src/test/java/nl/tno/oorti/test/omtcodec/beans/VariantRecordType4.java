package nl.tno.oorti.test.omtcodec.beans;

/**
 *
 * @author bergtwvd
 */
public class VariantRecordType4 {
	
	EnumType Discriminant;
	int A1; // E1
	double A2; // E2
	int A3; // HLAother

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

	public int getA3() {
		return A3;
	}

	public void setA3(int A3) {
		this.A3 = A3;
	}
	
}
