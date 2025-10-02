package nl.tno.oorti.test.omtcodec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
 *
 * @author bergtwvd
 */
public class TestHLAvariableArray {

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
	 * Test basic array datatypes
	 */
	@Test
	public void testHLAunicodeString() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(String.class, "HLAunicodeString");
		String inValue = "abc";
		String outValue = (String) codec.decode(codec.encode(inValue));
		Assertions.assertEquals(inValue, outValue);
	}

	@Test

	public void testUnicodeString() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(String.class);
		String in = "abc";
		String out = (String) codec.decode(codec.encode(in));
		Assertions.assertEquals(in, out);
	}

	@Test
	public void testHLAASCIIstring() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(String.class, "HLAASCIIstring");
		String inValue = "abc";
		String outValue = (String) codec.decode(codec.encode(inValue));
		Assertions.assertEquals(inValue, outValue);
	}

	@Test
	public void testHLAopaqueData() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(byte[].class, "HLAopaqueData");
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
	public void testHLAtoken() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(byte[].class, "HLAtoken");
		byte[] in = new byte[0];
		byte[] out = (byte[]) codec.decode(codec.encode(in));

		if (out == null || in.length != out.length) {
			Assertions.fail();
		}
	}

	/*
	** Test int arrays with and without datatype name
	 */
	@Test
	public void testVariableArrayType1WithoutName() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(int[].class);

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
	public void testVariableArrayType2WithoutName() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(int[][].class);

		int[][] in = new int[][]{{1}, {2, 3, 4}, {}};

		int[][] out = (int[][]) codec.decode(codec.encode(in));

		if (out == null || in.length != out.length) {
			Assertions.fail();
		}

		for (int i = 0; i < in.length; i++) {
			if (out[i] == null || in[i].length != out[i].length) {
				Assertions.fail();
			}

			for (int j = 0; j < in[i].length; j++) {
				if (in[i][j] != out[i][j]) {
					Assertions.fail();
				}
			}
		}
	}

	@Test
	public void testVariableArrayType1WithName() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(int[].class, "VariableArrayType1");

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
	public void testVariableArrayType2WithName() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(int[][].class, "VariableArrayType2");

		int[][] in = new int[][]{{1}, {2, 3, 4}, {}};

		int[][] out = (int[][]) codec.decode(codec.encode(in));

		if (out == null || in.length != out.length) {
			Assertions.fail();
		}

		for (int i = 0; i < in.length; i++) {
			if (out[i] == null || in[i].length != out[i].length) {
				Assertions.fail();
			}

			for (int j = 0; j < in[i].length; j++) {
				if (in[i][j] != out[i][j]) {
					Assertions.fail();
				}
			}
		}
	}

	/*
	** Test List types
	 */
    List<Integer> list1;

	@Test
	public void testListType1() throws OOcodecException, NoSuchFieldException {
		OOencoder codec = factory.createOOencoder(this.getClass().getDeclaredField("list1").getGenericType());

		List<Integer> in = new ArrayList<>();
		in.add(1);
		in.add(2);
		in.add(3);
		in.add(4);

		List<Integer> out = (List<Integer>) codec.decode(codec.encode(in));

		if (out == null || in.size() != out.size()) {
			Assertions.fail();
		}

		for (int i = 0; i < in.size(); i++) {
			if (in.get(i) != out.get(i)) {
				Assertions.fail();
			}
		}
	}

    List<List<Integer>> list2;

	@Test
	public void testListType2() throws OOcodecException, NoSuchFieldException {
		OOencoder codec = factory.createOOencoder(this.getClass().getDeclaredField("list2").getGenericType());

		List<List<Integer>> in = new ArrayList<>();

		List<Integer> in1 = new ArrayList<>();
		in.add(in1);
		in1.add(1);

		List<Integer> in2 = new ArrayList<>();
		in.add(in2);
		in2.add(2);
		in2.add(3);
		in2.add(4);

		List<Integer> in3 = new ArrayList<>();
		in.add(in3);

		List<List<Integer>> out = (List<List<Integer>>) codec.decode(codec.encode(in));

		if (out == null || in.size() != out.size()) {
			Assertions.fail();
		}

		for (int i = 0; i < in.size(); i++) {
			if (out.get(i) == null || in.get(i).size() != out.get(i).size()) {
				Assertions.fail();
			}

			for (int j = 0; j < in.get(i).size(); j++) {
				if (in.get(i).get(j) != out.get(i).get(j)) {
					Assertions.fail();
				}
			}
		}
	}

	/*
	** Test String types
	 */
	@Test
	public void testMyVariableUnicodeRangeStringType() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(String.class, "MyVariableUnicodeRangeStringType2to4");
		String in = "123";
		String out = (String) codec.decode(codec.encode(in));
		Assertions.assertEquals(in, out);
	}

	@Test
	public void testMyVariableUnicodeStringType1() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(String.class, "MyVariableUnicodeStringType");
		String in = "abc";
		String out = (String) codec.decode(codec.encode(in));
		Assertions.assertEquals(in, out);

		OOencoder codec2 = factory.createOOencoder(char[].class, "MyVariableUnicodeStringType");
		char[] out2 = (char[]) codec2.decode(codec.encode(in));
		Assertions.assertEquals(in, new String(out2));
	}

	@Test
	public void testMyVariableUnicodeStringType2() throws OOcodecException {
		// In this test we provide a char[] instead of a String, for the same datatype name.
		// The codec must handle this.
		OOencoder codec = factory.createOOencoder(char[].class, "MyVariableUnicodeStringType");
		char[] in = "abc".toCharArray();
		char[] out = (char[]) codec.decode(codec.encode(in));

		if (out == null || in.length != out.length) {
			Assertions.fail();
		}

		for (int i = 0; i < in.length; i++) {
			if (in[i] != out[i]) {
				Assertions.fail();
			}
		}

		OOencoder codec2 = factory.createOOencoder(String.class, "MyVariableUnicodeStringType");
		String out2 = (String) codec2.decode(codec.encode(in));
		Assertions.assertEquals(new String(in), out2);
	}

	@Test
	public void testMyVariableASCIIStringType1() throws OOcodecException {
		OOencoder codec = factory.createOOencoder(String.class, "MyVariableASCIIStringType");
		byte[] b = new byte[]{'1', '2', '3'};
		String in = new String(b);
		String out = (String) codec.decode(codec.encode(in));
		Assertions.assertEquals(in, out);

		OOencoder codec2 = factory.createOOencoder(byte[].class, "MyVariableASCIIStringType");
		byte[] out2 = (byte[]) codec2.decode(codec.encode(in));
		Assertions.assertEquals(in, new String(out2));
	}

	@Test
	public void testMyVariableASCIIStringType2() throws OOcodecException {
		// In this test we provide a char[] instead of a String, for the same datatype name.
		// The coded must handle this.
		OOencoder codec = factory.createOOencoder(byte[].class, "MyVariableASCIIStringType");
		byte in[] = new byte[]{'1', '2', '3'};
		byte[] out = (byte[]) codec.decode(codec.encode(in));

		if (out == null || in.length != out.length) {
			Assertions.fail();
		}

		for (int i = 0; i < in.length; i++) {
			if (in[i] != out[i]) {
				Assertions.fail();
			}
		}

		OOencoder codec2 = factory.createOOencoder(String.class, "MyVariableASCIIStringType");
		String out2 = (String) codec2.decode(codec.encode(in));
		Assertions.assertEquals(new String(in), out2);
	}

}
