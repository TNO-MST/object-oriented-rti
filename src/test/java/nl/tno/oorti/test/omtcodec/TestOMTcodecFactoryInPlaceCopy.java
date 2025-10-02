package nl.tno.oorti.test.omtcodec;

import java.io.IOException;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.oorti.ooencoder.OOencoder;
import nl.tno.oorti.ooencoder.OOencoderFactory;
import nl.tno.oorti.ooencoder.OOencoderFactoryFactory;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;
import nl.tno.oorti.test.omtcodec.beans.FixedRecordType1;
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
public class TestOMTcodecFactoryInPlaceCopy {

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
    ** Test for in place copy
     */
    @Test
    public void testInPlaceCopyForRecord() throws OOcodecException {

        OOencoder codec = factory.createOOencoder(FixedRecordType1.class);

        String v1 = "Hello";
        int v2 = 12;

        // initialize the input
        FixedRecordType1 in = new FixedRecordType1();
        in.setField1(v1);
        in.setField2(v2);

        // encode the input
        byte[] b = codec.encode(in);

        // change the values to something else
        in.setField1(v1 + "x");
        in.setField2(v2 + 1);

        // decode the saved data
        FixedRecordType1 out = (FixedRecordType1) codec.decode(b, in);

        if (out != in || !in.getField1().equals(v1) || in.getField2() != v2) {
            Assertions.fail();
        }
    }

    @Test
    public void testInPlaceCopyForFixedArray1() throws OOcodecException {

        OOencoder codec = factory.createOOencoder(int[].class, "FixedArrayType1");

        // initialize the input
        int[] in = new int[]{1, 2, 3};

        // encode the input
        byte[] b = codec.encode(in);

        // change the values to something else
        in[0]++;
        in[1]++;
        in[2]++;

        // decode the saved data
        int[] out = (int[]) codec.decode(b, in);

        if (out != in || out.length != 3) {
            Assertions.fail();
        }

        if (out[0] != 1 || out[1] != 2 || out[2] != 3) {
            Assertions.fail();
        }
    }

    @Test
    public void testInPlaceCopyForFixedArray2() throws OOcodecException {

        OOencoder codec = factory.createOOencoder(int[].class, "FixedArrayType1");

        // initialize the input
        int[] in = new int[]{1, 2, 3};

        // encode the input
        byte[] b = codec.encode(in);

        // change the array size
        in = new int[]{1, 2, 3, 4, 5, 6};

        // decode the saved data
        int[] out = (int[]) codec.decode(b, in);

        if (out == in || out.length != 3) {
            Assertions.fail();
        }

        if (out[0] != 1 || out[1] != 2 || out[2] != 3) {
            Assertions.fail();
        }
    }

    @Test
    public void testInPlaceCopyForVariableArray1() throws OOcodecException {

        OOencoder codec = factory.createOOencoder(int[].class, "VariableArrayType1");

        // initialize the input
        int[] in = new int[]{1, 2, 3};

        // encode the input
        byte[] b = codec.encode(in);

        // change the values to something else
        in[0]++;
        in[1]++;
        in[2]++;

        // decode the saved data
        int[] out = (int[]) codec.decode(b, in);

        if (out != in || out.length != 3) {
            Assertions.fail();
        }

        if (out[0] != 1 || out[1] != 2 || out[2] != 3) {
            Assertions.fail();
        }
    }

    @Test
    public void testInPlaceCopyForVariableArray2() throws OOcodecException {

        OOencoder codec = factory.createOOencoder(int[].class, "VariableArrayType1");

        // initialize the input
        int[] in = new int[]{1, 2, 3};

        // encode the input
        byte[] b = codec.encode(in);

        // change the array size
        in = new int[]{1, 2, 3, 4, 5, 6};

        // decode the saved data
        int[] out = (int[]) codec.decode(b, in);

        if (out == in || out.length != 3) {
            Assertions.fail();
        }

        if (out[0] != 1 || out[1] != 2 || out[2] != 3) {
            Assertions.fail();
        }
    }

}
