package nl.tno.oorti.test.omtcodec.beans;

/**
 *
 * @author bergtwvd
 */
public class OuterClass {

	public class FixedRecordType1 {

		String Field1;
		int Field2;

		public String getField1() {
			return Field1;
		}

		public void setField1(String Field1) {
			this.Field1 = Field1;
		}

		public int getField2() {
			return Field2;
		}

		public void setField2(int Field2) {
			this.Field2 = Field2;
		}
	}
	
	FixedRecordType1 rec;

	public FixedRecordType1 getRec() {
		return rec;
	}

	public void setRec(FixedRecordType1 rec) {
		this.rec = rec;
	}

}
