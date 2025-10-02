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
}
