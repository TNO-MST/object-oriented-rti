package nl.tno.oorti;

import hla.rti1516e.FederateAmbassador;
import hla.rti1516e.FederateHandle;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.MessageRetractionHandle;
import hla.rti1516e.OrderType;
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.exceptions.FederateInternalError;
import java.util.Set;

/**
 * The OOFederateAmbassador defines the interface that a federate must implement for receiving
 * callbacks from the RTI. This interface extends to FederateAmbassador interface with additional
 * methods to handle Java Bean classes.
 *
 * @author bergtwvd
 */
public interface OOFederateAmbassador extends FederateAmbassador {

  public void removeObjectInstance(
      Object theObject,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      LogicalTime theTime,
      OrderType receivedOrdering,
      MessageRetractionHandle retractionHandle,
      SupplementalRemoveInfo removeInfo)
      throws FederateInternalError;

  public void removeObjectInstance(
      Object theObject,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      LogicalTime theTime,
      OrderType receivedOrdering,
      SupplementalRemoveInfo removeInfo)
      throws FederateInternalError;

  public void removeObjectInstance(
      Object theObject,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      SupplementalRemoveInfo removeInfo)
      throws FederateInternalError;

  public void receiveInteraction(
      Object theInteraction,
      Set<OOparameter> theParameters,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      TransportationTypeHandle theTransport,
      LogicalTime theTime,
      OrderType receivedOrdering,
      MessageRetractionHandle retractionHandle,
      SupplementalReceiveInfo receiveInfo)
      throws FederateInternalError;

  public void receiveInteraction(
      Object theInteraction,
      Set<OOparameter> theParameters,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      TransportationTypeHandle theTransport,
      LogicalTime theTime,
      OrderType receivedOrdering,
      SupplementalReceiveInfo receiveInfo)
      throws FederateInternalError;

  public void receiveInteraction(
      Object theInteraction,
      Set<OOparameter> theParameters,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      TransportationTypeHandle theTransport,
      SupplementalReceiveInfo receiveInfo)
      throws FederateInternalError;

  public void reflectAttributeValues(
      Object theObject,
      Set<OOattribute> theAttributes,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      TransportationTypeHandle theTransport,
      LogicalTime theTime,
      OrderType receivedOrdering,
      MessageRetractionHandle retractionHandle,
      SupplementalReflectInfo reflectInfo)
      throws FederateInternalError;

  public void reflectAttributeValues(
      Object theObject,
      Set<OOattribute> theAttributes,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      TransportationTypeHandle theTransport,
      LogicalTime theTime,
      OrderType receivedOrdering,
      SupplementalReflectInfo reflectInfo)
      throws FederateInternalError;

  public void reflectAttributeValues(
      Object theObject,
      Set<OOattribute> theAttributes,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      TransportationTypeHandle theTransport,
      SupplementalReflectInfo reflectInfo)
      throws FederateInternalError;

  public void discoverObjectInstance(
      Object theObject, String theObjectName, FederateHandle producingFederate)
      throws FederateInternalError;

  public void discoverObjectInstance(Object theObject, String theObjectName)
      throws FederateInternalError;

  public void provideAttributeValueUpdate(
      Object theObject, Set<OOattribute> theAttributes, byte[] userSuppliedTag)
      throws FederateInternalError;
}
