package nl.tno.oorti.test.omtcodec;

import java.io.IOException;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.oorti.ooencoder.OOencoder;
import nl.tno.oorti.ooencoder.OOencoderFactory;
import nl.tno.oorti.ooencoder.OOencoderFactoryFactory;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;
import nl.tno.oorti.test.omtcodec.beans.VariantRecordType2;
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
public class TestHLAvariantRecord2 {

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
    public void test1() throws OOcodecException {
        OOencoder codec = factory.createOOencoder(VariantRecordType2.class);

        VariantRecordType2 in = new VariantRecordType2();
        in.setDiscriminant(true);
        in.setA1(11);
        in.setA2(3.0);

        VariantRecordType2 out = (VariantRecordType2) codec.decode(codec.encode(in));

        if (out == null) {
            Assertions.fail();
        }

        if (!out.getDiscriminant()) {
            Assertions.fail();
        }
        
        if (out.getA1() != 11) {
            Assertions.fail();
        }

        if (out.getA2() != 0) {
            Assertions.fail();
        }
    }

    @Test
    public void test2() throws OOcodecException {
        OOencoder codec = factory.createOOencoder(VariantRecordType2.class, "VariantRecordType2");

        VariantRecordType2 in = new VariantRecordType2();
        in.setDiscriminant(false);
        in.setA1(11);
        in.setA2(3.0);

        VariantRecordType2 out = (VariantRecordType2) codec.decode(codec.encode(in));

        if (out == null) {
            Assertions.fail();
        }

        if (out.getDiscriminant()) {
            Assertions.fail();
        }

        if (out.getA1() != 0) {
            Assertions.fail();
        }

        if (out.getA2() != 3.0) {
            Assertions.fail();
        }

    }
}
