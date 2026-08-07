package nl.tno.oorti.test.managers;

import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.ObjectClassNotPublished;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import java.io.IOException;
import java.net.URL;
import java.util.Set;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.oorti.DefaultOOobjectFactory;
import nl.tno.oorti.OOattribute;
import nl.tno.oorti.OOobjectFactory;
import nl.tno.oorti.OOproperties;
import nl.tno.oorti.accessor.AccessorFactory;
import nl.tno.oorti.accessor.AccessorFactoryFactory;
import nl.tno.oorti.exceptions.ObjectDecodingError;
import nl.tno.oorti.exceptions.ObjectEncodingError;
import nl.tno.oorti.impl.ObjectClass;
import nl.tno.oorti.impl.ObjectClassManager;
import nl.tno.oorti.impl.ObjectInstance;
import nl.tno.oorti.impl.ObjectInstanceManager;
import nl.tno.oorti.ooencoder.OOencoderFactory;
import nl.tno.oorti.ooencoder.OOencoderFactoryFactory;
import nl.tno.oorti.test.managers.hla.rti1516e.MyRTIambassador;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author bergtwvd
 */
public class TestSerializerObject extends NullFederateAmbassador {

  static RTIambassador rtiamb;
  static ObjectModelType[] modules;
  static OOobjectFactory objectFactory = new DefaultOOobjectFactory();
  static OOproperties properties = new OOproperties();
  static AccessorFactory accessorFactory;
  static OOencoderFactory encoderFactory;

  @BeforeAll
  public static void setUpClass() throws IOException {
    rtiamb = new MyRTIambassador();

    URL[] foms =
        new URL[] {
          TestSerializerObject.class.getResource("/foms/Person.xml"),
          TestSerializerObject.class.getResource("/foms/HLAstandardMIM.xml")
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
  public void test1()
      throws FederateNotExecutionMember,
          NotConnected,
          RTIinternalError,
          AttributeNotDefined,
          ObjectClassNotDefined,
          ObjectClassNotPublished,
          SaveInProgress,
          RestoreInProgress,
          ObjectInstanceNotKnown,
          ObjectEncodingError,
          ObjectDecodingError {
    
    ObjectClassManager ocm =
        new ObjectClassManager(rtiamb, accessorFactory, encoderFactory, modules);

    ObjectInstanceManager oim = new ObjectInstanceManager(properties);

    ObjectClass oc1 = ocm.create(Person.class);
    oc1.addPublications();
    oc1.addSubscriptions();

    Person person1 = new Person();
    person1.setName("MyName");
    person1.setAge(20);

    ObjectInstanceHandle instanceHandle1 = rtiamb.registerObjectInstance(oc1.getClassHandle());
    ObjectInstance oi = oim.create(oc1, instanceHandle1, person1, "p1");
    AttributeHandleValueMap map = oi.serialize();

    Assertions.assertEquals(2, map.size());

    Person person2 = new Person();

    Assertions.assertEquals(null, person2.getName());
    Assertions.assertEquals(0.0, person2.getAge());

    ObjectClass oc2 = ocm.getObjectClassIfExists(Person.class);
    ObjectInstanceHandle instanceHandle2 = rtiamb.registerObjectInstance(oc2.getClassHandle());
    ObjectInstance oi2 = oim.create(oc2, instanceHandle2, person2, "p2");
    Set<OOattribute> attribuetSet2 = oi2.deserialize(map);

    Assertions.assertEquals(2, attribuetSet2.size());
    Assertions.assertEquals(person1.getName(), person2.getName());
    Assertions.assertEquals(person1.getAge(), person2.getAge());

    // use an existing object to let the serializer re-populate the object
    Person person3 = new Person();
    person3.setName("aaa");
    person3.setAge(111);

    ObjectClass oc3 = ocm.getObjectClassIfExists(Person.class);
    ObjectInstanceHandle instanceHandle3 = rtiamb.registerObjectInstance(oc2.getClassHandle());
    ObjectInstance oi3 = oim.create(oc3, instanceHandle3, person3, "p3");
    Set<OOattribute> attribuetSet3 = oi3.deserialize(map);

    Assertions.assertEquals(2, attribuetSet3.size());
    Assertions.assertEquals(person1.getName(), person3.getName());
    Assertions.assertEquals(person1.getAge(), person3.getAge());
  }

  // Simple test for using Object and HLAobjectRoot
  @Test
  public void test2()
      throws FederateNotExecutionMember,
          NotConnected,
          RTIinternalError,
          AttributeNotDefined,
          ObjectClassNotDefined,
          ObjectClassNotPublished,
          SaveInProgress,
          RestoreInProgress,
          ObjectInstanceNotKnown,
          ObjectEncodingError,
          ObjectDecodingError {

    ObjectClassManager ocm =
        new ObjectClassManager(rtiamb, accessorFactory, encoderFactory, modules);

    ObjectInstanceManager oim = new ObjectInstanceManager(properties);

    ObjectClass oc1 = ocm.create(Object.class);
    oc1.addPublications();
    oc1.addSubscriptions();

    Object object1 = objectFactory.createObject(Object.class);
    ObjectInstanceHandle instanceHandle1 = rtiamb.registerObjectInstance(oc1.getClassHandle());
    ObjectInstance oi = oim.create(oc1, instanceHandle1, object1, "p1");
    AttributeHandleValueMap map = oi.serialize();

    Object object2 = objectFactory.createObject(Object.class);
    ObjectClass oc2 = ocm.getObjectClassIfExists(objectFactory.getObjectClass(object1));
    ObjectInstanceHandle instanceHandle2 = rtiamb.registerObjectInstance(oc2.getClassHandle());
    ObjectInstance oi2 = oim.create(oc2, instanceHandle2, object2, "p2");
    Set<OOattribute> attribuetSet2 = oi2.deserialize(map);

    Assertions.assertEquals(0, attribuetSet2.size());
  }
}
