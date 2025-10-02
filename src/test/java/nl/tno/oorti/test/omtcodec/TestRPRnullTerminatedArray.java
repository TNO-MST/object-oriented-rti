package nl.tno.oorti.test.omtcodec;

import java.io.IOException;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.oorti.ooencoder.OOencoder;
import nl.tno.oorti.ooencoder.OOencoderFactoryFactory;
import nl.tno.oorti.ooencoder.exceptions.InvalidClassStructure;
import nl.tno.oorti.ooencoder.exceptions.InvalidType;
import nl.tno.oorti.ooencoder.exceptions.InvalidValue;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;
import static nl.tno.oorti.test.omtcodec.TestOMTcodecFactoryException.factory;
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
public class TestRPRnullTerminatedArray {

	static ObjectModelType module;
	static ObjectModelType mim;

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

	@Test
	public void testRPRnullTerminatedASCIICharArrayType1() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(String.class, "RPRnullTerminatedASCIICharArrayType");
		String in = "abc";
		String out = (String) codec.decode(codec.encode(in));
		Assertions.assertEquals(in, out);
	}

	@Test
	public void testRPRnullTerminatedASCIICharArrayType2() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(String.class, "RPRnullTerminatedASCIICharArrayType1to2");
		String in1 = "a";
		String out1 = (String) codec.decode(codec.encode(in1));
		Assertions.assertEquals(in1, out1);

		String in2 = "ab";
		String out2 = (String) codec.decode(codec.encode(in2));
		Assertions.assertEquals(in2, out2);
	}

	@Test
	public void testRPRnullTerminatedASCIICharArrayType3() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(String.class, "RPRnullTerminatedASCIICharArrayType1to2");
		String in = "abc";

		InvalidValue thrown = Assertions.assertThrows(InvalidValue.class, () -> {
			String out = (String) codec.decode(codec.encode(in));
		});
		Assertions.assertNotNull(thrown);
	}

	@Test
	public void testRPRnullTerminatedASCIICharArrayType4() throws OOcodecException {
		// folling should fail since only String is supported
		InvalidClassStructure thrown = Assertions.assertThrows(InvalidClassStructure.class, () -> {
			OOencoder codec = factory.createOOencoder(byte[].class, "RPRnullTerminatedASCIICharArrayType1to2");
		});
		Assertions.assertNotNull(thrown);
	}
}
