package nl.tno.oorti.test.managers;

import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.exceptions.RTIexception;
import hla.rti1516e.exceptions.RTIinternalError;
import java.io.IOException;
import java.net.URL;
import java.util.Set;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.oorti.DefaultOOobjectFactory;
import nl.tno.oorti.OOobjectFactory;
import nl.tno.oorti.OOparameter;
import nl.tno.oorti.OOproperties;
import nl.tno.oorti.accessor.AccessorFactory;
import nl.tno.oorti.accessor.AccessorFactoryFactory;
import nl.tno.oorti.impl.InteractionClassManager;
import nl.tno.oorti.impl.InteractionClass;
import nl.tno.oorti.ooencoder.OOencoderFactory;
import nl.tno.oorti.ooencoder.OOencoderFactoryFactory;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;
import nl.tno.oorti.test.managers.hla.rti1516e.MyRTIambassador;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author bergtwvd
 */
public class TestSerializerInteraction extends NullFederateAmbassador {

  static RTIambassador rtiamb;
  static ObjectModelType[] modules;
  static OOproperties properties = new OOproperties();
  static AccessorFactory accessorFactory;
  static OOencoderFactory encoderFactory;

  @BeforeAll
  public static void setUpClass() throws OOcodecException, IOException {
    rtiamb = new MyRTIambassador();

    URL[] foms =
        new URL[] {
          TestSerializerInteraction.class.getResource("/foms/Message.xml"),
          TestSerializerInteraction.class.getResource("/foms/HLAstandardMIM.xml")
        };

    modules = new ObjectModelType[foms.length];
    for (int i = 0; i < foms.length; i++) {
      modules[i] = OmtFunctions.readOmt(foms[i]);
    }

    accessorFactory = AccessorFactoryFactory.getAccessorFactory(properties.getAccessorType());

    encoderFactory =
        OOencoderFactoryFactory.getOOencoderFactory(
            properties.getEncodingType(), accessorFactory, modules);
  }

  private void testSerialisation(OOobjectFactory objectFactory) throws RTIexception {   
    InteractionClassManager icm1 =
        new InteractionClassManager(rtiamb, accessorFactory, encoderFactory, modules);
    InteractionClassManager icm2 =
        new InteractionClassManager(rtiamb, accessorFactory, encoderFactory, modules);

    Message theMessage1 = objectFactory.createInteraction(Message.class);
    Message theMessage2 = objectFactory.createInteraction(Message.class);
    theMessage1.setContents("hello");

    InteractionClass ic1 = icm1.create(objectFactory.getInteractionClass(theMessage1));
    InteractionClass ic2 = icm2.create(objectFactory.getInteractionClass(theMessage2));

    ic1.addPublications();
    ic2.addSubscriptions();

    ParameterHandleValueMap map = ic1.serialize(theMessage1);
    Set<OOparameter> parameterSet = ic2.deserialize(map, theMessage2);

    Assertions.assertEquals(Message.class, ic1.getClazz());
    Assertions.assertEquals(Message.class, ic2.getClazz());
    Assertions.assertEquals(1, map.size());
    Assertions.assertEquals(1, parameterSet.size());
    Assertions.assertEquals(theMessage1.getContents(), theMessage2.getContents());
  }

  class MyOOobjectFactory implements OOobjectFactory {

    @Override
    public Object createObject(Class clazz) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object createInteraction(Class clazz) throws RTIinternalError {
      if (clazz == Message.class) {
        return new SubMessage();
      } else {
        throw new RTIinternalError("Expected class Message but got " + clazz.getSimpleName());
      }
    }

    @Override
    public Class getObjectClass(Object object) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Class getInteractionClass(Object object) throws RTIinternalError {
      if (object.getClass() == SubMessage.class) {
        return Message.class;
      } else {
        throw new RTIinternalError(
            "Expected class SubMessage but got " + object.getClass().getSimpleName());
      }
    }
  }

  @Test
  public void test1() throws RTIexception {
    testSerialisation(new DefaultOOobjectFactory());
  }

  /* Test the processing of Java Bean subclasses with properties that are not in the FOM. */
  @Test
  public void test2() throws RTIexception {
    testSerialisation(new MyOOobjectFactory());
  }
}
