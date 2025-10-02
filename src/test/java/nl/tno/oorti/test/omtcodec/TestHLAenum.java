package nl.tno.oorti.test.omtcodec;

import java.io.IOException;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.oorti.ooencoder.OOencoder;
import nl.tno.oorti.ooencoder.OOencoderFactory;
import nl.tno.oorti.ooencoder.OOencoderFactoryFactory;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;
import nl.tno.oorti.test.omtcodec.beans.EnumType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author bergtwvd
 */
public class TestHLAenum {

  static ObjectModelType module;
  static ObjectModelType mim;
  static OOencoderFactory factory;

  @BeforeAll
  public static void setUpClass() throws IOException {
    module =
        OmtFunctions.readOmt(TestOMTcodecFactoryClassStructure.class.getResource("/foms/Test.xml"));
    mim =
        OmtFunctions.readOmt(
            TestOMTcodecFactoryClassStructure.class.getResource("/foms/HLAstandardMIM.xml"));
    factory = OOencoderFactoryFactory.getOOencoderFactory(new ObjectModelType[] {module, mim});
  }

  @AfterAll
  public static void tearDownClass() {}

  @BeforeEach
  public void setUp() {}

  @AfterEach
  public void tearDown() {}

  @Test
  public void testHLAboolean1() throws OOcodecException {
    OOencoder codec = factory.createOOencoder(Boolean.class, "HLAboolean");
    boolean in = true;
    boolean out = (boolean) codec.decode(codec.encode(in));
    Assertions.assertEquals(in, out);
  }

  @Test
  public void testHLAboolean2() throws OOcodecException {
    OOencoder codec = factory.createOOencoder(Boolean.class);
    boolean in = true;
    boolean out = (boolean) codec.decode(codec.encode(in));
    Assertions.assertEquals(in, out);
  }

  @Test
  public void testEnumType1() throws OOcodecException {
    OOencoder codec = factory.createOOencoder(EnumType.class, "EnumType");
    Enum in = EnumType.E2;
    Enum out = (Enum) codec.decode(codec.encode(in));
    Assertions.assertEquals(in, out);
  }

  @Test
  public void testEnumType2() throws OOcodecException {
    OOencoder codec = factory.createOOencoder(EnumType.class);
    Enum in = EnumType.E2;
    Enum out = (Enum) codec.decode(codec.encode(in));
    Assertions.assertEquals(in, out);
  }

  @Test
  public void testEnumType3() throws OOcodecException {
    OOencoder codec = factory.createOOencoder(EnumType.class);

    // value for an unknown enum
    byte[] b = new byte[] {1, 1, 1, 1};

    Enum out = (Enum) codec.decode(b);
    Assertions.assertEquals(EnumType.HLAunknown, out);
  }
}
