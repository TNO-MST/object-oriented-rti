package nl.tno.oorti.test.omtcodec;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.oorti.ooencoder.OOencoder;
import nl.tno.oorti.ooencoder.OOencoderFactory;
import nl.tno.oorti.ooencoder.OOencoderFactoryFactory;
import nl.tno.oorti.ooencoder.exceptions.InvalidClassStructure;
import nl.tno.oorti.ooencoder.exceptions.InvalidType;
import nl.tno.oorti.ooencoder.exceptions.InvalidValue;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;
import nl.tno.oorti.test.omtcodec.beans.MyFixedRecordType1;
import nl.tno.oorti.test.omtcodec.beans.VariantRecordType1;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unhappy tests for createOOencoder(Class).
 *
 * @author bergtwvd
 */
public class TestOMTcodecFactoryException {

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
    ** Test for undefined datatype
	 */
	@Test
	public void testUndefinedDataType1() throws OOcodecException {
		InvalidType thrown = Assertions.assertThrows(InvalidType.class, () -> {
			OOencoder codec = factory.createOOencoder(this.getClass());
		});
		Assertions.assertNotNull(thrown);
	}

	@Test
	public void testUndefinedDataType2() throws OOcodecException {
		InvalidType thrown = Assertions.assertThrows(InvalidType.class, () -> {
			OOencoder codec = factory.createOOencoder(String.class, "UndefinedType");
		});
		Assertions.assertNotNull(thrown);
	}

	// declare list without type, so that it will be interpreted as a record type
	List list1a;

	@Test
	public void testListType1() throws OOcodecException {
		InvalidType thrown = Assertions.assertThrows(InvalidType.class, () -> {
			OOencoder codec = factory.createOOencoder(this.getClass().getDeclaredField("list1a").getGenericType());
		});
		Assertions.assertNotNull(thrown);
	}

	/*
    ** Test for undefined codec
	 */
	@Test
	public void testMySimpleType() throws OOcodecException {
		InvalidType thrown = Assertions.assertThrows(InvalidType.class, () -> {
			OOencoder codec = factory.createOOencoder(String.class, "MySimpleType");
		});
		Assertions.assertNotNull(thrown);
	}

	@Test
	public void testMyArrayType() throws OOcodecException {
		InvalidType thrown = Assertions.assertThrows(InvalidType.class, () -> {
			OOencoder codec = factory.createOOencoder(int[].class, "MyArrayType");
		});
		Assertions.assertNotNull(thrown);
	}

	@Test
	public void testMyFixedRecordType1() throws OOcodecException {
		InvalidType thrown = Assertions.assertThrows(InvalidType.class, () -> {
			OOencoder codec = factory.createOOencoder(MyFixedRecordType1.class, "MyFixedRecordType1");
		});
		Assertions.assertNotNull(thrown);
	}

	/*
    ** Test for mismatches
	 */
	@Test
	public void testDataTypeMismatchForInteger() throws OOcodecException {
		InvalidType thrown = Assertions.assertThrows(InvalidType.class, () -> {
			OOencoder codec = factory.createOOencoder(int.class, "ShortType");
		});
		Assertions.assertNotNull(thrown);
	}

	@Test
	public void testDataTypeMismatchForShort() throws OOcodecException {
		InvalidType thrown = Assertions.assertThrows(InvalidType.class, () -> {
			OOencoder codec = factory.createOOencoder(short.class, "IntegerType");
		});
		Assertions.assertNotNull(thrown);
	}

	@Test
	public void testEnumMismatch1() throws OOcodecException {
		InvalidClassStructure thrown = Assertions.assertThrows(InvalidClassStructure.class, () -> {
			OOencoder codec = factory.createOOencoder(nl.tno.oorti.test.omtcodec.errorbeans.EnumType.class);
			// this should fail because the enumType has more enums defined than the FOM
		});
		Assertions.assertNotNull(thrown);
	}

	@Test
	public void testDataTypeMismatchForVariantRecordType1() throws OOcodecException {
		InvalidType thrown = Assertions.assertThrows(InvalidType.class, () -> {
			OOencoder codec = factory.createOOencoder(VariantRecordType1.class, "FixedRecordType1");
		});
		Assertions.assertNotNull(thrown);
	}

	@Test
	public void testCardinalityMismatch() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(int[].class, "FixedArrayType1");

		// create an array larger than the declared cardinality
		int[] in = new int[]{1, 2, 3, 4};

		// this method should raise an exception
		InvalidValue thrown = Assertions.assertThrows(InvalidValue.class, () -> {
			byte[] b = codec.encode(in);
		});
		Assertions.assertNotNull(thrown);
	}

	@Test
	public void testCardinalityMismatchForMyFixedUnicodeString1() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(String.class, "MyFixedUnicodeStringType");
		String in = "1234";

		// this method should raise an exception
		InvalidValue thrown = Assertions.assertThrows(InvalidValue.class, () -> {
			String out = (String) codec.decode(codec.encode(in));
		});
		Assertions.assertNotNull(thrown);
	}

	@Test
	public void testCardinalityMismatchForMyFixedUnicodeString2() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(char[].class, "MyFixedUnicodeStringType");
		char[] in = "1234".toCharArray();

		// this method should raise an exception
		InvalidValue thrown = Assertions.assertThrows(InvalidValue.class, () -> {
			char[] out = (char[]) codec.decode(codec.encode(in));
		});
		Assertions.assertNotNull(thrown);
	}

	@Test
	public void testCardinalityMismatchForMyFixedASCIIString1() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(String.class, "MyFixedASCIIStringType");
		byte[] b = new byte[]{'1', '2', '3', '4'};
		String in = new String(b);

		// this method should raise an exception
		InvalidValue thrown = Assertions.assertThrows(InvalidValue.class, () -> {
			String out = (String) codec.decode(codec.encode(in));
		});
		Assertions.assertNotNull(thrown);
	}

	@Test
	public void testCardinalityMismatchForMyFixedASCIIString2() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(byte[].class, "MyFixedASCIIStringType");
		byte[] in = new byte[]{'1', '2', '3', '4'};

		// this method should raise an exception
		InvalidValue thrown = Assertions.assertThrows(InvalidValue.class, () -> {
			byte[] out = (byte[]) codec.decode(codec.encode(in));
		});
		Assertions.assertNotNull(thrown);
	}

	@Test
	public void testMyVariableUnicodeRangeStringType() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(String.class, "MyVariableUnicodeRangeStringType2to4");
		String in1 = "12345";

		InvalidValue thrown = Assertions.assertThrows(InvalidValue.class, () -> {
			String out = (String) codec.decode(codec.encode(in1));
		});
		Assertions.assertNotNull(thrown);
	}

	@Test
	public void testUUIDType() throws OOcodecException {
		InvalidType thrown = Assertions.assertThrows(InvalidType.class, () -> {
			OOencoder codec = factory.createOOencoder(UUID.class, "UuidArrayOfHLAbyte15");
		});
		Assertions.assertNotNull(thrown);
	}
}
