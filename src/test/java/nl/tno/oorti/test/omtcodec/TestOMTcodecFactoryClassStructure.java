package nl.tno.oorti.test.omtcodec;

import java.io.IOException;
import java.util.Set;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.oorti.ooencoder.OOencoder;
import nl.tno.oorti.ooencoder.OOencoderFactory;
import nl.tno.oorti.ooencoder.OOencoderFactoryFactory;
import nl.tno.oorti.ooencoder.exceptions.InvalidClassStructure;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;
import nl.tno.oorti.test.omtcodec.beans.FixedRecordType1;
import nl.tno.oorti.test.omtcodec.beans.OuterClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author bergtwvd
 */
public class TestOMTcodecFactoryClassStructure {

	static ObjectModelType module;
	static ObjectModelType mim;
	static OOencoderFactory factory;

	@BeforeAll
	public static void setUpClass() throws IOException  {
		module = OmtFunctions.readOmt(TestOMTcodecFactoryClassStructure.class.getResource("/foms/Test.xml"));
		mim = OmtFunctions.readOmt(TestOMTcodecFactoryClassStructure.class.getResource("/foms/HLAstandardMIM.xml"));
        factory = OOencoderFactoryFactory.getOOencoderFactory(new ObjectModelType[]{module, mim});
	}

	@AfterAll
	public static void tearDownClass() {
	}

	@BeforeEach
	public void setUp() {
	}

	@AfterEach
	public void tearDown() {
	}

	/*
	** Test for class structure
	 */
	@Test
	public void testNestedClass() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(OuterClass.FixedRecordType1.class);

		OuterClass outer = new OuterClass();

		OuterClass.FixedRecordType1 in = outer.new FixedRecordType1();
		in.setField1("Hello");
		in.setField2(12);

		byte[] b = codec.encode(in);

		OuterClass.FixedRecordType1 out = (OuterClass.FixedRecordType1) codec.decode(b, null, outer);

		if (out == null || !in.getField1().equals(out.getField1()) || in.getField2() != out.getField2()) {
			Assertions.fail();
		}
	}

	@Test
	public void testNestedClassError() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(OuterClass.FixedRecordType1.class);

		OuterClass outer = new OuterClass();

		OuterClass.FixedRecordType1 in = outer.new FixedRecordType1();
		in.setField1("Hello");
		in.setField2(12);

		byte[] b = codec.encode(in);

		// the following fails since there is no outer class provided
		InvalidClassStructure thrown = Assertions.assertThrows(InvalidClassStructure.class, () -> {
			OuterClass.FixedRecordType1 out = (OuterClass.FixedRecordType1) codec.decode(b, null, null);
		});

		Assertions.assertNotNull(thrown);
	}

	@Test
	public void testNestedClassError2() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(FixedRecordType1.class);

		OuterClass outer = new OuterClass();

		FixedRecordType1 in = new FixedRecordType1();
		in.setField1("Hello");
		in.setField2(12);

		byte[] b = codec.encode(in);

		// this should result in a standard Java ClassCastException since FixedRecordType1 is not enclosed in the OuterClass
		ClassCastException thrown = Assertions.assertThrows(ClassCastException.class, () -> {
			OuterClass.FixedRecordType1 out = (OuterClass.FixedRecordType1) codec.decode(b, null, outer);
		});
		Assertions.assertNotNull(thrown);

	}

	// declare a parameterized type other than List<T>
	Set<Integer> map;

	@Test
	public void testType() throws OOcodecException {
		// the following should fail since only List types are supported
		InvalidClassStructure thrown = Assertions.assertThrows(InvalidClassStructure.class, () -> {
			OOencoder codec = factory.createOOencoder(this.getClass().getDeclaredField("map").getGenericType());
		});
		Assertions.assertNotNull(thrown);
	}

}
