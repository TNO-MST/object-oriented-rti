package nl.tno.oorti;

import hla.rti1516e.FederateHandle;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.MessageRetractionHandle;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.OrderType;
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.exceptions.FederateInternalError;
import java.util.Set;

/**
 * The NullOOFederateAmbassador provides a null implementation of the OOFederateAmbassador
 * interface. That is, all interface methods have an empty default implementation that can be
 * overridden in derived classes.
 *
 * @author bergtwvd
 */
public class NullOOFederateAmbassador extends NullFederateAmbassador
    implements OOFederateAmbassador {

  ////////////////////////////////
  // Object Management Services //
  ////////////////////////////////

  @Override
  public void removeObjectInstance(
      Object theObject,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      LogicalTime theTime,
      OrderType receivedOrdering,
      MessageRetractionHandle retractionHandle,
      SupplementalRemoveInfo removeInfo)
      throws FederateInternalError {}

  @Override
  public void removeObjectInstance(
      Object theObject,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      LogicalTime theTime,
      OrderType receivedOrdering,
      SupplementalRemoveInfo removeInfo)
      throws FederateInternalError {}

  @Override
  public void removeObjectInstance(
      Object theObject,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      SupplementalRemoveInfo removeInfo)
      throws FederateInternalError {}

  @Override
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
      throws FederateInternalError {}

  @Override
  public void reflectAttributeValues(
      Object theObject,
      Set<OOattribute> theAttributes,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      TransportationTypeHandle theTransport,
      LogicalTime theTime,
      OrderType receivedOrdering,
      SupplementalReflectInfo reflectInfo)
      throws FederateInternalError {}

  @Override
  public void reflectAttributeValues(
      Object theObject,
      Set<OOattribute> theAttributes,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      TransportationTypeHandle theTransport,
      SupplementalReflectInfo reflectInfo)
      throws FederateInternalError {}

  @Override
  public void discoverObjectInstance(
      Object theObject, String objectName, FederateHandle producingFederate)
      throws FederateInternalError {}

  @Override
  public void discoverObjectInstance(Object theObject, String objectName)
      throws FederateInternalError {}

  @Override
  public void provideAttributeValueUpdate(
      Object theObject, Set<OOattribute> theAttributes, byte[] userSuppliedTag)
      throws FederateInternalError {}

  @Override
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
      throws FederateInternalError {}

  @Override
  public void receiveInteraction(
      Object theInteraction,
      Set<OOparameter> theParameters,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      TransportationTypeHandle theTransport,
      LogicalTime theTime,
      OrderType receivedOrdering,
      SupplementalReceiveInfo receiveInfo)
      throws FederateInternalError {}

  @Override
  public void receiveInteraction(
      Object theInteraction,
      Set<OOparameter> theParameters,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      TransportationTypeHandle theTransport,
      SupplementalReceiveInfo receiveInfo)
      throws FederateInternalError {}

  ///////////////////////////////////
  // Ownership Management Services //
  ///////////////////////////////////

  @Override
  public void requestAttributeOwnershipAssumption(
      Object theObject, Set<OOattribute> offeredAttributes, byte[] userSuppliedTag)
      throws FederateInternalError {}

  @Override
  public void requestDivestitureConfirmation(Object theObject, Set<OOattribute> offeredAttributes)
      throws FederateInternalError {}

  @Override
  public void attributeOwnershipAcquisitionNotification(
      Object theObject, Set<OOattribute> securedAttributes, byte[] userSuppliedTag)
      throws FederateInternalError {}

  @Override
  public void attributeOwnershipUnavailable(Object theObject, Set<OOattribute> theAttributes)
      throws FederateInternalError {}

  @Override
  public void requestAttributeOwnershipRelease(
      Object theObject, Set<OOattribute> candidateAttributes, byte[] userSuppliedTag)
      throws FederateInternalError {}

  @Override
  public void confirmAttributeOwnershipAcquisitionCancellation(
      Object theObject, Set<OOattribute> theAttributes) throws FederateInternalError {}

  @Override
  public void informAttributeOwnership(
      Object theObject, OOattribute theAttribute, FederateHandle theOwner)
      throws FederateInternalError {}

  @Override
  public void attributeIsNotOwned(Object theObject, OOattribute theAttribute)
      throws FederateInternalError {}

  @Override
  public void attributeIsOwnedByRTI(Object theObject, OOattribute theAttribute)
      throws FederateInternalError {}
}
