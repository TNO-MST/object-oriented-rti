package nl.tno.oorti.test.omtcodec;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.oorti.ooencoder.OOencoder;
import nl.tno.oorti.ooencoder.OOencoderFactory;
import nl.tno.oorti.ooencoder.OOencoderFactoryFactory;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;
import nl.tno.oorti.test.netn4.datatypes.EnvironmentRecStruct;
import nl.tno.oorti.test.netn4.datatypes.EnvironmentRecVariantStruct;
import nl.tno.oorti.test.netn4.datatypes.EnvironmentRecordTypeEnum32;
import nl.tno.oorti.test.netn4.datatypes.WorldLocationStruct;
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
public class TestRPRextendableVariantRecord {

    static ObjectModelType module;
    static ObjectModelType mim;
    static OOencoderFactory encoderFactory;
    static OOencoderFactory decoderFactory;

    @BeforeAll
    public static void setUpClass() throws IOException {
        module = OmtFunctions.readOmt(TestOMTcodecFactoryClassStructure.class.getResource("/foms/RPR_FOM_v2.0_1516-2010.xml"));
        mim = OmtFunctions.readOmt(TestOMTcodecFactoryClassStructure.class.getResource("/foms/HLAstandardMIM.xml"));
        encoderFactory = OOencoderFactoryFactory.getOOencoderFactory(new ObjectModelType[]{module, mim});
        decoderFactory = OOencoderFactoryFactory.getOOencoderFactory(new ObjectModelType[]{module, mim});
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

    EnvironmentRecStruct[] createEnvironmentRecStruct() {
        EnvironmentRecStruct[] envRecData = new EnvironmentRecStruct[2];

        envRecData[0] = new EnvironmentRecStruct();
        envRecData[0].setIndex(1);
        EnvironmentRecVariantStruct envRecVar0 = new EnvironmentRecVariantStruct();
        envRecData[0].setDataVariant(envRecVar0);
        envRecVar0.setType(EnvironmentRecordTypeEnum32.PointRecord1Type);
        WorldLocationStruct point1data0 = new WorldLocationStruct();
        point1data0.setX(0);
        point1data0.setY(1);
        point1data0.setZ(2);
        envRecVar0.setPoint1GeometryData(point1data0);

        envRecData[1] = new EnvironmentRecStruct();
        envRecData[1].setIndex(2);
        EnvironmentRecVariantStruct envRecVar1 = new EnvironmentRecVariantStruct();
        envRecData[1].setDataVariant(envRecVar1);
        envRecVar1.setType(EnvironmentRecordTypeEnum32.PointRecord1Type);
        WorldLocationStruct point1data1 = new WorldLocationStruct();
        point1data1.setX(10);
        point1data1.setY(11);
        point1data1.setZ(12);
        envRecVar1.setPoint1GeometryData(point1data1);

        return envRecData;
    }

    @Test
    public void testEnvironmentRecStruct1() throws OOcodecException {
        OOencoder encoder = encoderFactory.createOOencoder(EnvironmentRecStruct[].class, "EnvironmentRecStructArray");
        OOencoder decoder = decoderFactory.createOOencoder(EnvironmentRecStruct[].class, "EnvironmentRecStructArray");

        EnvironmentRecStruct[] in = createEnvironmentRecStruct();

        // test if byte encodings are the same
        byte[] bytes1 = encoder.encode(in);
        byte[] bytes2 = decoder.encode(in);

        //System.out.println("1 >>\n"+bytesToHex(bytes1));
        //System.out.println("2 >>\n"+bytesToHex(bytes2));
        Assertions.assertEquals(bytes1.length, bytes2.length);
        Assertions.assertArrayEquals(bytes1, bytes2);

        // test if cross RTI encoding / decoding is correct
        EnvironmentRecStruct[] out = (EnvironmentRecStruct[]) decoder.decode(bytes1);

        Assertions.assertEquals(in.length, out.length);

        Assertions.assertEquals(in[0].getIndex(), out[0].getIndex());
        Assertions.assertEquals(in[0].getDataVariant().getPoint1GeometryData().getX(), out[0].getDataVariant().getPoint1GeometryData().getX(), 0);
        Assertions.assertEquals(in[0].getDataVariant().getPoint1GeometryData().getY(), out[0].getDataVariant().getPoint1GeometryData().getY(), 0);
        Assertions.assertEquals(in[0].getDataVariant().getPoint1GeometryData().getZ(), out[0].getDataVariant().getPoint1GeometryData().getZ(), 0);

        Assertions.assertEquals(in[1].getIndex(), out[1].getIndex());
        Assertions.assertEquals(in[1].getDataVariant().getPoint1GeometryData().getX(), out[1].getDataVariant().getPoint1GeometryData().getX(), 0);
        Assertions.assertEquals(in[1].getDataVariant().getPoint1GeometryData().getY(), out[1].getDataVariant().getPoint1GeometryData().getY(), 0);
        Assertions.assertEquals(in[1].getDataVariant().getPoint1GeometryData().getZ(), out[1].getDataVariant().getPoint1GeometryData().getZ(), 0);
    }

    private final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);

    public String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.US_ASCII);
    }

}
