package nl.tno.oorti.example.handler;

import hla.rti1516e.CallbackModel;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.MessageRetractionHandle;
import hla.rti1516e.OrderType;
import hla.rti1516e.ResignAction;
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.exceptions.FederateInternalError;
import hla.rti1516e.exceptions.FederatesCurrentlyJoined;
import hla.rti1516e.exceptions.FederationExecutionAlreadyExists;
import hla.rti1516e.exceptions.RTIexception;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.tno.oorti.CookieFunctions;
import nl.tno.oorti.test.netn4.datatypes.EntityTypeStruct;
import nl.tno.oorti.test.netn4.datatypes.MarkingEncodingEnum8;
import nl.tno.oorti.test.netn4.datatypes.MarkingStruct;
import nl.tno.oorti.test.netn4.objects.PhysicalEntity;
import nl.tno.oorti.NullOOFederateAmbassador;
import nl.tno.oorti.OORTIambassador;
import nl.tno.oorti.OORTIfactory;
import nl.tno.oorti.OOattribute;

/**
 *
 * @author bergtwvd
 */
public class ExampleApplication extends NullOOFederateAmbassador implements Runnable {

	final OORTIambassador oortiamb;
	final boolean sender;

	ExampleApplication(boolean sender) throws RTIexception {
		this.oortiamb = new OORTIfactory().getRtiAmbassador();
		this.sender = sender;
	}

	// start the processing
	void start() throws RTIexception {

		// first connect to the RTI and create/join the federation
		oortiamb.connect(this, CallbackModel.HLA_EVOKED);
		try {
			oortiamb.createFederationExecution("federationName", new URL[]{this.getClass().getResource("/foms/RPR_FOM_v2.0_1516-2010.xml")});
			oortiamb.joinFederationExecution("federateType", "federationName");
		} catch (FederationExecutionAlreadyExists ex) {
			oortiamb.joinFederationExecution("federateType", "federationName");
		}

		if (sender) {
			// publish the attributes identified by the provided String objects;
			// the "toString" method of each object is used to identify the attribute to be published
			Set<OOattribute> pubAttributes = oortiamb.publishObjectClass(PhysicalEntity.class, new HashSet<>(Arrays.asList("Marking", "EntityType")));

			// get the OOattributes for later reference
			OOattribute markingAttribute = CookieFunctions.getAttribute(pubAttributes, "Marking");
			OOattribute entityTypeAttribute = CookieFunctions.getAttribute(pubAttributes, "EntityType");

			// create a PhysicalEntity object and register the object
			PhysicalEntity pe = new PhysicalEntity();
			oortiamb.registerObjectInstance(pe);

			// update the following object attributes a few times
			pe.setEntityType(new EntityTypeStruct());
			pe.setMarking(new MarkingStruct());

			for (int i = 0; i < 60; i++) {
				// set of attribues that has been updated
				Set<OOattribute> theAttributes = new HashSet<>();

				if (i % 2 == 0) {
					// update marking, which must be exactly 11 bytes (otherwise the encoder throws an exception)		
					byte[] bytes = Arrays.copyOf(Integer.toString(i).getBytes(), 11);
					pe.getMarking().setMarkingData(bytes);
					pe.getMarking().setMarkingEncodingType(MarkingEncodingEnum8.ASCII);

					// indicate that we have updated this attribute
					theAttributes.add(markingAttribute);
				}

				if (i % 3 == 0) {
					// update entity type		
					pe.getEntityType().setDomain((byte) (i % 256));

					// indicate that we have updated this attribute
					theAttributes.add(entityTypeAttribute);
				}

				// provide the updates to the RTI; also provide a user supplied tag
				oortiamb.updateAttributeValues(pe, theAttributes, Integer.toString(i).getBytes());

				// poll one second for callbacks
				oortiamb.evokeMultipleCallbacks(1, 1);
			}

			oortiamb.deleteObjectInstance(pe, null);
		} else {

			// create handler obects for the attributes of interest
			MarkingAttributeHandler markingAttributeHandler = new MarkingAttributeHandler();
			EntityTypeAttributeHandler entityTypeAttributeHandler = new EntityTypeAttributeHandler();

			// subscribe using the provided handler objects; the "toString" method of each
			// object is used to identify the attribute to subscribe to
			Set<OOattribute> subAttributes = oortiamb.subscribeObjectClass(PhysicalEntity.class, new HashSet<>(Arrays.asList(markingAttributeHandler, entityTypeAttributeHandler)));

			// just loop for some time
			for (int i = 0; i < 60; i++) {
				oortiamb.evokeMultipleCallbacks(1, 1);
			}
		}

		// we are done
		oortiamb.resignFederationExecution(ResignAction.NO_ACTION);
		try {
			oortiamb.destroyFederationExecution("federationName");
		} catch (FederatesCurrentlyJoined ex) {
		}

		oortiamb.disconnect();
	}

	/*
	** discoverObjectInstance callbacks
	 */
	@Override
	public void discoverObjectInstance(Object theObject, String objectName) throws FederateInternalError {
		System.out.println("Discovered object instance=" + theObject.toString() + " name=" + objectName);
	}

	/*
	** removeObjectInstance callbacks
	 */
	@Override
	public void removeObjectInstance(Object theObject, byte[] userSuppliedTag, OrderType sentOrdering, LogicalTime theTime, OrderType receivedOrdering, MessageRetractionHandle retractionHandle, SupplementalRemoveInfo removeInfo) throws FederateInternalError {
		removeObjectInstance(theObject, userSuppliedTag);
	}

	@Override
	public void removeObjectInstance(Object theObject, byte[] userSuppliedTag, OrderType sentOrdering, LogicalTime theTime, OrderType receivedOrdering, SupplementalRemoveInfo removeInfo) throws FederateInternalError {
		removeObjectInstance(theObject, userSuppliedTag);
	}

	@Override
	public void removeObjectInstance(Object theObject, byte[] userSuppliedTag, OrderType sentOrdering, SupplementalRemoveInfo removeInfo) throws FederateInternalError {
		removeObjectInstance(theObject, userSuppliedTag);
	}

	void removeObjectInstance(Object theObject, byte[] userSuppliedTag) {
		System.out.println("Removed object instance=" + theObject.toString());
	}

	/*
	** reflectAttributeValues callbacks
	 */
	@Override
	public void reflectAttributeValues(Object theObject, Set<OOattribute> theAttributes, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, LogicalTime theTime, OrderType receivedOrdering, MessageRetractionHandle retractionHandle, SupplementalReflectInfo reflectInfo) throws FederateInternalError {
		reflectAttributeValues(theObject, theAttributes, userSuppliedTag);
	}

	@Override
	public void reflectAttributeValues(Object theObject, Set<OOattribute> theAttributes, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, LogicalTime theTime, OrderType receivedOrdering, SupplementalReflectInfo reflectInfo) throws FederateInternalError {
		reflectAttributeValues(theObject, theAttributes, userSuppliedTag);
	}

	@Override
	public void reflectAttributeValues(Object theObject, Set<OOattribute> theAttributes, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, SupplementalReflectInfo reflectInfo) throws FederateInternalError {
		reflectAttributeValues(theObject, theAttributes, userSuppliedTag);
	}

	void reflectAttributeValues(Object theObject, Set<OOattribute> theAttributes, byte[] userSuppliedTag) {
		System.out.println(">> userSuppliedTag=" + new String(userSuppliedTag));

		for (OOattribute attribute : theAttributes) {
			((AttributeHandler) attribute.getCookie()).handle(theObject);
		}
	}

	@Override
	public void run() {
		try {
			this.start();
		} catch (RTIexception ex) {
			Logger.getLogger(ExampleApplication.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Main entry point to the example.Two applications are created, one that
	 * sends updates and one that receives updates. The example runs for about
	 * one minute wall clock time.
	 *
	 * @param args
	 * @throws RTIexception
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws RTIexception, InterruptedException {

		ExampleApplication app1 = new ExampleApplication(true);
		ExampleApplication app2 = new ExampleApplication(false);

		Thread thread1 = new Thread(app1);
		thread1.start();

		Thread thread2 = new Thread(app2);
		thread2.start();

		thread1.join();
		thread2.join();
	}

}
