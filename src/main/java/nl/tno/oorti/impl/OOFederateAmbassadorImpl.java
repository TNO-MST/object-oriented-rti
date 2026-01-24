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

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    InteractionClass ic = ec.getIcm().getClassByHandle(classHandle);
    if (ic == null) {
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

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    InteractionClass ic = ec.getIcm().getClassByHandle(classHandle);
    if (ic == null) {
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

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    InteractionClass ic = ec.getIcm().getClassByHandle(classHandle);
    if (ic == null) {
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

    ExecutionContext ec = rtiamb.ecmInitial.getExecutionContext();
    if (ec == null) {
      throw new FederateInternalError("No execution context");
    }

    // get the object class of the discovered instance
    ObjectClass oc = ec.getOcm().getClassByHandle(classHandle);

    // keep the instance handle for quick lookup later
    rtiamb.hlaFederationInstanceHandle = instanceHandle;

    try {
      // create an object instance
      rtiamb.hlaFederation =
          (HLAfederation)
              ec.getOim()
                  .create(
                      oc,
                      instanceHandle,
                      new HLAfederation(),
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
        | IllegalArgumentException
        | SecurityException ex) {
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
      ExecutionContext ec = rtiamb.ecm.getExecutionContext();
      if (ec == null) return;

      ObjectClass oc = ec.getOcm().getClassByHandle(classHandle);
      if (oc == null) {
        federateReference.discoverObjectInstance(
            instanceHandle, classHandle, theObjectName, producingFederate);
      } else {
        try {
          Object theObject = rtiamb.objectFactory.createObject(oc.getClazz());
          ObjectInstance oi = ec.getOim().create(oc, instanceHandle, theObject, theObjectName);
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
      ExecutionContext ec = rtiamb.ecm.getExecutionContext();
      if (ec == null) return;

      ObjectClass oc = ec.getOcm().getClassByHandle(classHandle);
      if (oc == null) {
        federateReference.discoverObjectInstance(instanceHandle, classHandle, theObjectName);
      } else {
        try {
          Object theObject = rtiamb.objectFactory.createObject(oc.getClazz());
          ObjectInstance oi = ec.getOim().create(oc, instanceHandle, theObject, theObjectName);
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
      ExecutionContext ec = rtiamb.ecmInitial.getExecutionContext();
      if (ec == null) {
        throw new FederateInternalError("No execution context");
      }

      ec.getOim().getObjectInstanceByHandle(instanceHandle).deserialize(theAttributes);
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

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
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

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
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

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
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

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
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
      ec.getOim().removeObjectInstance(oi);
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

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.removeObjectInstance(
          instanceHandle, userSuppliedTag, sentOrdering, theTime, receivedOrdering, removeInfo);
    } else {
      ec.getOim().removeObjectInstance(oi);
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

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.removeObjectInstance(
          instanceHandle, userSuppliedTag, sentOrdering, removeInfo);
    } else {
      ec.getOim().removeObjectInstance(oi);
      federateReference.removeObjectInstance(
          oi.getObject(), userSuppliedTag, sentOrdering, removeInfo);
    }
  }

  @Override
  public void provideAttributeValueUpdate(
      ObjectInstanceHandle instanceHandle, AttributeHandleSet theAttributes, byte[] userSuppliedTag)
      throws FederateInternalError {

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
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

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.attributesInScope(instanceHandle, theAttributes);
    } else {
      try {
        // convert the attribute instance handles to a set of OOattributes
        Set<OOattribute> attributeSet = oi.getObjectClass().createAttributeSet(theAttributes);

        federateReference.attributesInScope(oi.getObject(), attributeSet);
      } catch (AttributeNotDefined ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void attributesOutOfScope(
      ObjectInstanceHandle instanceHandle, AttributeHandleSet theAttributes)
      throws FederateInternalError {

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.attributesOutOfScope(instanceHandle, theAttributes);
    } else {
      try {
        Set<OOattribute> attributeSet = oi.getObjectClass().createAttributeSet(theAttributes);
        federateReference.attributesOutOfScope(oi.getObject(), attributeSet);
      } catch (AttributeNotDefined ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void turnUpdatesOnForObjectInstance(
      ObjectInstanceHandle instanceHandle, AttributeHandleSet theAttributes)
      throws FederateInternalError {

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.turnUpdatesOnForObjectInstance(instanceHandle, theAttributes);
    } else {
      try {
        Set<OOattribute> attributeSet = oi.getObjectClass().createAttributeSet(theAttributes);
        federateReference.turnUpdatesOnForObjectInstance(oi.getObject(), attributeSet);
      } catch (AttributeNotDefined ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void turnUpdatesOnForObjectInstance(
      ObjectInstanceHandle instanceHandle,
      AttributeHandleSet theAttributes,
      String updateRateDesignator)
      throws FederateInternalError {

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.turnUpdatesOnForObjectInstance(
          instanceHandle, theAttributes, updateRateDesignator);
    } else {
      try {
        Set<OOattribute> attributeSet = oi.getObjectClass().createAttributeSet(theAttributes);
        federateReference.turnUpdatesOnForObjectInstance(
            oi.getObject(), attributeSet, updateRateDesignator);
      } catch (AttributeNotDefined ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void turnUpdatesOffForObjectInstance(
      ObjectInstanceHandle instanceHandle, AttributeHandleSet theAttributes)
      throws FederateInternalError {

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.turnUpdatesOffForObjectInstance(instanceHandle, theAttributes);
    } else {
      try {
        Set<OOattribute> attributeSet = oi.getObjectClass().createAttributeSet(theAttributes);
        federateReference.turnUpdatesOffForObjectInstance(oi.getObject(), attributeSet);
      } catch (AttributeNotDefined ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void confirmAttributeTransportationTypeChange(
      ObjectInstanceHandle instanceHandle,
      AttributeHandleSet theAttributes,
      TransportationTypeHandle theTransportation)
      throws FederateInternalError {

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.confirmAttributeTransportationTypeChange(
          instanceHandle, theAttributes, theTransportation);
    } else {
      try {
        Set<OOattribute> attributeSet = oi.getObjectClass().createAttributeSet(theAttributes);
        federateReference.confirmAttributeTransportationTypeChange(
            oi.getObject(), attributeSet, theTransportation);
      } catch (AttributeNotDefined ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void reportAttributeTransportationType(
      ObjectInstanceHandle instanceHandle,
      AttributeHandle theAttribute,
      TransportationTypeHandle theTransportation)
      throws FederateInternalError {

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.reportAttributeTransportationType(
          instanceHandle, theAttribute, theTransportation);
    } else {
      try {
        Attribute a = oi.getObjectClass().getAttributeByHandleIfExists(theAttribute);
        federateReference.reportAttributeTransportationType(oi.getObject(), a, theTransportation);
      } catch (AttributeNotDefined ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void confirmInteractionTransportationTypeChange(
      InteractionClassHandle classHandle, TransportationTypeHandle theTransportation)
      throws FederateInternalError {

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    InteractionClass ic = ec.getIcm().getClassByHandle(classHandle);
    if (ic == null) {
      federateReference.confirmInteractionTransportationTypeChange(classHandle, theTransportation);
    } else {
      federateReference.confirmInteractionTransportationTypeChange(
          ic.getClazz(), theTransportation);
    }
  }

  @Override
  public void reportInteractionTransportationType(
      FederateHandle theFederate,
      InteractionClassHandle classHandle,
      TransportationTypeHandle theTransportation)
      throws FederateInternalError {

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    InteractionClass ic = ec.getIcm().getClassByHandle(classHandle);
    if (ic == null) {
      federateReference.reportInteractionTransportationType(
          theFederate, classHandle, theTransportation);
    } else {
      federateReference.reportInteractionTransportationType(
          theFederate, ic.getClazz(), theTransportation);
    }
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

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.requestAttributeOwnershipAssumption(
          instanceHandle, offeredAttributes, userSuppliedTag);
    } else {
      try {
        Set<OOattribute> attributeSet = oi.getObjectClass().createAttributeSet(offeredAttributes);
        federateReference.requestAttributeOwnershipAssumption(
            oi.getObject(), attributeSet, userSuppliedTag);
      } catch (AttributeNotDefined ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void requestDivestitureConfirmation(
      ObjectInstanceHandle instanceHandle, AttributeHandleSet offeredAttributes)
      throws FederateInternalError {

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.requestDivestitureConfirmation(instanceHandle, offeredAttributes);
    } else {
      try {
        Set<OOattribute> attributeSet = oi.getObjectClass().createAttributeSet(offeredAttributes);
        federateReference.requestDivestitureConfirmation(oi.getObject(), attributeSet);
      } catch (AttributeNotDefined ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void attributeOwnershipAcquisitionNotification(
      ObjectInstanceHandle instanceHandle,
      AttributeHandleSet securedAttributes,
      byte[] userSuppliedTag)
      throws FederateInternalError {

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.attributeOwnershipAcquisitionNotification(
          instanceHandle, securedAttributes, userSuppliedTag);
    } else {
      try {
        Set<OOattribute> attributeSet = oi.getObjectClass().createAttributeSet(securedAttributes);
        federateReference.attributeOwnershipAcquisitionNotification(
            oi.getObject(), attributeSet, userSuppliedTag);
      } catch (AttributeNotDefined ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void attributeOwnershipUnavailable(
      ObjectInstanceHandle instanceHandle, AttributeHandleSet theAttributes)
      throws FederateInternalError {

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.attributeOwnershipUnavailable(instanceHandle, theAttributes);
    } else {
      try {
        Set<OOattribute> attributeSet = oi.getObjectClass().createAttributeSet(theAttributes);
        federateReference.attributeOwnershipUnavailable(oi.getObject(), attributeSet);
      } catch (AttributeNotDefined ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void requestAttributeOwnershipRelease(
      ObjectInstanceHandle instanceHandle,
      AttributeHandleSet candidateAttributes,
      byte[] userSuppliedTag)
      throws FederateInternalError {

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.requestAttributeOwnershipRelease(
          instanceHandle, candidateAttributes, userSuppliedTag);
    } else {
      try {
        Set<OOattribute> attributeSet = oi.getObjectClass().createAttributeSet(candidateAttributes);
        federateReference.requestAttributeOwnershipRelease(
            oi.getObject(), attributeSet, userSuppliedTag);
      } catch (AttributeNotDefined ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void confirmAttributeOwnershipAcquisitionCancellation(
      ObjectInstanceHandle instanceHandle, AttributeHandleSet theAttributes)
      throws FederateInternalError {

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.confirmAttributeOwnershipAcquisitionCancellation(
          instanceHandle, theAttributes);
    } else {
      try {
        Set<OOattribute> attributeSet = oi.getObjectClass().createAttributeSet(theAttributes);
        federateReference.confirmAttributeOwnershipAcquisitionCancellation(
            oi.getObject(), attributeSet);
      } catch (AttributeNotDefined ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void informAttributeOwnership(
      ObjectInstanceHandle instanceHandle, AttributeHandle theAttribute, FederateHandle theOwner)
      throws FederateInternalError {

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.informAttributeOwnership(instanceHandle, theAttribute, theOwner);
    } else {
      try {
        Attribute a = oi.getObjectClass().getAttributeByHandleIfExists(theAttribute);
        federateReference.informAttributeOwnership(oi.getObject(), a, theOwner);
      } catch (AttributeNotDefined ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void attributeIsNotOwned(ObjectInstanceHandle instanceHandle, AttributeHandle theAttribute)
      throws FederateInternalError {

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.attributeIsNotOwned(instanceHandle, theAttribute);
    } else {
      try {
        Attribute a = oi.getObjectClass().getAttributeByHandleIfExists(theAttribute);
        federateReference.attributeIsNotOwned(oi.getObject(), a);
      } catch (AttributeNotDefined ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void attributeIsOwnedByRTI(
      ObjectInstanceHandle instanceHandle, AttributeHandle theAttribute)
      throws FederateInternalError {

    ExecutionContext ec = rtiamb.ecm.getExecutionContext();
    if (ec == null) return;

    ObjectInstance oi = ec.getOim().getObjectInstanceByHandle(instanceHandle);
    if (oi == null) {
      federateReference.attributeIsOwnedByRTI(instanceHandle, theAttribute);
    } else {
      try {
        Attribute a = oi.getObjectClass().getAttributeByHandleIfExists(theAttribute);
        federateReference.attributeIsOwnedByRTI(oi.getObject(), a);
      } catch (AttributeNotDefined ex) {
        throw new FederateInternalError(ex.getMessage(), ex);
      }
    }
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
