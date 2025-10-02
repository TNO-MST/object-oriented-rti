package nl.tno.oorti.test.omtcodec.beans;

import nl.tno.oorti.test.mim4.datatypes.HLAboolean;

/**
 *
 * @author bergtwvd
 */
public class VariantRecordType3 {
  	
	HLAboolean Discriminant;
	int A1;
	double A2;

	public HLAboolean getDiscriminant() {
		return Discriminant;
	}

	public void setDiscriminant(HLAboolean Discriminant) {
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
	
}
