package nl.tno.oorti.impl;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.FederateAmbassador;
import hla.rti1516e.FederateHandle;
import hla.rti1516e.FederateHandleSaveStatusPair;
import hla.rti1516e.FederateHandleSet;
import hla.rti1516e.FederateRestoreStatus;
import hla.rti1516e.FederationExecutionInformationSet;
import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.MessageRetractionHandle;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.OrderType;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.RestoreFailureReason;
import hla.rti1516e.SaveFailureReason;
import hla.rti1516e.SynchronizationPointFailureReason;
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.FederateInternalError;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InteractionClassNotDefined;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import nl.tno.oorti.OOFederateAmbassador;
import nl.tno.oorti.OOattribute;
import nl.tno.oorti.OOparameter;
import nl.tno.oorti.impl.mim.objects.HLAfederation;

/**
 * @author bergtwvd
 */
public class OOFederateAmbassadorImpl implements FederateAmbassador {

  final OORTIambassadorImpl rtiamb;
  final OOFederateAmbassador federateReference;
  final boolean isCheckInitialState;

  // getting the FDD from the RTI is done in the initial state
  boolean isInInitialState;

  public OOFederateAmbassadorImpl(
      OORTIambassadorImpl rtiamb, OOFederateAmbassador federateReference) {
    this.rtiamb = rtiamb;
    this.federateReference = federateReference;
    this.isCheckInitialState = rtiamb.properties.isUseRtiForCurrentFdd();
    this.isInInitialState = rtiamb.properties.isUseRtiForCurrentFdd();
  }

  ////////////////////////////////////
  // Federation Management Services //
  ////////////////////////////////////
  @Override
  public void connectionLost(String faultDescription) throws FederateInternalError {
    federateReference.connectionLost(faultDescription);
  }

  @Override
  public void reportFederationExecutions(
      FederationExecutionInformationSet theFederationExecutionInformationSet)
      throws FederateInternalError {
    federateReference.reportFederationExecutions(theFederationExecutionInformationSet);
  }

  @Override
  public void synchronizationPointRegistrationSucceeded(String synchronizationPointLabel)
      throws FederateInternalError {
    federateReference.synchronizationPointRegistrationSucceeded(synchronizationPointLabel);
  }

  @Override
  public void synchronizationPointRegistrationFailed(
      String synchronizationPointLabel, SynchronizationPointFailureReason reason)
      throws FederateInternalError {
    federateReference.synchronizationPointRegistrationFailed(synchronizationPointLabel, reason);
  }

  @Override
  public void announceSynchronizationPoint(String synchronizationPointLabel, byte[] userSuppliedTag)
      throws FederateInternalError {
    federateReference.announceSynchronizationPoint(synchronizationPointLabel, userSuppliedTag);
  }

  @Override
  public void federationSynchronized(
      String synchronizationPointLabel, FederateHandleSet failedToSyncSet)
      throws FederateInternalError {
    federateReference.federationSynchronized(synchronizationPointLabel, failedToSyncSet);
  }

  @Override
  public void initiateFederateSave(String label) throws FederateInternalError {
    federateReference.initiateFederateSave(label);
  }

  @Override
  public void initiateFederateSave(String label, LogicalTime time) throws FederateInternalError {
    federateReference.initiateFederateSave(label, time);
  }

  @Override
  public void federationSaved() throws FederateInternalError {
    federateReference.federationSaved();
  }

  @Override
  public void federationNotSaved(SaveFailureReason reason) throws FederateInternalError {
    federateReference.federationNotSaved(reason);
  }

  @Override
  public void federationSaveStatusResponse(FederateHandleSaveStatusPair[] response)
      throws FederateInternalError {
    federateReference.federationSaveStatusResponse(response);
  }

  @Override
  public void requestFederationRestoreSucceeded(String label) throws FederateInternalError {
    federateReference.requestFederationRestoreSucceeded(label);
  }

  @Override
  public void requestFederationRestoreFailed(String label) throws FederateInternalError {
    federateReference.requestFederationRestoreFailed(label);
  }

  @Override
  public void federationRestoreBegun() throws FederateInternalError {
    federateReference.federationRestoreBegun();
  }

  @Override
  public void initiateFederateRestore(
      String label, String federateName, FederateHandle federateHandle)
      throws FederateInternalError {
    federateReference.initiateFederateRestore(label, federateName, federateHandle);
  }

  @Override
  public void federationRestored() throws FederateInternalError {
    federateReference.federationRestored();
  }

  @Override
  public void federationNotRestored(RestoreFailureReason reason) throws FederateInternalError {
    federateReference.federationNotRestored(reason);
  }

  @Override
  public void federationRestoreStatusResponse(FederateRestoreStatus[] response)
      throws FederateInternalError {
    federateReference.federationRestoreStatusResponse(response);
  }

  /////////////////////////////////////
  // Declaration Management Services //
  /////////////////////////////////////
  @Override
  public void startRegistrationForObjectClass(ObjectClassHandle theClass)
      throws FederateInternalError {
    federateReference.startRegistrationForObjectClass(theClass);
  }

  @Override
  public void stopRegistrationForObjectClass(ObjectClassHandle theClass)
      throws FederateInternalError {
    federateReference.stopRegistrationForObjectClass(theClass);
  }

  @Override
  public void turnInteractionsOn(InteractionClassHandle theHandle) throws FederateInternalError {
    federateReference.turnInteractionsOn(theHandle);
  }

  @Override
  public void turnInteractionsOff(InteractionClassHandle theHandle) throws FederateInternalError {
    federateReference.turnInteractionsOff(theHandle);
  }

  ////////////////////////////////
  // Object Management Services //
  ////////////////////////////////
  @Override
  public void objectInstanceNameReservationSucceeded(String objectName)
      throws FederateInternalError {
    federateReference.objectInstanceNameReservationSucceeded(objectName);
  }

  @Override
  public void objectInstanceNameReservationFailed(String objectName) throws FederateInternalError {
    federateReference.objectInstanceNameReservationFailed(objectName);
  }

  @Override
  public void multipleObjectInstanceNameReservationSucceeded(Set<String> objectNames)
      throws FederateInternalError {
    federateReference.multipleObjectInstanceNameReservationSucceeded(objectNames);
  }

  @Override
  public void multipleObjectInstanceNameReservationFailed(Set<String> objectNames)
      throws FederateInternalError {
    federateReference.multipleObjectInstanceNameReservationFailed(objectNames);
  }

  @Override
  public void receiveInteraction(
      InteractionClassHandle classHandle,
      ParameterHandleValueMap theParameters,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      TransportationTypeHandle theTransport,
      LogicalTime theTime,
      OrderType receivedOrdering,
      MessageRetractionHandle retractionHandle,
      SupplementalReceiveInfo receiveInfo)
      throws FederateInternalError {

    InteractionClass ic = rtiamb.icm.getClassByHandle(classHandle);
    if (ic == null) {
      // if the interaction class was not registered use the regular call back
      federateReference.receiveInteraction(
          classHandle,
          theParameters,
          userSuppliedTag,
          sentOrdering,
          theTransport,
          theTime,
          receivedOrdering,
          retractionHandle,
          receiveInfo);
    } else {
      try {
        Object theInteraction = rtiamb.objectFactory.createInteraction(ic.getClazz());
        Set<OOparameter> parameterSet = ic.deserialize(theParameters, theInteraction);

        federateReference.receiveInteraction(
            theInteraction,
            parameterSet,
            userSuppliedTag,
            sentOrdering,
            theTransport,
            theTime,
            receivedOrdering,
            retractionHandle,
            receiveInfo);
      } catch (InteractionClassNotDefined | RTIinternalError ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void receiveInteraction(
      InteractionClassHandle classHandle,
      ParameterHandleValueMap theParameters,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      TransportationTypeHandle theTransport,
      LogicalTime theTime,
      OrderType receivedOrdering,
      SupplementalReceiveInfo receiveInfo)
      throws FederateInternalError {

    InteractionClass ic = rtiamb.icm.getClassByHandle(classHandle);
    if (ic == null) {
      // if the interaction class was not registered use the regular call back
      federateReference.receiveInteraction(
          classHandle,
          theParameters,
          userSuppliedTag,
          sentOrdering,
          theTransport,
          theTime,
          receivedOrdering,
          receiveInfo);
    } else {
      try {
        Object theInteraction = rtiamb.objectFactory.createInteraction(ic.getClazz());
        Set<OOparameter> parameterSet = ic.deserialize(theParameters, theInteraction);

        federateReference.receiveInteraction(
            theInteraction,
            parameterSet,
            userSuppliedTag,
            sentOrdering,
            theTransport,
            theTime,
            receivedOrdering,
            receiveInfo);
      } catch (InteractionClassNotDefined | RTIinternalError ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void receiveInteraction(
      InteractionClassHandle classHandle,
      ParameterHandleValueMap theParameters,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      TransportationTypeHandle theTransport,
      SupplementalReceiveInfo receiveInfo)
      throws FederateInternalError {

    InteractionClass ic = rtiamb.icm.getClassByHandle(classHandle);
    if (ic == null) {
      // if the interaction class was not registered use the regular call back
      federateReference.receiveInteraction(
          classHandle, theParameters, userSuppliedTag, sentOrdering, theTransport, receiveInfo);
    } else {
      try {
        Object theInteraction = rtiamb.objectFactory.createInteraction(ic.getClazz());
        Set<OOparameter> parameterSet = ic.deserialize(theParameters, theInteraction);

        federateReference.receiveInteraction(
            theInteraction, parameterSet, userSuppliedTag, sentOrdering, theTransport, receiveInfo);
      } catch (InteractionClassNotDefined | RTIinternalError ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  private void discoverHLAfederationInstance(
      ObjectInstanceHandle instanceHandle, ObjectClassHandle classHandle)
      throws FederateInternalError {
    
    // get the object class of the discovered instance
    ObjectClass oc = rtiamb.ocmMim.getClassByHandle(classHandle);

    // keep the instance handle for quick lookup later
    rtiamb.hlaFederationInstanceHandle = instanceHandle;

    try {
      // create an object instance with newInstance here, rather than going through the user
      // supplied objectFactory
      rtiamb.hlaFederation =
          (HLAfederation)
              rtiamb
                  .oim
                  .create(
                      oc,
                      instanceHandle,
                      oc.getClazz().getDeclaredConstructor().newInstance(),
                      rtiamb.getObjectInstanceName(instanceHandle))
                  .getObject();

      // request attribute values
      rtiamb.requestAttributeValueUpdate(instanceHandle, oc.createAttributeHandleSet(), null);
    } catch (AttributeNotDefined
        | FederateNotExecutionMember
        | NotConnected
        | ObjectClassNotDefined
        | ObjectInstanceNotKnown
        | RTIinternalError
        | RestoreInProgress
        | SaveInProgress
        | IllegalAccessException
        | IllegalArgumentException
        | InstantiationException
        | NoSuchMethodException
        | SecurityException
        | InvocationTargetException ex) {
      throw new FederateInternalError(ex.getMessage(), ex);
    }
  }

  @Override
  public void discoverObjectInstance(
      ObjectInstanceHandle instanceHandle,
      ObjectClassHandle classHandle,
      String theObjectName,
      FederateHandle producingFederate)
      throws FederateInternalError {
    
    if (this.isCheckInitialState && this.isInInitialState) {
      synchronized (this.rtiamb) {
        this.discoverHLAfederationInstance(instanceHandle, classHandle);
        this.rtiamb.notify();
      }
    } else {
      // if the object class was not registered use the regular call back
      ObjectClass oc = rtiamb.ocm.getClassByHandle(classHandle);
      if (oc == null) {
        federateReference.discoverObjectInstance(
            instanceHandle, classHandle, theObjectName, producingFederate);
      } else {
        try {
          Object theObject = rtiamb.objectFactory.createObject(oc.getClazz());
          ObjectInstance oi = rtiamb.oim.create(oc, instanceHandle, theObject, theObjectName);
          federateReference.discoverObjectInstance(
              oi.getObject(), theObjectName, producingFederate);
        } catch (ObjectClassNotDefined | RTIinternalError ex) {
          throw new FederateInternalError(ex.getMessage(), ex);
        }
      }
    }
  }

  @Override
  public void discoverObjectInstance(
      ObjectInstanceHandle instanceHandle, ObjectClassHandle classHandle, String theObjectName)
      throws FederateInternalError {
    
    if (this.isCheckInitialState && this.isInInitialState) {
      synchronized (this.rtiamb) {
        this.discoverHLAfederationInstance(instanceHandle, classHandle);
        this.rtiamb.notify();
      }
    } else {
      // if the object class was not registered use the regular call back
      ObjectClass oc = rtiamb.ocm.getClassByHandle(classHandle);
      if (oc == null) {
        federateReference.discoverObjectInstance(instanceHandle, classHandle, theObjectName);
      } else {
        try {
          Object theObject = rtiamb.objectFactory.createObject(oc.getClazz());
          ObjectInstance oi = rtiamb.oim.create(oc, instanceHandle, theObject, theObjectName);
          federateReference.discoverObjectInstance(oi.getObject(), theObjectName);
        } catch (ObjectClassNotDefined | RTIinternalError ex) {
          throw new FederateInternalError(ex.getMessage(), ex);
        }
      }
    }
  }

  private void reflectHLAfederationInstance(
      ObjectInstanceHandle instanceHandle, AttributeHandleValueMap theAttributes)
      throws FederateInternalError {
    try {
      rtiamb.oim.getObjectInstanceByHandle(instanceHandle).deserialize(theAttributes);
    } catch (RTIinternalError ex) {
      throw new FederateInternalError(ex.getMessage(), ex);
    }
  }

  @Override
  public void reflectAttributeValues(
      ObjectInstanceHandle instanceHandle,
      AttributeHandleValueMap theAttributes,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      TransportationTypeHandle theTransport,
      LogicalTime theTime,
      OrderType receivedOrdering,
      MessageRetractionHandle retractionHandle,
      SupplementalReflectInfo reflectInfo)
      throws FederateInternalError {
    
    if (this.isCheckInitialState && this.isInInitialState) {
      synchronized (this.rtiamb) {
        if (this.isInInitialState = this.rtiamb.hlaFederationInstanceHandle != null) {
          this.reflectHLAfederationInstance(instanceHandle, theAttributes);
          this.rtiamb.notify();
          return;
        }
      }
    }

    // if the object instance was not registered use the regular call back
    ObjectInstance oi = rtiamb.oim.getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.reflectAttributeValues(
          instanceHandle,
          theAttributes,
          userSuppliedTag,
          sentOrdering,
          theTransport,
          theTime,
          receivedOrdering,
          retractionHandle,
          reflectInfo);
    } else {
      try {
        Set<OOattribute> attributeSet = oi.deserialize(theAttributes);

        federateReference.reflectAttributeValues(
            oi.getObject(),
            attributeSet,
            userSuppliedTag,
            sentOrdering,
            theTransport,
            theTime,
            receivedOrdering,
            retractionHandle,
            reflectInfo);
      } catch (RTIinternalError ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void reflectAttributeValues(
      ObjectInstanceHandle instanceHandle,
      AttributeHandleValueMap theAttributes,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      TransportationTypeHandle theTransport,
      LogicalTime theTime,
      OrderType receivedOrdering,
      FederateAmbassador.SupplementalReflectInfo reflectInfo)
      throws FederateInternalError {
    
    if (this.isCheckInitialState && this.isInInitialState) {
      synchronized (this.rtiamb) {
        if (this.isInInitialState = this.rtiamb.hlaFederationInstanceHandle != null) {
          this.reflectHLAfederationInstance(instanceHandle, theAttributes);
          this.rtiamb.notify();
          return;
        }
      }
    }

    // if the object instance was not registered use the regular call back
    ObjectInstance oi = rtiamb.oim.getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.reflectAttributeValues(
          instanceHandle,
          theAttributes,
          userSuppliedTag,
          sentOrdering,
          theTransport,
          theTime,
          receivedOrdering,
          reflectInfo);
    } else {
      try {
        Set<OOattribute> attributeSet = oi.deserialize(theAttributes);

        federateReference.reflectAttributeValues(
            oi.getObject(),
            attributeSet,
            userSuppliedTag,
            sentOrdering,
            theTransport,
            theTime,
            receivedOrdering,
            reflectInfo);
      } catch (RTIinternalError ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void reflectAttributeValues(
      ObjectInstanceHandle instanceHandle,
      AttributeHandleValueMap theAttributes,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      TransportationTypeHandle theTransport,
      FederateAmbassador.SupplementalReflectInfo reflectInfo)
      throws FederateInternalError {
    
    if (this.isCheckInitialState && this.isInInitialState) {
      synchronized (this.rtiamb) {
        if (this.isInInitialState = this.rtiamb.hlaFederationInstanceHandle != null) {
          this.reflectHLAfederationInstance(instanceHandle, theAttributes);
          this.rtiamb.notify();
          return;
        }
      }
    }

    // if the object instance was not registered use the regular call back
    ObjectInstance oi = rtiamb.oim.getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.reflectAttributeValues(
          instanceHandle, theAttributes, userSuppliedTag, sentOrdering, theTransport, reflectInfo);
    } else {
      try {
        Set<OOattribute> attributeSet = oi.deserialize(theAttributes);

        federateReference.reflectAttributeValues(
            oi.getObject(), attributeSet, userSuppliedTag, sentOrdering, theTransport, reflectInfo);
      } catch (RTIinternalError ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void removeObjectInstance(
      ObjectInstanceHandle instanceHandle,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      LogicalTime theTime,
      OrderType receivedOrdering,
      MessageRetractionHandle retractionHandle,
      FederateAmbassador.SupplementalRemoveInfo removeInfo)
      throws FederateInternalError {
    // if the object instance was not registered use the regular call back
    ObjectInstance oi = rtiamb.oim.getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.removeObjectInstance(
          instanceHandle,
          userSuppliedTag,
          sentOrdering,
          theTime,
          receivedOrdering,
          retractionHandle,
          removeInfo);
    } else {
      rtiamb.oim.removeObjectInstance(oi);
      federateReference.removeObjectInstance(
          oi.getObject(),
          userSuppliedTag,
          sentOrdering,
          theTime,
          receivedOrdering,
          retractionHandle,
          removeInfo);
    }
  }

  @Override
  public void removeObjectInstance(
      ObjectInstanceHandle instanceHandle,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      LogicalTime theTime,
      OrderType receivedOrdering,
      FederateAmbassador.SupplementalRemoveInfo removeInfo)
      throws FederateInternalError {
    // if the object instance was not registered use the regular call back
    ObjectInstance oi = rtiamb.oim.getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.removeObjectInstance(
          instanceHandle, userSuppliedTag, sentOrdering, theTime, receivedOrdering, removeInfo);
    } else {
      rtiamb.oim.removeObjectInstance(oi);
      federateReference.removeObjectInstance(
          oi.getObject(), userSuppliedTag, sentOrdering, theTime, receivedOrdering, removeInfo);
    }
  }

  @Override
  public void removeObjectInstance(
      ObjectInstanceHandle instanceHandle,
      byte[] userSuppliedTag,
      OrderType sentOrdering,
      FederateAmbassador.SupplementalRemoveInfo removeInfo)
      throws FederateInternalError {
    // if the object instance was not registered use the regular call back
    ObjectInstance oi = rtiamb.oim.getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.removeObjectInstance(
          instanceHandle, userSuppliedTag, sentOrdering, removeInfo);
    } else {
      rtiamb.oim.removeObjectInstance(oi);
      federateReference.removeObjectInstance(
          oi.getObject(), userSuppliedTag, sentOrdering, removeInfo);
    }
  }

  @Override
  public void provideAttributeValueUpdate(
      ObjectInstanceHandle instanceHandle, AttributeHandleSet theAttributes, byte[] userSuppliedTag)
      throws FederateInternalError {

    ObjectInstance oi = rtiamb.oim.getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      // the object instance was not registered, so use the regular call back
      federateReference.provideAttributeValueUpdate(instanceHandle, theAttributes, userSuppliedTag);
    } else {
      try {
        // convert the attribute instance handles to a set of OOattributes
        Set<OOattribute> attributeSet = oi.getObjectClass().createAttributeSet(theAttributes);

        federateReference.provideAttributeValueUpdate(
            oi.getObject(), attributeSet, userSuppliedTag);
      } catch (AttributeNotDefined ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void attributesInScope(
      ObjectInstanceHandle instanceHandle, AttributeHandleSet theAttributes)
      throws FederateInternalError {
    federateReference.attributesInScope(instanceHandle, theAttributes);
  }

  @Override
  public void attributesOutOfScope(
      ObjectInstanceHandle instanceHandle, AttributeHandleSet theAttributes)
      throws FederateInternalError {
    federateReference.attributesOutOfScope(instanceHandle, theAttributes);
  }

  @Override
  public void turnUpdatesOnForObjectInstance(
      ObjectInstanceHandle instanceHandle, AttributeHandleSet theAttributes)
      throws FederateInternalError {
    federateReference.turnUpdatesOnForObjectInstance(instanceHandle, theAttributes);
  }

  @Override
  public void turnUpdatesOnForObjectInstance(
      ObjectInstanceHandle instanceHandle,
      AttributeHandleSet theAttributes,
      String updateRateDesignator)
      throws FederateInternalError {
    federateReference.turnUpdatesOnForObjectInstance(instanceHandle, theAttributes);
  }

  @Override
  public void turnUpdatesOffForObjectInstance(
      ObjectInstanceHandle instanceHandle, AttributeHandleSet theAttributes)
      throws FederateInternalError {
    federateReference.turnUpdatesOffForObjectInstance(instanceHandle, theAttributes);
  }

  @Override
  public void confirmAttributeTransportationTypeChange(
      ObjectInstanceHandle instanceHandle,
      AttributeHandleSet theAttributes,
      TransportationTypeHandle theTransportation)
      throws FederateInternalError {
    federateReference.confirmAttributeTransportationTypeChange(
        instanceHandle, theAttributes, theTransportation);
  }

  @Override
  public void reportAttributeTransportationType(
      ObjectInstanceHandle instanceHandle,
      AttributeHandle theAttribute,
      TransportationTypeHandle theTransportation)
      throws FederateInternalError {
    federateReference.reportAttributeTransportationType(
        instanceHandle, theAttribute, theTransportation);
  }

  @Override
  public void confirmInteractionTransportationTypeChange(
      InteractionClassHandle classHandle, TransportationTypeHandle theTransportation)
      throws FederateInternalError {
    federateReference.confirmInteractionTransportationTypeChange(classHandle, theTransportation);
  }

  @Override
  public void reportInteractionTransportationType(
      FederateHandle theFederate,
      InteractionClassHandle classHandle,
      TransportationTypeHandle theTransportation)
      throws FederateInternalError {
    federateReference.reportInteractionTransportationType(
        theFederate, classHandle, theTransportation);
  }

  ///////////////////////////////////
  // Ownership Management Services //
  ///////////////////////////////////
  @Override
  public void requestAttributeOwnershipAssumption(
      ObjectInstanceHandle instanceHandle,
      AttributeHandleSet offeredAttributes,
      byte[] userSuppliedTag)
      throws FederateInternalError {
    federateReference.requestAttributeOwnershipAssumption(
        instanceHandle, offeredAttributes, userSuppliedTag);
  }

  @Override
  public void requestDivestitureConfirmation(
      ObjectInstanceHandle instanceHandle, AttributeHandleSet offeredAttributes)
      throws FederateInternalError {
    federateReference.requestDivestitureConfirmation(instanceHandle, offeredAttributes);
  }

  @Override
  public void attributeOwnershipAcquisitionNotification(
      ObjectInstanceHandle instanceHandle,
      AttributeHandleSet securedAttributes,
      byte[] userSuppliedTag)
      throws FederateInternalError {
    federateReference.attributeOwnershipAcquisitionNotification(
        instanceHandle, securedAttributes, userSuppliedTag);
  }

  @Override
  public void attributeOwnershipUnavailable(
      ObjectInstanceHandle instanceHandle, AttributeHandleSet theAttributes)
      throws FederateInternalError {
    federateReference.attributeOwnershipUnavailable(instanceHandle, theAttributes);
  }

  @Override
  public void requestAttributeOwnershipRelease(
      ObjectInstanceHandle instanceHandle,
      AttributeHandleSet candidateAttributes,
      byte[] userSuppliedTag)
      throws FederateInternalError {
    federateReference.requestAttributeOwnershipRelease(
        instanceHandle, candidateAttributes, userSuppliedTag);
  }

  @Override
  public void confirmAttributeOwnershipAcquisitionCancellation(
      ObjectInstanceHandle instanceHandle, AttributeHandleSet theAttributes)
      throws FederateInternalError {
    federateReference.confirmAttributeOwnershipAcquisitionCancellation(
        instanceHandle, theAttributes);
  }

  @Override
  public void informAttributeOwnership(
      ObjectInstanceHandle instanceHandle, AttributeHandle theAttribute, FederateHandle theOwner)
      throws FederateInternalError {
    federateReference.informAttributeOwnership(instanceHandle, theAttribute, theOwner);
  }

  @Override
  public void attributeIsNotOwned(ObjectInstanceHandle instanceHandle, AttributeHandle theAttribute)
      throws FederateInternalError {
    federateReference.attributeIsNotOwned(instanceHandle, theAttribute);
  }

  @Override
  public void attributeIsOwnedByRTI(
      ObjectInstanceHandle instanceHandle, AttributeHandle theAttribute)
      throws FederateInternalError {
    federateReference.attributeIsOwnedByRTI(instanceHandle, theAttribute);
  }

  //////////////////////////////
  // Time Management Services //
  //////////////////////////////
  @Override
  public void timeRegulationEnabled(LogicalTime time) throws FederateInternalError {
    federateReference.timeRegulationEnabled(time);
  }

  @Override
  public void timeConstrainedEnabled(LogicalTime time) throws FederateInternalError {
    federateReference.timeConstrainedEnabled(time);
  }

  @Override
  public void timeAdvanceGrant(LogicalTime theTime) throws FederateInternalError {
    federateReference.timeAdvanceGrant(theTime);
  }

  @Override
  public void requestRetraction(MessageRetractionHandle theHandle) throws FederateInternalError {
    federateReference.requestRetraction(theHandle);
  }
}
