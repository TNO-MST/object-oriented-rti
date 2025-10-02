package nl.tno.oorti.test.serializer;

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
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.oorti.DefaultOOobjectFactory;
import nl.tno.oorti.OOobjectFactory;
import nl.tno.oorti.OOproperties;
import nl.tno.oorti.impl.serializer.DeserializedObjectData;
import nl.tno.oorti.impl.serializer.ObjectClass;
import nl.tno.oorti.impl.serializer.ObjectInstance;
import nl.tno.oorti.impl.serializer.SerializedObjectData;
import nl.tno.oorti.impl.serializer.Serializer;
import nl.tno.oorti.test.serializer.hla.rti1516e.MyRTIambassador;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author bergtwvd
 */
public class TestSerializerObject extends NullFederateAmbassador {

	// the RTI Ambassador
	static RTIambassador rtiamb;
	static ObjectModelType[] modules;

	@BeforeAll
	public static void setUpClass() throws IOException {
		rtiamb = new MyRTIambassador();

		URL[] foms = new URL[]{
			TestSerializerObject.class.getResource("/foms/Person.xml"),
			TestSerializerObject.class.getResource("/foms/HLAstandardMIM.xml")
		};

		modules = new ObjectModelType[foms.length];
		for (int i = 0; i < foms.length; i++) {
			modules[i] = OmtFunctions.readOmt(foms[i]);
		}
	}

	@Test
	public void test1() throws FederateNotExecutionMember, NotConnected, RTIinternalError, AttributeNotDefined, ObjectClassNotDefined, ObjectClassNotPublished, SaveInProgress, RestoreInProgress, ObjectInstanceNotKnown  {

		OOobjectFactory objectFactory = new DefaultOOobjectFactory();

		Serializer ser = new Serializer(rtiamb, modules, objectFactory, new OOproperties());

		Person person1 = new Person();
		person1.setName("MyName");
		person1.setAge(20);

		ObjectClass oc1 = ser.addObjectClass(true, ser.createObjectClass(person1.getClass()));
		ObjectInstanceHandle instanceHandle1 = rtiamb.registerObjectInstance(oc1.getClassHandle());
		ObjectInstance oi = ser.createObjectInstance(oc1, instanceHandle1, person1, "p1");
		SerializedObjectData serializedData = ser.serializeObject(person1, null);

		Person person2 = new Person();
		ObjectClass oc2 = ser.getObjectClassIfExists(true, person1.getClass());
		ObjectInstanceHandle instanceHandle2 = rtiamb.registerObjectInstance(oc2.getClassHandle());
		ObjectInstance oi2 = ser.createObjectInstance(oc2, instanceHandle2, person2, "p2");
		DeserializedObjectData deserializedData2 = ser.deserializeObject(oi2, serializedData.getAttributeValueMap(), null);
		person2 = (Person) deserializedData2.getTheObject();
		Assertions.assertNotNull(person2);
		Assertions.assertEquals(person1.getName(), person2.getName());
		Assertions.assertEquals(person1.getAge(), person2.getAge());
		Assertions.assertEquals(2, deserializedData2.getAttributeSet().size());

		// use an existing object to let the serializer re-populate the object
		Person person3 = new Person();
		person3.setName("aaa");
		person3.setAge(111);

		ObjectClass oc3 = ser.getObjectClassIfExists(true, person1.getClass());
		ObjectInstanceHandle instanceHandle3 = rtiamb.registerObjectInstance(oc2.getClassHandle());
		ObjectInstance oi3 = ser.createObjectInstance(oc3, instanceHandle3, person3, "p3");
		DeserializedObjectData deserializedData3 = ser.deserializeObject(oi3, serializedData.getAttributeValueMap(), null);
		Assertions.assertEquals(person3, deserializedData3.getTheObject());
		Assertions.assertEquals(person1.getName(), person3.getName());
		Assertions.assertEquals(person1.getAge(), person3.getAge());
		Assertions.assertEquals(2, deserializedData3.getAttributeSet().size());
	}

	// Simple test for using Object and HLAobjectRoot
	@Test
	public void test2() throws FederateNotExecutionMember, NotConnected, RTIinternalError, AttributeNotDefined, ObjectClassNotDefined, ObjectClassNotPublished, SaveInProgress, RestoreInProgress, ObjectInstanceNotKnown {

		OOobjectFactory objectFactory = new DefaultOOobjectFactory();

		Serializer ser = new Serializer(rtiamb, modules, objectFactory, new OOproperties());

		Object object1 = new Object();

		ObjectClass oc1 = ser.addObjectClass(true, ser.createObjectClass(object1.getClass()));
		ObjectInstanceHandle instanceHandle1 = rtiamb.registerObjectInstance(oc1.getClassHandle());
		ObjectInstance oi = ser.createObjectInstance(oc1, instanceHandle1, object1, "p1");
		SerializedObjectData serializedData = ser.serializeObject(object1, null);

		Object object2 = objectFactory.createObject(Object.class);
		ObjectClass oc2 = ser.getObjectClassIfExists(true, object1.getClass());
		ObjectInstanceHandle instanceHandle2 = rtiamb.registerObjectInstance(oc2.getClassHandle());
		ObjectInstance oi2 = ser.createObjectInstance(oc2, instanceHandle2, object2, "p2");
		DeserializedObjectData deserializedData2 = ser.deserializeObject(oi2, serializedData.getAttributeValueMap(), null);
		object2 = (Object) deserializedData2.getTheObject();
		Assertions.assertNotNull(object2);
		Assertions.assertEquals(0, deserializedData2.getAttributeSet().size());
	}

}
