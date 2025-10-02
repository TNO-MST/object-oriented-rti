package nl.tno.oorti.test.omtcodec;

import java.io.IOException;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.oorti.ooencoder.OOencoder;
import nl.tno.oorti.ooencoder.OOencoderFactory;
import nl.tno.oorti.ooencoder.OOencoderFactoryFactory;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;
import nl.tno.oorti.test.omtcodec.beans.EnumType;
import nl.tno.oorti.test.omtcodec.beans.VariantRecordType4;
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
public class TestHLAvariantRecord4 {

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
        OOencoder codec = factory.createOOencoder(VariantRecordType4.class);

        VariantRecordType4 in = new VariantRecordType4();
        in.setDiscriminant(EnumType.E5);
        in.setA1(1);
        in.setA2(2.0);
        in.setA3(3);

        VariantRecordType4 out = (VariantRecordType4) codec.decode(codec.encode(in));

        if (out == null) {
            Assertions.fail();
        }

        if (out.getDiscriminant() != EnumType.E5) {
            Assertions.fail();
        }

        // check that the other fields have their default set 0 or null
        if (out.getA1() != 0) {
            Assertions.fail();
        }

        if (out.getA2() != 0) {
            Assertions.fail();
        }

        if (out.getA3() != 3) {
            Assertions.fail();
        }
    }

    @Test
    public void test2() throws OOcodecException {
        OOencoder codec = factory.createOOencoder(VariantRecordType4.class);

        VariantRecordType4 in = new VariantRecordType4();
        in.setDiscriminant(EnumType.E1);
        in.setA1(1);
        in.setA2(2.0);
        in.setA3(3);

        VariantRecordType4 out = (VariantRecordType4) codec.decode(codec.encode(in));

        if (out == null) {
            Assertions.fail();
        }

        if (out.getDiscriminant() != EnumType.E1) {
            Assertions.fail();
        }

        // check that the other fields have their default set 0 or null
        if (out.getA1() != 1) {
            Assertions.fail();
        }

        if (out.getA2() != 0) {
            Assertions.fail();
        }

        if (out.getA3() != 0) {
            Assertions.fail();
        }
    }
}
