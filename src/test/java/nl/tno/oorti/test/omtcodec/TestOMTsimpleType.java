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
 * Happy tests for createOOencoder(Class, String).
 *
 * @author bergtwvd
 */
public class TestOMTsimpleType {

    static ObjectModelType module;
    static ObjectModelType mim;
    static OOencoderFactory factory;

    @BeforeAll
    public static void setUpClass() throws IOException {
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
    ** Test simple types
     */
    @Test
    public void testHLAbyte() throws OOcodecException {
        OOencoder codec = factory.createOOencoder(Byte.class, "HLAbyte");
        byte in = 'x';
        byte out = (byte) codec.decode(codec.encode(in));
        Assertions.assertEquals(in, out);
    }

    @Test
    public void testHLAASCIIchar() throws OOcodecException {
        OOencoder codec = factory.createOOencoder(Byte.class, "HLAASCIIchar");
        byte in = 'x';
        byte out = (byte) codec.decode(codec.encode(in));
        Assertions.assertEquals(in, out);
    }

    @Test
    public void testHLAunicodeChar() throws OOcodecException {
        OOencoder codec = factory.createOOencoder(Character.class, "HLAunicodeChar");
        char in = 'x';
        char out = (char) codec.decode(codec.encode(in));
        Assertions.assertEquals(in, out);
    }

    @Test
    public void testHLAinteger64time() throws OOcodecException {
        OOencoder codec = factory.createOOencoder(Long.class, "HLAinteger64Time");
        long in = 11;
        long out = (long) codec.decode(codec.encode(in));
        Assertions.assertEquals(in, out);
    }

    @Test
    public void testHLAfloat64time() throws OOcodecException {
        OOencoder codec = factory.createOOencoder(Double.class, "HLAfloat64Time");
        double in = 11;
        double out = (double) codec.decode(codec.encode(in));
        Assertions.assertEquals(in, out, 0);
    }

    @Test
    public void testRPRByteType() throws OOcodecException {
        OOencoder codec = factory.createOOencoder(Byte.class, "RPRByteType");
        byte in = 11;
        byte out = (byte) codec.decode(codec.encode(in));
        Assertions.assertEquals(in, out);
    }

    @Test
    public void testRPRShortType() throws OOcodecException {
        OOencoder codec = factory.createOOencoder(Short.class, "RPRShortType");
        short in = 11;
        short out = (short) codec.decode(codec.encode(in));
        Assertions.assertEquals(in, out);
    }

    @Test
    public void testRPRIntegerType() throws OOcodecException {
        OOencoder codec = factory.createOOencoder(Integer.class, "RPRIntegerType");
        int in = 11;
        int out = (int) codec.decode(codec.encode(in));
        Assertions.assertEquals(in, out);
    }

    @Test
    public void testRPRLongType() throws OOcodecException {
        OOencoder codec = factory.createOOencoder(Long.class, "RPRLongType");
        long in = 11;
        long out = (long) codec.decode(codec.encode(in));
        Assertions.assertEquals(in, out);
    }

    @Test
    public void testShortType() throws OOcodecException {
        OOencoder codec = factory.createOOencoder(Short.class, "ShortType");
        short in = 10;
        short out = (short) codec.decode(codec.encode(in));
        Assertions.assertEquals(in, out);
    }

    @Test
    public void testIntegerType() throws OOcodecException {
        OOencoder codec = factory.createOOencoder(Integer.class, "IntegerType");
        int in = 10;
        int out = (int) codec.decode(codec.encode(in));
        Assertions.assertEquals(in, out);
    }

    @Test
    public void testLongType() throws OOcodecException {
        OOencoder codec = factory.createOOencoder(Long.class, "LongType");
        long in = 10;
        long out = (long) codec.decode(codec.encode(in));
        Assertions.assertEquals(in, out);
    }

    @Test
    public void testFloatType() throws OOcodecException {
        OOencoder codec = factory.createOOencoder(Float.class, "FloatType");
        float in = 10;
        float out = (float) codec.decode(codec.encode(in));
        Assertions.assertEquals(in, out, 0);
    }

    @Test
    public void testDoubleType() throws OOcodecException {
        OOencoder codec = factory.createOOencoder(Double.class, "DoubleType");
        double in = 10;
        double out = (double) codec.decode(codec.encode(in));
        Assertions.assertEquals(in, out, 0);
    }

}
