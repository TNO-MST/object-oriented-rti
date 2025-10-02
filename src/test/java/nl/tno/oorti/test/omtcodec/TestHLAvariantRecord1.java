package nl.tno.oorti.test.omtcodec;

import java.io.IOException;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.oorti.ooencoder.OOencoder;
import nl.tno.oorti.ooencoder.OOencoderFactory;
import nl.tno.oorti.ooencoder.OOencoderFactoryFactory;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;
import nl.tno.oorti.test.omtcodec.beans.EnumType;
import nl.tno.oorti.test.omtcodec.beans.FixedRecordType1;
import nl.tno.oorti.test.omtcodec.beans.VariantRecordType1;
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
public class TestHLAvariantRecord1 {

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
        OOencoder codec = factory.createOOencoder(VariantRecordType1.class);

        VariantRecordType1 in = new VariantRecordType1();
        in.setDiscriminant(EnumType.E5);
        in.setA1(11);
        in.setA2(3.0);

        FixedRecordType1 f = new FixedRecordType1();
        f.setField1("Hello");
        f.setField2(12);
        in.setA5(f);

        VariantRecordType1 out = (VariantRecordType1) codec.decode(codec.encode(in));

        if (out == null) {
            Assertions.fail();
        }

        if (out.getDiscriminant() != EnumType.E5) {
            Assertions.fail();
        }

        if (out.getA5() == null) {
            Assertions.fail();
        }

        if (!out.getA5().getField1().equals("Hello")) {
            Assertions.fail();
        }

        if (out.getA5().getField2() != 12) {
            Assertions.fail();
        }

        // check that the other fields have their default set 0 or null
        if (out.getA1() != 0) {
            Assertions.fail();
        }

        if (out.getA2() != 0) {
            Assertions.fail();
        }
    }
}
