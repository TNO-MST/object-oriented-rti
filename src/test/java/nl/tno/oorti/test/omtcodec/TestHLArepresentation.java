package nl.tno.oorti.test.omtcodec;

import java.io.IOException;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.oorti.ooencoder.OOencoder;
import nl.tno.oorti.ooencoder.OOencoderFactory;
import nl.tno.oorti.ooencoder.OOencoderFactoryFactory;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Happy tests for createOOencoder(Class).
 *
 * @author bergtwvd
 */
public class TestHLArepresentation {

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

	@Test
	public void testShort() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(Short.class);
		short in = 10;
		short out = (short) codec.decode(codec.encode(in));
		Assertions.assertEquals(in, out);
	}

	@Test
	public void testInteger() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(Integer.class);
		int in = 10;
		int out = (int) codec.decode(codec.encode(in));
		Assertions.assertEquals(in, out);
	}

	@Test
	public void testLong() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(Long.class);
		long in = 10;
		long out = (long) codec.decode(codec.encode(in));
		Assertions.assertEquals(in, out);
	}

	@Test
	public void testFloat() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(Float.class);
		float in = 10.0f;
		float out = (float) codec.decode(codec.encode(in));
		Assertions.assertEquals(in, out, 0);
	}

	@Test
	public void testDouble() throws OOcodecException {
		OOencoder coded = factory.createOOencoder(Double.class);
		double in = 10.0;
		double out = (double) coded.decode(coded.encode(in));
		Assertions.assertEquals(in, out, 0);
	}

	@Test
	public void testByte() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(Byte.class);
		byte in = 'x';
		byte out = (byte) codec.decode(codec.encode(in));
		Assertions.assertEquals(in, out);
	}

	@Test
	public void testChar() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(Character.class);
		char in = 'x';
		char out = (char) codec.decode(codec.encode(in));
		Assertions.assertEquals(in, out);
	}

}
