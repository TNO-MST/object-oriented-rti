package nl.tno.oorti.example.helloworld;

import hla.rti1516e.*;
import hla.rti1516e.exceptions.*;
import java.net.URL;
import java.util.Set;
import nl.tno.oorti.NullOOFederateAmbassador;
import nl.tno.oorti.OORTIambassador;
import nl.tno.oorti.OORTIfactory;
import nl.tno.oorti.OOparameter;

public class HelloWorld extends NullOOFederateAmbassador {

	public void demo() throws RTIexception {
		// create the OORTI Ambassador
		OORTIambassador rtiamb = new OORTIfactory().getRtiAmbassador();

		// connect to the RTI in evoked mode
		rtiamb.connect(this, CallbackModel.HLA_EVOKED);

		// setup module URL
		URL[] modules = new URL[]{this.getClass().getResource("/foms/Message.xml")};

		// Attempt to create a new federation
		try {
			rtiamb.createFederationExecution("federationName", modules);
		} catch (FederationExecutionAlreadyExists ex) {
		}

		// join the federation execution
		rtiamb.joinFederationExecution("federateType", "federationName", modules);

		// publish and subscribe to the class of interest
		rtiamb.publishInteractionClass(Message.class);
		rtiamb.subscribeInteractionClass(Message.class);

		// send and receive a message a few times
		for (int i = 0; i < 60; i++) {
			Message msg = new Message();
			msg.setContents("Message" + i);
			rtiamb.sendInteraction(msg, null);
			System.out.println("Sent: " + msg.getContents());
			rtiamb.evokeCallback(1);
		}

		// resign and disconnect
		rtiamb.resignFederationExecution(ResignAction.NO_ACTION);
		rtiamb.disconnect();
	}

	@Override
	public void receiveInteraction(
			Object theInteraction,
			Set<OOparameter> theParameters,
			byte[] userSuppliedTag,
			OrderType sentOrdering,
			TransportationTypeHandle theTransport,
			SupplementalReceiveInfo receiveInfo)
			throws FederateInternalError {
		// print the message received
		Message theMessage = (Message) theInteraction;
		System.out.println("Received: " + theMessage.getContents());
	}

	public static void main(String[] args) throws RTIexception {
		new HelloWorld().demo();
	}
}
