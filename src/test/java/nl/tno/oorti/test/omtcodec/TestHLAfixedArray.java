package nl.tno.oorti.test.omtcodec;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
public class TestHLAfixedArray {

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
    public void testFixedArrayType1() throws OOcodecException {
        OOencoder<int[]> codec = factory.createOOencoder(int[].class, "FixedArrayType1");

        int[] in = new int[]{1, 2, 3};

        int[] out = codec.decode(codec.encode(in));

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
    public void testFixedArrayType2() throws OOcodecException {
        OOencoder<int[][]> codec = factory.createOOencoder(int[][].class, "FixedArrayType2");

        int[][] in = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};

        int[][] out = codec.decode(codec.encode(in));

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
    public void testMyFixedUnicodeStringType1() throws OOcodecException {
        OOencoder<String> codec = factory.createOOencoder(String.class, "MyFixedUnicodeStringType");
        String in = "12345";
        String out = codec.decode(codec.encode(in));
        Assertions.assertEquals(in, out);

        OOencoder<char[]> codec2 = factory.createOOencoder(char[].class, "MyFixedUnicodeStringType");
        char[] out2 = codec2.decode(codec.encode(in));
        Assertions.assertEquals(in, new String(out2));
    }

    @Test
    public void testMyFixedUnicodeStringType2() throws OOcodecException {
        // In this test we provide a char[] instead of a String, for the same datatype name.
        // The codec must handle this.
        OOencoder<char[]> codec = factory.createOOencoder(char[].class, "MyFixedUnicodeStringType");
        char[] in = "12345".toCharArray();
        char[] out = codec.decode(codec.encode(in));

        if (out == null || in.length != out.length) {
            Assertions.fail();
        }

        for (int i = 0; i < in.length; i++) {
            if (in[i] != out[i]) {
                Assertions.fail();
            }
        }

        OOencoder codec2 = factory.createOOencoder(String.class, "MyFixedUnicodeStringType");
        String out2 = (String) codec2.decode(codec.encode(in));
        Assertions.assertEquals(new String(in), out2);
    }

    @Test
    public void testMyFixedASCIIStringType1() throws OOcodecException {
        OOencoder<String> codec = factory.createOOencoder(String.class, "MyFixedASCIIStringType");
        byte[] b = new byte[]{'1', '2', '3', '4', '5'};
        String in = new String(b);
        String out = codec.decode(codec.encode(in));
        Assertions.assertEquals(in, out);

        OOencoder codec2 = factory.createOOencoder(byte[].class, "MyFixedASCIIStringType");
        byte[] out2 = (byte[]) codec2.decode(codec.encode(in));
        Assertions.assertEquals(in, new String(out2));
    }

    @Test
    public void testMyFixedASCIIStringType2() throws OOcodecException {
        // In this test we provide a char[] instead of a String, for the same datatype name.
        // The coded must handle this.
        OOencoder<byte[]> codec = factory.createOOencoder(byte[].class, "MyFixedASCIIStringType");
        byte[] in = new byte[]{'1', '2', '3', '4', '5'};
        byte[] out = codec.decode(codec.encode(in));

        if (out == null || in.length != out.length) {
            Assertions.fail();
        }

        for (int i = 0; i < in.length; i++) {
            if (in[i] != out[i]) {
                Assertions.fail();
            }
        }

        OOencoder codec2 = factory.createOOencoder(String.class, "MyFixedASCIIStringType");
        String out2 = (String) codec2.decode(codec.encode(in));
        Assertions.assertEquals(new String(in), out2);
    }

    /*
    ** Test List types
     */
    List<Integer> list1;

    @Test
    public void testListType1() throws OOcodecException, NoSuchFieldException {
        OOencoder<List<Integer>> codec = factory.createOOencoder(this.getClass().getDeclaredField("list1").getGenericType(), "FixedArrayType1");

        List<Integer> in = new ArrayList();
        in.add(1);
        in.add(2);
        in.add(3);

        List<Integer> out = codec.decode(codec.encode(in));

        if (out == null || in.size() != out.size()) {
            Assertions.fail();
        }

        for (int i = 0; i < in.size(); i++) {
            if (in.get(i).intValue() != out.get(i).intValue()) {
                Assertions.fail();
            }
        }
    }

    List<List<Integer>> list2;

    @Test
    public void testListType2() throws OOcodecException, NoSuchFieldException {
        OOencoder<List<List<Integer>>> codec = factory.createOOencoder(this.getClass().getDeclaredField("list2").getGenericType(), "FixedArrayType2");

        List<List<Integer>> in = new ArrayList();

        List<Integer> in1 = new ArrayList();
        in.add(in1);
        in1.add(1);
        in1.add(2);
        in1.add(3);

        List<Integer> in2 = new ArrayList();
        in.add(in2);
        in2.add(4);
        in2.add(5);
        in2.add(6);

        List<Integer> in3 = new ArrayList();
        in.add(in3);
        in3.add(7);
        in3.add(8);
        in3.add(9);

        List<List<Integer>> out = codec.decode(codec.encode(in));

        if (out == null || in.size() != out.size()) {
            Assertions.fail();
        }

        for (int i = 0; i < in.size(); i++) {
            if (out.get(i) == null || in.get(i).size() != out.get(i).size()) {
                Assertions.fail();
            }

            for (int j = 0; j < in.get(i).size(); j++) {
                if (in.get(i).get(j).intValue() != out.get(i).get(j).intValue()) {
                    Assertions.fail();
                }
            }
        }
    }

    @Test
    public void testUUID1() throws OOcodecException {
        OOencoder<UUID> codec = factory.createOOencoder(UUID.class, "UuidArrayOfHLAbyte16");

        long msb = 0xff11223344556677L;
        long lsb = 0xee12345678901234L;
        UUID in = new UUID(msb, lsb);

        UUID out = codec.decode(codec.encode(in));

        if (out == null || !in.equals(out)) {
            Assertions.fail();
        }
    }

    @Test
    public void testUUID2() throws OOcodecException {
       OOencoder<UUID> codec = factory.createOOencoder(UUID.class);

        long msb = 0xff11223344556677L;
        long lsb = 0xee12345678901234L;

        UUID in = new UUID(msb, lsb);

        UUID out = codec.decode(codec.encode(in));

        if (out == null || !in.equals(out)) {
            Assertions.fail();
        }
    }

    @Test
    public void testUUID3() throws OOcodecException {
        OOencoder<UUID> codec = factory.createOOencoder(UUID.class);

        String uuidString = "00010203-0405-0607-0809-0a0b0c0d0e0f";
        UUID in = UUID.fromString(uuidString);

        if (in.getMostSignificantBits() != 0x0001020304050607L || in.getLeastSignificantBits() != 0x08090A0B0C0D0E0FL) {
            Assertions.fail();
        }

        byte bytes[] = codec.encode(in);
        UUID out = codec.decode(bytes);

        if (out == null || !in.equals(out)) {
            Assertions.fail();
        } else if (!out.toString().equals(uuidString)) {
            Assertions.fail();
        }
    }

    @Test
    public void testUUID4() throws OOcodecException {
        OOencoder<UUID> codec = factory.createOOencoder(UUID.class);

        byte[] bytes = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};
        UUID out = codec.decode(bytes);

        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long msb = bb.getLong();
        long lsb = bb.getLong();
        UUID out2 = new UUID(msb, lsb);
        
        Assertions.assertEquals(out, out2);
    }
}
