package nl.tno.oorti.test.managers;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.exceptions.RTIexception;
import java.io.IOException;
import java.net.URL;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.oorti.OOproperties;
import nl.tno.oorti.accessor.AccessorFactory;
import nl.tno.oorti.accessor.AccessorFactoryFactory;
import nl.tno.oorti.exceptions.InteractionDecodingError;
import nl.tno.oorti.exceptions.ObjectDecodingError;
import nl.tno.oorti.impl.InteractionClassManager;
import nl.tno.oorti.impl.InteractionClass;
import nl.tno.oorti.impl.ObjectClass;
import nl.tno.oorti.impl.ObjectClassManager;
import nl.tno.oorti.impl.ObjectInstance;
import nl.tno.oorti.impl.ObjectInstanceManager;
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
public class TestDecodingError extends NullFederateAmbassador {

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
          TestDecodingError.class.getResource("/foms/Message.xml"),
          TestDecodingError.class.getResource("/foms/Person.xml"),
          TestDecodingError.class.getResource("/foms/HLAstandardMIM.xml")
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

  @Test
  public void testInteractionDecodingError() throws RTIexception {
    InteractionClassManager icm =
        new InteractionClassManager(rtiamb, accessorFactory, encoderFactory, modules);

    InteractionClass ic = icm.create(Message.class);
    ic.addSubscriptions();

    ParameterHandle ph = rtiamb.getParameterHandle(ic.getClassHandle(), "Contents");
    ParameterHandleValueMap map = rtiamb.getParameterHandleValueMapFactory().create(0);
    map.put(ph, new byte[1]);
    
    Message theInteraction = new Message();

    InteractionDecodingError ex = Assertions.assertThrows(
        InteractionDecodingError.class,
        () -> {
          ic.deserialize(map, theInteraction);
        });
    
    Assertions.assertEquals(theInteraction, ex.getTheInteraction());
    Assertions.assertEquals(ic.getParameterByName("Contents"), ex.getParameter());
    Assertions.assertEquals("00", ex.getValue());
  }

  @Test
  public void testObjectDecodingError() throws RTIexception {
    ObjectClassManager ocm =
        new ObjectClassManager(rtiamb, accessorFactory, encoderFactory, modules);

    ObjectClass oc = ocm.create(Person.class);
    oc.addSubscriptions();

    ObjectInstanceManager oim = new ObjectInstanceManager(properties);
    ObjectInstanceHandle oih = rtiamb.registerObjectInstance(oc.getClassHandle());
    ObjectInstance oi = oim.create(oc, oih, new Person(), "p");

    AttributeHandle ah = rtiamb.getAttributeHandle(oc.getClassHandle(), "Age");
    AttributeHandleValueMap map = rtiamb.getAttributeHandleValueMapFactory().create(0);
    map.put(ah, new byte[1]);

    ObjectDecodingError ex = Assertions.assertThrows(
        ObjectDecodingError.class,
        () -> {
          oi.deserialize(map);
        });
    
    Assertions.assertEquals(oi.getObject(), ex.getTheObject());
    Assertions.assertEquals(oc.getAttributeByName("Age"), ex.getAttribute());
    Assertions.assertEquals("00", ex.getValue());
  }
}
