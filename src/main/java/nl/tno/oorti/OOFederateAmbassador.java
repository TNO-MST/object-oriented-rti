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
 * methods to handle Java Beans.
 *
 * @author bergtwvd
 */
public interface OOFederateAmbassador extends FederateAmbassador {

  ////////////////////////////////
  // Object Management Services //
  ////////////////////////////////

  void removeObjectInstance(
      Object theObject,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      LogicalTime theTime,
      OrderType receivedOrdering,
      MessageRetractionHandle retractionHandle,
      SupplementalRemoveInfo removeInfo)
      throws FederateInternalError;

  void removeObjectInstance(
      Object theObject,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      LogicalTime theTime,
      OrderType receivedOrdering,
      SupplementalRemoveInfo removeInfo)
      throws FederateInternalError;

  void removeObjectInstance(
      Object theObject,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      SupplementalRemoveInfo removeInfo)
      throws FederateInternalError;

  void receiveInteraction(
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

  void receiveInteraction(
      Object theInteraction,
      Set<OOparameter> theParameters,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      TransportationTypeHandle theTransport,
      LogicalTime theTime,
      OrderType receivedOrdering,
      SupplementalReceiveInfo receiveInfo)
      throws FederateInternalError;

  void receiveInteraction(
      Object theInteraction,
      Set<OOparameter> theParameters,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      TransportationTypeHandle theTransport,
      SupplementalReceiveInfo receiveInfo)
      throws FederateInternalError;

  void reflectAttributeValues(
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

  void reflectAttributeValues(
      Object theObject,
      Set<OOattribute> theAttributes,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      TransportationTypeHandle theTransport,
      LogicalTime theTime,
      OrderType receivedOrdering,
      SupplementalReflectInfo reflectInfo)
      throws FederateInternalError;

  void reflectAttributeValues(
      Object theObject,
      Set<OOattribute> theAttributes,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      TransportationTypeHandle theTransport,
      SupplementalReflectInfo reflectInfo)
      throws FederateInternalError;

  void discoverObjectInstance(
      Object theObject, String theObjectName, FederateHandle producingFederate)
      throws FederateInternalError;

  void discoverObjectInstance(Object theObject, String theObjectName) throws FederateInternalError;

  void provideAttributeValueUpdate(
      Object theObject, Set<OOattribute> theAttributes, byte[] userSuppliedTag)
      throws FederateInternalError;

  void attributesInScope(Object theObject, Set<OOattribute> theAttributes)
      throws FederateInternalError;

  void attributesOutOfScope(Object theObject, Set<OOattribute> theAttributes)
      throws FederateInternalError;

  void turnUpdatesOnForObjectInstance(Object theObject, Set<OOattribute> theAttributes)
      throws FederateInternalError;

  void turnUpdatesOnForObjectInstance(
      Object theObject, Set<OOattribute> theAttributes, String updateRateDesignator)
      throws FederateInternalError;

  void turnUpdatesOffForObjectInstance(Object theObject, Set<OOattribute> theAttributes)
      throws FederateInternalError;

  void confirmAttributeTransportationTypeChange(
      Object theObject, Set<OOattribute> theAttributes, TransportationTypeHandle theTransportation)
      throws FederateInternalError;

  void reportAttributeTransportationType(
      Object theObject, OOattribute theAttribute, TransportationTypeHandle theTransportation)
      throws FederateInternalError;

  void confirmInteractionTransportationTypeChange(
      Class clazz, TransportationTypeHandle theTransportation) throws FederateInternalError;

  void reportInteractionTransportationType(
      FederateHandle theFederate, Class clazz, TransportationTypeHandle theTransportation)
      throws FederateInternalError;

  ///////////////////////////////////
  // Ownership Management Services //
  ///////////////////////////////////

  void requestAttributeOwnershipAssumption(
      Object theObject, Set<OOattribute> offeredAttributes, byte[] userSuppliedTag)
      throws FederateInternalError;

  void requestDivestitureConfirmation(Object theObject, Set<OOattribute> offeredAttributes)
      throws FederateInternalError;

  void attributeOwnershipAcquisitionNotification(
      Object theObject, Set<OOattribute> securedAttributes, byte[] userSuppliedTag)
      throws FederateInternalError;

  void attributeOwnershipUnavailable(Object theObject, Set<OOattribute> theAttributes)
      throws FederateInternalError;

  void requestAttributeOwnershipRelease(
      Object theObject, Set<OOattribute> candidateAttributes, byte[] userSuppliedTag)
      throws FederateInternalError;

  void confirmAttributeOwnershipAcquisitionCancellation(
      Object theObject, Set<OOattribute> theAttributes) throws FederateInternalError;

  void informAttributeOwnership(Object theObject, OOattribute theAttribute, FederateHandle theOwner)
      throws FederateInternalError;

  void attributeIsNotOwned(Object theObject, OOattribute theAttribute) throws FederateInternalError;

  void attributeIsOwnedByRTI(Object theObject, OOattribute theAttribute)
      throws FederateInternalError;
}
