package nl.tno.oorti.test.omtcodec;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.oorti.ooencoder.OOencoder;
import nl.tno.oorti.ooencoder.OOencoderFactory;
import nl.tno.oorti.ooencoder.OOencoderFactoryFactory;
import nl.tno.oorti.ooencoder.exceptions.InvalidType;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;
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
public class TestRPRLengthLessArray {

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
	public void testRPRlengthlessIntegerArrayType() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(int[].class, "RPRlengthlessIntegerArrayType");

		int[] in = new int[]{1, 2, 3, 4};
		int[] out = (int[]) codec.decode(codec.encode(in));

		if (out == null || in.length != out.length) {
			Assertions.fail();
		}

		for (int i = 0; i < in.length; i++) {
			if (in[i] != out[i]) {
				Assertions.fail();
			}
		}
	}

	@Test
	public void testRPRlengthlessUnicodeCharArrayType1() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(char[].class, "RPRlengthlessUnicodeCharArrayType");

		char[] in = new char[]{'a', 'b', 'c'};
		char[] out = (char[]) codec.decode(codec.encode(in));

		if (out == null || in.length != out.length) {
			Assertions.fail();
		}

		for (int i = 0; i < in.length; i++) {
			if (in[i] != out[i]) {
				Assertions.fail();
			}
		}
	}

	@Test
	public void testRPRlengthlessUnicodeCharArrayType2() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(String.class, "RPRlengthlessUnicodeCharArrayType");

		String in = "1234";
		String out = (String) codec.decode(codec.encode(in));
		Assertions.assertEquals(in, out);
	}

	List<Character> charList;

	@Test
	public void testRPRlengthlessUnicodeCharArrayType3() throws OOcodecException, NoSuchFieldException {

		Type type = this.getClass().getDeclaredField("charList").getGenericType();
		OOencoder codec = factory.createOOencoder(type, "RPRlengthlessUnicodeCharArrayType");

		List<Character> in = new ArrayList<>();
		in.add('a');
		in.add('b');
		in.add('c');

		List<Character> out = (List<Character>) codec.decode(codec.encode(in));

		if (out == null || in.size() != out.size()) {
			Assertions.fail();
		}

		for (int i = 0; i < in.size(); i++) {
			if (in.get(i) != out.get(i)) {
				Assertions.fail();
			}
		}
	}

	@Test
	public void testRPRlengthlessASCIICharArrayType1() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(byte[].class, "RPRlengthlessASCIICharArrayType");

		byte[] in = new byte[]{'a', 'b', 'c'};
		byte[] out = (byte[]) codec.decode(codec.encode(in));

		if (out == null || in.length != out.length) {
			Assertions.fail();
		}

		for (int i = 0; i < in.length; i++) {
			if (in[i] != out[i]) {
				Assertions.fail();
			}
		}
	}

	@Test
	public void testRPRlengthlessASCIICharArrayType2() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(String.class, "RPRlengthlessASCIICharArrayType");

		byte[] inbytes = new byte[]{'a', 'b', 'c'};
		String in = new String(inbytes);
		String out = (String) codec.decode(codec.encode(in));
		byte[] outbytes = (out == null) ? null : out.getBytes();

		if (outbytes == null || inbytes.length != outbytes.length) {
			Assertions.fail();
		}

		for (int i = 0; i < inbytes.length; i++) {
			if (inbytes[i] != outbytes[i]) {
				Assertions.fail();
			}
		}
	}

	@Test
	public void testRPRlengthlessUnicodeCharArrayTypeException() throws OOcodecException {
		// following should fail because char is wrong type for Unicode; must be char
		InvalidType thrown = Assertions.assertThrows(InvalidType.class, () -> {
			OOencoder codec = factory.createOOencoder(byte[].class, "RPRlengthlessUnicodeCharArrayType");
		});
		Assertions.assertNotNull(thrown);
	}

	@Test
	public void testRPRlengthlessASCIICharArrayTypeException() throws OOcodecException {
		// following should fail because char is wrong type for ASCII; must be byte
		InvalidType thrown = Assertions.assertThrows(InvalidType.class, () -> {
			OOencoder codec = factory.createOOencoder(char[].class, "RPRlengthlessASCIICharArrayType");
		});
		Assertions.assertNotNull(thrown);
	}
}
