package nl.tno.oorti.test.serializer;

import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.exceptions.RTIexception;
import hla.rti1516e.exceptions.RTIinternalError;
import java.io.IOException;
import java.net.URL;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.oorti.DefaultOOobjectFactory;
import nl.tno.oorti.OOobjectFactory;
import nl.tno.oorti.OOproperties;
import nl.tno.oorti.impl.serializer.DeserializedInteractionData;
import nl.tno.oorti.impl.serializer.InteractionClass;
import nl.tno.oorti.impl.serializer.SerializedInteractionData;
import nl.tno.oorti.impl.serializer.Serializer;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;
import nl.tno.oorti.test.serializer.hla.rti1516e.MyRTIambassador;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author bergtwvd
 */
public class TestSerializerInteraction extends NullFederateAmbassador {

	// the RTI Ambassador
	static RTIambassador rtiamb;
	static ObjectModelType[] modules;

	@BeforeAll
	public static void setUpClass() throws OOcodecException, IOException {
		rtiamb = new MyRTIambassador();

		URL[] foms = new URL[]{
			TestSerializerInteraction.class.getResource("/foms/Message.xml"),
			TestSerializerInteraction.class.getResource("/foms/HLAstandardMIM.xml")
		};

		modules = new ObjectModelType[foms.length];
		for (int i = 0; i < foms.length; i++) {
			modules[i] = OmtFunctions.readOmt(foms[i]);
		}
	}

	@Test
	public void test1() throws RTIexception {
		OOobjectFactory objectFactory = new DefaultOOobjectFactory();

		Serializer ser1 = new Serializer(rtiamb, modules, objectFactory, new OOproperties());
		Serializer ser2 = new Serializer(rtiamb, modules, objectFactory, new OOproperties());

		Message theMessage1 = new Message();
		theMessage1.setContents("hello");

		InteractionClass ic1 = ser1.addInteractionClass(true, ser1.createInteractionClass(Message.class));
		InteractionClass ic2 = ser2.addInteractionClass(true, ser2.createInteractionClass(Message.class));

		SerializedInteractionData serializedData = ser1.serializeInteraction(theMessage1, null);
		DeserializedInteractionData deserializedData = ser2.deserializeInteraction(ic2, serializedData.getParameterValueMap(), null);

		Message theMessage2 = (Message) deserializedData.getTheInteraction();
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
				throw new RTIinternalError("Expected class SubMessage but got " + object.getClass().getSimpleName());
			}
		}
	}

	/**
	 * Test the processing of proprietary Java Bean subclasses with properties
	 * that are not in the FOM.
	 *
	 * @throws hla.rti1516e.exceptions.RTIexception
	 */
	@Test
	public void test2() throws RTIexception {

		OOobjectFactory objectFactory = new MyOOobjectFactory();

		Serializer ser1 = new Serializer(rtiamb, modules, objectFactory, new OOproperties());
		Serializer ser2 = new Serializer(rtiamb, modules, objectFactory, new OOproperties());

		InteractionClass ic1 = ser1.addInteractionClass(true, ser1.createInteractionClass(Message.class));
		InteractionClass ic2 = ser2.addInteractionClass(true, ser2.createInteractionClass(Message.class));

		Message theMessage1 = objectFactory.createInteraction(Message.class);
		theMessage1.setContents("hello");

		SerializedInteractionData serializedData = ser1.serializeInteraction(theMessage1, null);
		DeserializedInteractionData deserializedData = ser2.deserializeInteraction(ic2, serializedData.getParameterValueMap(), null);

		Message theMessage2 = (Message) deserializedData.getTheInteraction();

		Assertions.assertEquals(theMessage1.getClass(), SubMessage.class);
		Assertions.assertEquals(theMessage2.getClass(), SubMessage.class);
		Assertions.assertEquals(theMessage1.getContents(), theMessage2.getContents());
	}

}
