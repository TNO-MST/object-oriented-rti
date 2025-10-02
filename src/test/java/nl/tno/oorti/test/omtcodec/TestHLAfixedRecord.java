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
public class TestHLAfixedRecord {

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
    public void testFixedRecordType1() throws OOcodecException {
        OOencoder<FixedRecordType1> codec = factory.createOOencoder(FixedRecordType1.class);

        FixedRecordType1 in = new FixedRecordType1();
        in.setField1("Hello");
        in.setField2(12);

        FixedRecordType1 out = codec.decode(codec.encode(in));

        if (out == null || !in.getField1().equals(out.getField1()) || in.getField2() != out.getField2()) {
            Assertions.fail();
        }
    }

    @Test
    public void testFixedRecordType2() throws OOcodecException {
        OOencoder<FixedRecordType1> codec = factory.createOOencoder(FixedRecordType1.class, "FixedRecordType1");

        FixedRecordType1 in = new FixedRecordType1();
        in.setField1("Hello");
        in.setField2(12);

        FixedRecordType1 out = codec.decode(codec.encode(in));

        if (out == null || !in.getField1().equals(out.getField1()) || in.getField2() != out.getField2()) {
            Assertions.fail();
        }
    }

}
