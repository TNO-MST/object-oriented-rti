package nl.tno.oorti.impl;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleFactory;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleSetFactory;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.AttributeHandleValueMapFactory;
import hla.rti1516e.AttributeSetRegionSetPairList;
import hla.rti1516e.AttributeSetRegionSetPairListFactory;
import hla.rti1516e.CallbackModel;
import hla.rti1516e.DimensionHandle;
import hla.rti1516e.DimensionHandleFactory;
import hla.rti1516e.DimensionHandleSet;
import hla.rti1516e.DimensionHandleSetFactory;
import hla.rti1516e.FederateAmbassador;
import hla.rti1516e.FederateHandle;
import hla.rti1516e.FederateHandleFactory;
import hla.rti1516e.FederateHandleSet;
import hla.rti1516e.FederateHandleSetFactory;
import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.InteractionClassHandleFactory;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.LogicalTimeFactory;
import hla.rti1516e.LogicalTimeInterval;
import hla.rti1516e.MessageRetractionHandle;
import hla.rti1516e.MessageRetractionReturn;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectClassHandleFactory;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.ObjectInstanceHandleFactory;
import hla.rti1516e.OrderType;
import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleFactory;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.ParameterHandleValueMapFactory;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.RangeBounds;
import hla.rti1516e.RegionHandle;
import hla.rti1516e.RegionHandleSet;
import hla.rti1516e.RegionHandleSetFactory;
import hla.rti1516e.ResignAction;
import hla.rti1516e.ServiceGroup;
import hla.rti1516e.TimeQueryReturn;
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.TransportationTypeHandleFactory;
import hla.rti1516e.exceptions.AlreadyConnected;
import hla.rti1516e.exceptions.AsynchronousDeliveryAlreadyDisabled;
import hla.rti1516e.exceptions.AsynchronousDeliveryAlreadyEnabled;
import hla.rti1516e.exceptions.AttributeAcquisitionWasNotRequested;
import hla.rti1516e.exceptions.AttributeAlreadyBeingAcquired;
import hla.rti1516e.exceptions.AttributeAlreadyBeingChanged;
import hla.rti1516e.exceptions.AttributeAlreadyBeingDivested;
import hla.rti1516e.exceptions.AttributeAlreadyOwned;
import hla.rti1516e.exceptions.AttributeDivestitureWasNotRequested;
import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.AttributeNotOwned;
import hla.rti1516e.exceptions.AttributeNotPublished;
import hla.rti1516e.exceptions.AttributeRelevanceAdvisorySwitchIsOff;
import hla.rti1516e.exceptions.AttributeRelevanceAdvisorySwitchIsOn;
import hla.rti1516e.exceptions.AttributeScopeAdvisorySwitchIsOff;
import hla.rti1516e.exceptions.AttributeScopeAdvisorySwitchIsOn;
import hla.rti1516e.exceptions.CallNotAllowedFromWithinCallback;
import hla.rti1516e.exceptions.ConnectionFailed;
import hla.rti1516e.exceptions.CouldNotCreateLogicalTimeFactory;
import hla.rti1516e.exceptions.CouldNotOpenFDD;
import hla.rti1516e.exceptions.CouldNotOpenMIM;
import hla.rti1516e.exceptions.DeletePrivilegeNotHeld;
import hla.rti1516e.exceptions.DesignatorIsHLAstandardMIM;
import hla.rti1516e.exceptions.ErrorReadingFDD;
import hla.rti1516e.exceptions.ErrorReadingMIM;
import hla.rti1516e.exceptions.FederateAlreadyExecutionMember;
import hla.rti1516e.exceptions.FederateHandleNotKnown;
import hla.rti1516e.exceptions.FederateHasNotBegunSave;
import hla.rti1516e.exceptions.FederateIsExecutionMember;
import hla.rti1516e.exceptions.FederateNameAlreadyInUse;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateOwnsAttributes;
import hla.rti1516e.exceptions.FederateServiceInvocationsAreBeingReportedViaMOM;
import hla.rti1516e.exceptions.FederateUnableToUseTime;
import hla.rti1516e.exceptions.FederatesCurrentlyJoined;
import hla.rti1516e.exceptions.FederationExecutionAlreadyExists;
import hla.rti1516e.exceptions.FederationExecutionDoesNotExist;
import hla.rti1516e.exceptions.IllegalName;
import hla.rti1516e.exceptions.InTimeAdvancingState;
import hla.rti1516e.exceptions.InconsistentFDD;
import hla.rti1516e.exceptions.InteractionClassAlreadyBeingChanged;
import hla.rti1516e.exceptions.InteractionClassNotDefined;
import hla.rti1516e.exceptions.InteractionClassNotPublished;
import hla.rti1516e.exceptions.InteractionParameterNotDefined;
import hla.rti1516e.exceptions.InteractionRelevanceAdvisorySwitchIsOff;
import hla.rti1516e.exceptions.InteractionRelevanceAdvisorySwitchIsOn;
import hla.rti1516e.exceptions.InvalidAttributeHandle;
import hla.rti1516e.exceptions.InvalidDimensionHandle;
import hla.rti1516e.exceptions.InvalidFederateHandle;
import hla.rti1516e.exceptions.InvalidInteractionClassHandle;
import hla.rti1516e.exceptions.InvalidLocalSettingsDesignator;
import hla.rti1516e.exceptions.InvalidLogicalTime;
import hla.rti1516e.exceptions.InvalidLookahead;
import hla.rti1516e.exceptions.InvalidMessageRetractionHandle;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.InvalidOrderName;
import hla.rti1516e.exceptions.InvalidOrderType;
import hla.rti1516e.exceptions.InvalidParameterHandle;
import hla.rti1516e.exceptions.InvalidRangeBound;
import hla.rti1516e.exceptions.InvalidRegion;
import hla.rti1516e.exceptions.InvalidRegionContext;
import hla.rti1516e.exceptions.InvalidResignAction;
import hla.rti1516e.exceptions.InvalidServiceGroup;
import hla.rti1516e.exceptions.InvalidTransportationName;
import hla.rti1516e.exceptions.InvalidTransportationType;
import hla.rti1516e.exceptions.InvalidUpdateRateDesignator;
import hla.rti1516e.exceptions.LogicalTimeAlreadyPassed;
import hla.rti1516e.exceptions.MessageCanNoLongerBeRetracted;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NameSetWasEmpty;
import hla.rti1516e.exceptions.NoAcquisitionPending;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.ObjectClassNotPublished;
import hla.rti1516e.exceptions.ObjectClassRelevanceAdvisorySwitchIsOff;
import hla.rti1516e.exceptions.ObjectClassRelevanceAdvisorySwitchIsOn;
import hla.rti1516e.exceptions.ObjectInstanceNameInUse;
import hla.rti1516e.exceptions.ObjectInstanceNameNotReserved;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import hla.rti1516e.exceptions.OwnershipAcquisitionPending;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RegionDoesNotContainSpecifiedDimension;
import hla.rti1516e.exceptions.RegionInUseForUpdateOrSubscription;
import hla.rti1516e.exceptions.RegionNotCreatedByThisFederate;
import hla.rti1516e.exceptions.RequestForTimeConstrainedPending;
import hla.rti1516e.exceptions.RequestForTimeRegulationPending;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.RestoreNotInProgress;
import hla.rti1516e.exceptions.RestoreNotRequested;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.exceptions.SaveNotInProgress;
import hla.rti1516e.exceptions.SaveNotInitiated;
import hla.rti1516e.exceptions.SynchronizationPointLabelNotAnnounced;
import hla.rti1516e.exceptions.TimeConstrainedAlreadyEnabled;
import hla.rti1516e.exceptions.TimeConstrainedIsNotEnabled;
import hla.rti1516e.exceptions.TimeRegulationAlreadyEnabled;
import hla.rti1516e.exceptions.TimeRegulationIsNotEnabled;
import hla.rti1516e.exceptions.UnsupportedCallbackModel;
import java.net.URL;
import java.util.Set;

/**
 * @author bergtwvd
 */
class NullRTIambassador implements RTIambassador {

  // the RTI Ambassador
  protected final RTIambassador rtiamb;

  protected NullRTIambassador(RTIambassador rtiamb) {
    this.rtiamb = rtiamb;
  }

  ////////////////////////////////////
  // Federation Management Services //
  ////////////////////////////////////
  @Override
  public void connect(
      FederateAmbassador federateReference,
      CallbackModel callbackModel,
      String localSettingsDesignator)
      throws ConnectionFailed,
          InvalidLocalSettingsDesignator,
          UnsupportedCallbackModel,
          AlreadyConnected,
          CallNotAllowedFromWithinCallback,
          RTIinternalError {
    rtiamb.connect(federateReference, callbackModel, localSettingsDesignator);
  }

  @Override
  public void connect(FederateAmbassador federateReference, CallbackModel callbackModel)
      throws ConnectionFailed,
          InvalidLocalSettingsDesignator,
          UnsupportedCallbackModel,
          AlreadyConnected,
          CallNotAllowedFromWithinCallback,
          RTIinternalError {
    rtiamb.connect(federateReference, callbackModel);
  }

  @Override
  public void disconnect()
      throws FederateIsExecutionMember, CallNotAllowedFromWithinCallback, RTIinternalError {
    rtiamb.disconnect();
  }

  @Override
  public void createFederationExecution(
      String federationExecutionName,
      URL[] fomModules,
      URL mimModule,
      String logicalTimeImplementationName)
      throws CouldNotCreateLogicalTimeFactory,
          InconsistentFDD,
          ErrorReadingFDD,
          CouldNotOpenFDD,
          ErrorReadingMIM,
          CouldNotOpenMIM,
          DesignatorIsHLAstandardMIM,
          FederationExecutionAlreadyExists,
          NotConnected,
          RTIinternalError {
    rtiamb.createFederationExecution(
        federationExecutionName, fomModules, mimModule, logicalTimeImplementationName);
  }

  @Override
  public void createFederationExecution(
      String federationExecutionName, URL[] fomModules, String logicalTimeImplementationName)
      throws CouldNotCreateLogicalTimeFactory,
          InconsistentFDD,
          ErrorReadingFDD,
          CouldNotOpenFDD,
          FederationExecutionAlreadyExists,
          NotConnected,
          RTIinternalError {
    rtiamb.createFederationExecution(
        federationExecutionName, fomModules, logicalTimeImplementationName);
  }

  @Override
  public void createFederationExecution(
      String federationExecutionName, URL[] fomModules, URL mimModule)
      throws InconsistentFDD,
          ErrorReadingFDD,
          CouldNotOpenFDD,
          ErrorReadingMIM,
          CouldNotOpenMIM,
          DesignatorIsHLAstandardMIM,
          FederationExecutionAlreadyExists,
          NotConnected,
          RTIinternalError {
    rtiamb.createFederationExecution(federationExecutionName, fomModules, mimModule);
  }

  @Override
  public void createFederationExecution(String federationExecutionName, URL[] fomModules)
      throws InconsistentFDD,
          ErrorReadingFDD,
          CouldNotOpenFDD,
          FederationExecutionAlreadyExists,
          NotConnected,
          RTIinternalError {
    rtiamb.createFederationExecution(federationExecutionName, fomModules);
  }

  @Override
  public void createFederationExecution(String federationExecutionName, URL fomModule)
      throws InconsistentFDD,
          ErrorReadingFDD,
          CouldNotOpenFDD,
          FederationExecutionAlreadyExists,
          NotConnected,
          RTIinternalError {
    rtiamb.createFederationExecution(federationExecutionName, fomModule);
  }

  @Override
  public void destroyFederationExecution(String federationExecutionName)
      throws FederatesCurrentlyJoined,
          FederationExecutionDoesNotExist,
          NotConnected,
          RTIinternalError {
    rtiamb.destroyFederationExecution(federationExecutionName);
  }

  @Override
  public void listFederationExecutions() throws NotConnected, RTIinternalError {
    rtiamb.listFederationExecutions();
  }

  @Override
  public FederateHandle joinFederationExecution(
      String federateName,
      String federateType,
      String federationExecutionName,
      URL[] additionalFomModules)
      throws CouldNotCreateLogicalTimeFactory,
          FederateNameAlreadyInUse,
          FederationExecutionDoesNotExist,
          InconsistentFDD,
          ErrorReadingFDD,
          CouldNotOpenFDD,
          SaveInProgress,
          RestoreInProgress,
          FederateAlreadyExecutionMember,
          NotConnected,
          CallNotAllowedFromWithinCallback,
          RTIinternalError {
    return rtiamb.joinFederationExecution(
        federateName, federateType, federationExecutionName, additionalFomModules);
  }

  @Override
  public FederateHandle joinFederationExecution(
      String federateType, String federationExecutionName, URL[] additionalFomModules)
      throws CouldNotCreateLogicalTimeFactory,
          FederationExecutionDoesNotExist,
          InconsistentFDD,
          ErrorReadingFDD,
          CouldNotOpenFDD,
          SaveInProgress,
          RestoreInProgress,
          FederateAlreadyExecutionMember,
          NotConnected,
          CallNotAllowedFromWithinCallback,
          RTIinternalError {
    return rtiamb.joinFederationExecution(
        federateType, federationExecutionName, additionalFomModules);
  }

  @Override
  public FederateHandle joinFederationExecution(
      String federateName, String federateType, String federationExecutionName)
      throws CouldNotCreateLogicalTimeFactory,
          FederateNameAlreadyInUse,
          FederationExecutionDoesNotExist,
          SaveInProgress,
          RestoreInProgress,
          FederateAlreadyExecutionMember,
          NotConnected,
          CallNotAllowedFromWithinCallback,
          RTIinternalError {
    return rtiamb.joinFederationExecution(federateName, federateType, federationExecutionName);
  }

  @Override
  public FederateHandle joinFederationExecution(String federateType, String federationExecutionName)
      throws CouldNotCreateLogicalTimeFactory,
          FederationExecutionDoesNotExist,
          SaveInProgress,
          RestoreInProgress,
          FederateAlreadyExecutionMember,
          NotConnected,
          CallNotAllowedFromWithinCallback,
          RTIinternalError {
    return rtiamb.joinFederationExecution(federateType, federationExecutionName);
  }

  @Override
  public void resignFederationExecution(ResignAction resignAction)
      throws InvalidResignAction,
          OwnershipAcquisitionPending,
          FederateOwnsAttributes,
          FederateNotExecutionMember,
          NotConnected,
          CallNotAllowedFromWithinCallback,
          RTIinternalError {
    rtiamb.resignFederationExecution(resignAction);
  }

  @Override
  public void registerFederationSynchronizationPoint(
      String synchronizationPointLabel, byte[] userSuppliedTag)
      throws SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.registerFederationSynchronizationPoint(synchronizationPointLabel, userSuppliedTag);
  }

  @Override
  public void registerFederationSynchronizationPoint(
      String synchronizationPointLabel,
      byte[] userSuppliedTag,
      FederateHandleSet synchronizationSet)
      throws InvalidFederateHandle,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.registerFederationSynchronizationPoint(
        synchronizationPointLabel, userSuppliedTag, synchronizationSet);
  }

  @Override
  public void synchronizationPointAchieved(String synchronizationPointLabel)
      throws SynchronizationPointLabelNotAnnounced,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.synchronizationPointAchieved(synchronizationPointLabel);
  }

  @Override
  public void synchronizationPointAchieved(
      String synchronizationPointLabel, boolean successIndicator)
      throws SynchronizationPointLabelNotAnnounced,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.synchronizationPointAchieved(synchronizationPointLabel, successIndicator);
  }

  @Override
  public void requestFederationSave(String label)
      throws SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.requestFederationSave(label);
  }

  @Override
  public void requestFederationSave(String label, LogicalTime theTime)
      throws LogicalTimeAlreadyPassed,
          InvalidLogicalTime,
          FederateUnableToUseTime,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.requestFederationSave(label, theTime);
  }

  @Override
  public void federateSaveBegun()
      throws SaveNotInitiated,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.federateSaveBegun();
  }

  @Override
  public void federateSaveComplete()
      throws FederateHasNotBegunSave,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.federateSaveComplete();
  }

  @Override
  public void federateSaveNotComplete()
      throws FederateHasNotBegunSave,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.federateSaveNotComplete();
  }

  @Override
  public void abortFederationSave()
      throws SaveNotInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
    rtiamb.abortFederationSave();
  }

  @Override
  public void queryFederationSaveStatus()
      throws RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
    rtiamb.queryFederationSaveStatus();
  }

  @Override
  public void requestFederationRestore(String label)
      throws SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.requestFederationRestore(label);
  }

  @Override
  public void federateRestoreComplete()
      throws RestoreNotRequested,
          SaveInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.federateRestoreComplete();
  }

  @Override
  public void federateRestoreNotComplete()
      throws RestoreNotRequested,
          SaveInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.federateRestoreNotComplete();
  }

  @Override
  public void abortFederationRestore()
      throws RestoreNotInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
    rtiamb.abortFederationRestore();
  }

  @Override
  public void queryFederationRestoreStatus()
      throws SaveInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
    rtiamb.queryFederationRestoreStatus();
  }

  /////////////////////////////////////
  // Declaration Management Services //
  /////////////////////////////////////
  @Override
  public void publishObjectClassAttributes(
      ObjectClassHandle theClass, AttributeHandleSet attributeList)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.publishObjectClassAttributes(theClass, attributeList);
  }

  @Override
  public void unpublishObjectClass(ObjectClassHandle theClass)
      throws OwnershipAcquisitionPending,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.unpublishObjectClass(theClass);
  }

  @Override
  public void unpublishObjectClassAttributes(
      ObjectClassHandle theClass, AttributeHandleSet attributeList)
      throws OwnershipAcquisitionPending,
          AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.unpublishObjectClassAttributes(theClass, attributeList);
  }

  @Override
  public void publishInteractionClass(InteractionClassHandle theInteraction)
      throws InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.publishInteractionClass(theInteraction);
  }

  @Override
  public void unpublishInteractionClass(InteractionClassHandle theInteraction)
      throws InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.unpublishInteractionClass(theInteraction);
  }

  @Override
  public void subscribeObjectClassAttributes(
      ObjectClassHandle theClass, AttributeHandleSet attributeList)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.subscribeObjectClassAttributes(theClass, attributeList);
  }

  @Override
  public void subscribeObjectClassAttributes(
      ObjectClassHandle theClass, AttributeHandleSet attributeList, String updateRateDesignator)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          InvalidUpdateRateDesignator,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.subscribeObjectClassAttributes(theClass, attributeList, updateRateDesignator);
  }

  @Override
  public void subscribeObjectClassAttributesPassively(
      ObjectClassHandle theClass, AttributeHandleSet attributeList)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.subscribeObjectClassAttributesPassively(theClass, attributeList);
  }

  @Override
  public void subscribeObjectClassAttributesPassively(
      ObjectClassHandle theClass, AttributeHandleSet attributeList, String updateRateDesignator)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          InvalidUpdateRateDesignator,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.subscribeObjectClassAttributesPassively(theClass, attributeList, updateRateDesignator);
  }

  @Override
  public void unsubscribeObjectClass(ObjectClassHandle theClass)
      throws ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.unsubscribeObjectClass(theClass);
  }

  @Override
  public void unsubscribeObjectClassAttributes(
      ObjectClassHandle theClass, AttributeHandleSet attributeList)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.unsubscribeObjectClassAttributes(theClass, attributeList);
  }

  @Override
  public void subscribeInteractionClass(InteractionClassHandle theClass)
      throws FederateServiceInvocationsAreBeingReportedViaMOM,
          InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.subscribeInteractionClass(theClass);
  }

  @Override
  public void subscribeInteractionClassPassively(InteractionClassHandle theClass)
      throws FederateServiceInvocationsAreBeingReportedViaMOM,
          InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.subscribeInteractionClassPassively(theClass);
  }

  @Override
  public void unsubscribeInteractionClass(InteractionClassHandle theClass)
      throws InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.unsubscribeInteractionClass(theClass);
  }

  ////////////////////////////////
  // Object Management Services //
  ////////////////////////////////
  @Override
  public void reserveObjectInstanceName(String theObjectName)
      throws IllegalName,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.reserveObjectInstanceName(theObjectName);
  }

  @Override
  public void releaseObjectInstanceName(String theObjectInstanceName)
      throws ObjectInstanceNameNotReserved,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.releaseObjectInstanceName(theObjectInstanceName);
  }

  @Override
  public void reserveMultipleObjectInstanceName(Set<String> theObjectNames)
      throws IllegalName,
          NameSetWasEmpty,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.reserveMultipleObjectInstanceName(theObjectNames);
  }

  @Override
  public void releaseMultipleObjectInstanceName(Set<String> theObjectNames)
      throws ObjectInstanceNameNotReserved,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.releaseMultipleObjectInstanceName(theObjectNames);
  }

  @Override
  public ObjectInstanceHandle registerObjectInstance(ObjectClassHandle theClass)
      throws ObjectClassNotPublished,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.registerObjectInstance(theClass);
  }

  @Override
  public ObjectInstanceHandle registerObjectInstance(
      ObjectClassHandle theClass, String theObjectName)
      throws ObjectInstanceNameInUse,
          ObjectInstanceNameNotReserved,
          ObjectClassNotPublished,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.registerObjectInstance(theClass, theObjectName);
  }

  @Override
  public void updateAttributeValues(
      ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes, byte[] userSuppliedTag)
      throws AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.updateAttributeValues(theObject, theAttributes, userSuppliedTag);
  }

  @Override
  public MessageRetractionReturn updateAttributeValues(
      ObjectInstanceHandle theObject,
      AttributeHandleValueMap theAttributes,
      byte[] userSuppliedTag,
      LogicalTime theTime)
      throws InvalidLogicalTime,
          AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.updateAttributeValues(theObject, theAttributes, userSuppliedTag, theTime);
  }

  @Override
  public void sendInteraction(
      InteractionClassHandle theInteraction,
      ParameterHandleValueMap theParameters,
      byte[] userSuppliedTag)
      throws InteractionClassNotPublished,
          InteractionParameterNotDefined,
          InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.sendInteraction(theInteraction, theParameters, userSuppliedTag);
  }

  @Override
  public MessageRetractionReturn sendInteraction(
      InteractionClassHandle theInteraction,
      ParameterHandleValueMap theParameters,
      byte[] userSuppliedTag,
      LogicalTime theTime)
      throws InvalidLogicalTime,
          InteractionClassNotPublished,
          InteractionParameterNotDefined,
          InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.sendInteraction(theInteraction, theParameters, userSuppliedTag, theTime);
  }

  @Override
  public void deleteObjectInstance(ObjectInstanceHandle objectHandle, byte[] userSuppliedTag)
      throws DeletePrivilegeNotHeld,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.deleteObjectInstance(objectHandle, userSuppliedTag);
  }

  @Override
  public MessageRetractionReturn deleteObjectInstance(
      ObjectInstanceHandle objectHandle, byte[] userSuppliedTag, LogicalTime theTime)
      throws InvalidLogicalTime,
          DeletePrivilegeNotHeld,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.deleteObjectInstance(objectHandle, userSuppliedTag, theTime);
  }

  @Override
  public void localDeleteObjectInstance(ObjectInstanceHandle objectHandle)
      throws OwnershipAcquisitionPending,
          FederateOwnsAttributes,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.localDeleteObjectInstance(objectHandle);
  }

  @Override
  public void requestAttributeValueUpdate(
      ObjectInstanceHandle theObject, AttributeHandleSet theAttributes, byte[] userSuppliedTag)
      throws AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.requestAttributeValueUpdate(theObject, theAttributes, userSuppliedTag);
  }

  @Override
  public void requestAttributeValueUpdate(
      ObjectClassHandle theClass, AttributeHandleSet theAttributes, byte[] userSuppliedTag)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.requestAttributeValueUpdate(theClass, theAttributes, userSuppliedTag);
  }

  @Override
  public void requestAttributeTransportationTypeChange(
      ObjectInstanceHandle theObject,
      AttributeHandleSet theAttributes,
      TransportationTypeHandle theType)
      throws AttributeAlreadyBeingChanged,
          AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          InvalidTransportationType,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.requestAttributeTransportationTypeChange(theObject, theAttributes, theType);
  }

  @Override
  public void queryAttributeTransportationType(
      ObjectInstanceHandle theObject, AttributeHandle theAttribute)
      throws AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.queryAttributeTransportationType(theObject, theAttribute);
  }

  @Override
  public void requestInteractionTransportationTypeChange(
      InteractionClassHandle theClass, TransportationTypeHandle theType)
      throws InteractionClassAlreadyBeingChanged,
          InteractionClassNotPublished,
          InteractionClassNotDefined,
          InvalidTransportationType,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.requestInteractionTransportationTypeChange(theClass, theType);
  }

  @Override
  public void queryInteractionTransportationType(
      FederateHandle theFederate, InteractionClassHandle theInteraction)
      throws InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.queryInteractionTransportationType(theFederate, theInteraction);
  }

  ///////////////////////////////////
  // Ownership Management Services //
  ///////////////////////////////////
  @Override
  public void unconditionalAttributeOwnershipDivestiture(
      ObjectInstanceHandle theObject, AttributeHandleSet theAttributes)
      throws AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.unconditionalAttributeOwnershipDivestiture(theObject, theAttributes);
  }

  @Override
  public void negotiatedAttributeOwnershipDivestiture(
      ObjectInstanceHandle theObject, AttributeHandleSet theAttributes, byte[] userSuppliedTag)
      throws AttributeAlreadyBeingDivested,
          AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.negotiatedAttributeOwnershipDivestiture(theObject, theAttributes, userSuppliedTag);
  }

  @Override
  public void confirmDivestiture(
      ObjectInstanceHandle theObject, AttributeHandleSet theAttributes, byte[] userSuppliedTag)
      throws NoAcquisitionPending,
          AttributeDivestitureWasNotRequested,
          AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.confirmDivestiture(theObject, theAttributes, userSuppliedTag);
  }

  @Override
  public void attributeOwnershipAcquisition(
      ObjectInstanceHandle theObject, AttributeHandleSet desiredAttributes, byte[] userSuppliedTag)
      throws AttributeNotPublished,
          ObjectClassNotPublished,
          FederateOwnsAttributes,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.attributeOwnershipAcquisition(theObject, desiredAttributes, userSuppliedTag);
  }

  @Override
  public void attributeOwnershipAcquisitionIfAvailable(
      ObjectInstanceHandle theObject, AttributeHandleSet desiredAttributes)
      throws AttributeAlreadyBeingAcquired,
          AttributeNotPublished,
          ObjectClassNotPublished,
          FederateOwnsAttributes,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.attributeOwnershipAcquisitionIfAvailable(theObject, desiredAttributes);
  }

  @Override
  public void attributeOwnershipReleaseDenied(
      ObjectInstanceHandle theObject, AttributeHandleSet theAttributes)
      throws AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.attributeOwnershipReleaseDenied(theObject, theAttributes);
  }

  @Override
  public AttributeHandleSet attributeOwnershipDivestitureIfWanted(
      ObjectInstanceHandle theObject, AttributeHandleSet theAttributes)
      throws AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.attributeOwnershipDivestitureIfWanted(theObject, theAttributes);
  }

  @Override
  public void cancelNegotiatedAttributeOwnershipDivestiture(
      ObjectInstanceHandle theObject, AttributeHandleSet theAttributes)
      throws AttributeDivestitureWasNotRequested,
          AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.cancelNegotiatedAttributeOwnershipDivestiture(theObject, theAttributes);
  }

  @Override
  public void cancelAttributeOwnershipAcquisition(
      ObjectInstanceHandle theObject, AttributeHandleSet theAttributes)
      throws AttributeAcquisitionWasNotRequested,
          AttributeAlreadyOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.cancelAttributeOwnershipAcquisition(theObject, theAttributes);
  }

  @Override
  public void queryAttributeOwnership(ObjectInstanceHandle theObject, AttributeHandle theAttribute)
      throws AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.queryAttributeOwnership(theObject, theAttribute);
  }

  @Override
  public boolean isAttributeOwnedByFederate(
      ObjectInstanceHandle theObject, AttributeHandle theAttribute)
      throws AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.isAttributeOwnedByFederate(theObject, theAttribute);
  }

  //////////////////////////////
  // Time Management Services //
  //////////////////////////////
  @Override
  public void enableTimeRegulation(LogicalTimeInterval theLookahead)
      throws InvalidLookahead,
          InTimeAdvancingState,
          RequestForTimeRegulationPending,
          TimeRegulationAlreadyEnabled,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.enableTimeRegulation(theLookahead);
  }

  @Override
  public void disableTimeRegulation()
      throws TimeRegulationIsNotEnabled,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.disableTimeRegulation();
  }

  @Override
  public void enableTimeConstrained()
      throws InTimeAdvancingState,
          RequestForTimeConstrainedPending,
          TimeConstrainedAlreadyEnabled,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.enableTimeConstrained();
  }

  @Override
  public void disableTimeConstrained()
      throws TimeConstrainedIsNotEnabled,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.disableTimeConstrained();
  }

  @Override
  public void timeAdvanceRequest(LogicalTime theTime)
      throws LogicalTimeAlreadyPassed,
          InvalidLogicalTime,
          InTimeAdvancingState,
          RequestForTimeRegulationPending,
          RequestForTimeConstrainedPending,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.timeAdvanceRequest(theTime);
  }

  @Override
  public void timeAdvanceRequestAvailable(LogicalTime theTime)
      throws LogicalTimeAlreadyPassed,
          InvalidLogicalTime,
          InTimeAdvancingState,
          RequestForTimeRegulationPending,
          RequestForTimeConstrainedPending,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.timeAdvanceRequestAvailable(theTime);
  }

  @Override
  public void nextMessageRequest(LogicalTime theTime)
      throws LogicalTimeAlreadyPassed,
          InvalidLogicalTime,
          InTimeAdvancingState,
          RequestForTimeRegulationPending,
          RequestForTimeConstrainedPending,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.nextMessageRequest(theTime);
  }

  @Override
  public void nextMessageRequestAvailable(LogicalTime theTime)
      throws LogicalTimeAlreadyPassed,
          InvalidLogicalTime,
          InTimeAdvancingState,
          RequestForTimeRegulationPending,
          RequestForTimeConstrainedPending,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.nextMessageRequestAvailable(theTime);
  }

  @Override
  public void flushQueueRequest(LogicalTime theTime)
      throws LogicalTimeAlreadyPassed,
          InvalidLogicalTime,
          InTimeAdvancingState,
          RequestForTimeRegulationPending,
          RequestForTimeConstrainedPending,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.flushQueueRequest(theTime);
  }

  @Override
  public void enableAsynchronousDelivery()
      throws AsynchronousDeliveryAlreadyEnabled,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.enableAsynchronousDelivery();
  }

  @Override
  public void disableAsynchronousDelivery()
      throws AsynchronousDeliveryAlreadyDisabled,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.disableAsynchronousDelivery();
  }

  @Override
  public TimeQueryReturn queryGALT()
      throws SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.queryGALT();
  }

  @Override
  public LogicalTime queryLogicalTime()
      throws SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.queryLogicalTime();
  }

  @Override
  public TimeQueryReturn queryLITS()
      throws SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.queryLITS();
  }

  @Override
  public void modifyLookahead(LogicalTimeInterval theLookahead)
      throws InvalidLookahead,
          InTimeAdvancingState,
          TimeRegulationIsNotEnabled,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.modifyLookahead(theLookahead);
  }

  @Override
  public LogicalTimeInterval queryLookahead()
      throws TimeRegulationIsNotEnabled,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.queryLookahead();
  }

  @Override
  public void retract(MessageRetractionHandle theHandle)
      throws MessageCanNoLongerBeRetracted,
          InvalidMessageRetractionHandle,
          TimeRegulationIsNotEnabled,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.retract(theHandle);
  }

  @Override
  public void changeAttributeOrderType(
      ObjectInstanceHandle theObject, AttributeHandleSet theAttributes, OrderType theType)
      throws AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.changeAttributeOrderType(theObject, theAttributes, theType);
  }

  @Override
  public void changeInteractionOrderType(InteractionClassHandle theClass, OrderType theType)
      throws InteractionClassNotPublished,
          InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.changeInteractionOrderType(theClass, theType);
  }

  //////////////////////////////////
  // Data Distribution Management //
  //////////////////////////////////
  @Override
  public RegionHandle createRegion(DimensionHandleSet dimensions)
      throws InvalidDimensionHandle,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.createRegion(dimensions);
  }

  @Override
  public void commitRegionModifications(RegionHandleSet regions)
      throws RegionNotCreatedByThisFederate,
          InvalidRegion,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.commitRegionModifications(regions);
  }

  @Override
  public void deleteRegion(RegionHandle theRegion)
      throws RegionInUseForUpdateOrSubscription,
          RegionNotCreatedByThisFederate,
          InvalidRegion,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.deleteRegion(theRegion);
  }

  @Override
  public ObjectInstanceHandle registerObjectInstanceWithRegions(
      ObjectClassHandle theClass, AttributeSetRegionSetPairList attributesAndRegions)
      throws InvalidRegionContext,
          RegionNotCreatedByThisFederate,
          InvalidRegion,
          AttributeNotPublished,
          ObjectClassNotPublished,
          AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.registerObjectInstanceWithRegions(theClass, attributesAndRegions);
  }

  @Override
  public ObjectInstanceHandle registerObjectInstanceWithRegions(
      ObjectClassHandle theClass,
      AttributeSetRegionSetPairList attributesAndRegions,
      String theObject)
      throws ObjectInstanceNameInUse,
          ObjectInstanceNameNotReserved,
          InvalidRegionContext,
          RegionNotCreatedByThisFederate,
          InvalidRegion,
          AttributeNotPublished,
          ObjectClassNotPublished,
          AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.registerObjectInstanceWithRegions(theClass, attributesAndRegions, theObject);
  }

  @Override
  public void associateRegionsForUpdates(
      ObjectInstanceHandle theObject, AttributeSetRegionSetPairList attributesAndRegions)
      throws InvalidRegionContext,
          RegionNotCreatedByThisFederate,
          InvalidRegion,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.associateRegionsForUpdates(theObject, attributesAndRegions);
  }

  @Override
  public void unassociateRegionsForUpdates(
      ObjectInstanceHandle theObject, AttributeSetRegionSetPairList attributesAndRegions)
      throws RegionNotCreatedByThisFederate,
          InvalidRegion,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.unassociateRegionsForUpdates(theObject, attributesAndRegions);
  }

  @Override
  public void subscribeObjectClassAttributesWithRegions(
      ObjectClassHandle theClass, AttributeSetRegionSetPairList attributesAndRegions)
      throws InvalidRegionContext,
          RegionNotCreatedByThisFederate,
          InvalidRegion,
          AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.subscribeObjectClassAttributesWithRegions(theClass, attributesAndRegions);
  }

  @Override
  public void subscribeObjectClassAttributesWithRegions(
      ObjectClassHandle theClass,
      AttributeSetRegionSetPairList attributesAndRegions,
      String updateRateDesignator)
      throws InvalidRegionContext,
          RegionNotCreatedByThisFederate,
          InvalidRegion,
          AttributeNotDefined,
          ObjectClassNotDefined,
          InvalidUpdateRateDesignator,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.subscribeObjectClassAttributesWithRegions(
        theClass, attributesAndRegions, updateRateDesignator);
  }

  @Override
  public void subscribeObjectClassAttributesPassivelyWithRegions(
      ObjectClassHandle theClass, AttributeSetRegionSetPairList attributesAndRegions)
      throws InvalidRegionContext,
          RegionNotCreatedByThisFederate,
          InvalidRegion,
          AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.subscribeObjectClassAttributesPassivelyWithRegions(theClass, attributesAndRegions);
  }

  @Override
  public void subscribeObjectClassAttributesPassivelyWithRegions(
      ObjectClassHandle theClass,
      AttributeSetRegionSetPairList attributesAndRegions,
      String updateRateDesignator)
      throws InvalidRegionContext,
          RegionNotCreatedByThisFederate,
          InvalidRegion,
          AttributeNotDefined,
          ObjectClassNotDefined,
          InvalidUpdateRateDesignator,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.subscribeObjectClassAttributesPassivelyWithRegions(
        theClass, attributesAndRegions, updateRateDesignator);
  }

  @Override
  public void unsubscribeObjectClassAttributesWithRegions(
      ObjectClassHandle theClass, AttributeSetRegionSetPairList attributesAndRegions)
      throws RegionNotCreatedByThisFederate,
          InvalidRegion,
          AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.unsubscribeObjectClassAttributesWithRegions(theClass, attributesAndRegions);
  }

  @Override
  public void subscribeInteractionClassWithRegions(
      InteractionClassHandle theClass, RegionHandleSet regions)
      throws FederateServiceInvocationsAreBeingReportedViaMOM,
          InvalidRegionContext,
          RegionNotCreatedByThisFederate,
          InvalidRegion,
          InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.subscribeInteractionClassWithRegions(theClass, regions);
  }

  @Override
  public void subscribeInteractionClassPassivelyWithRegions(
      InteractionClassHandle theClass, RegionHandleSet regions)
      throws FederateServiceInvocationsAreBeingReportedViaMOM,
          InvalidRegionContext,
          RegionNotCreatedByThisFederate,
          InvalidRegion,
          InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.subscribeInteractionClassPassivelyWithRegions(theClass, regions);
  }

  @Override
  public void unsubscribeInteractionClassWithRegions(
      InteractionClassHandle theClass, RegionHandleSet regions)
      throws RegionNotCreatedByThisFederate,
          InvalidRegion,
          InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.unsubscribeInteractionClassWithRegions(theClass, regions);
  }

  @Override
  public void sendInteractionWithRegions(
      InteractionClassHandle theInteraction,
      ParameterHandleValueMap theParameters,
      RegionHandleSet regions,
      byte[] userSuppliedTag)
      throws InvalidRegionContext,
          RegionNotCreatedByThisFederate,
          InvalidRegion,
          InteractionClassNotPublished,
          InteractionParameterNotDefined,
          InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.sendInteractionWithRegions(theInteraction, theParameters, regions, userSuppliedTag);
  }

  @Override
  public MessageRetractionReturn sendInteractionWithRegions(
      InteractionClassHandle theInteraction,
      ParameterHandleValueMap theParameters,
      RegionHandleSet regions,
      byte[] userSuppliedTag,
      LogicalTime theTime)
      throws InvalidLogicalTime,
          InvalidRegionContext,
          RegionNotCreatedByThisFederate,
          InvalidRegion,
          InteractionClassNotPublished,
          InteractionParameterNotDefined,
          InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.sendInteractionWithRegions(
        theInteraction, theParameters, regions, userSuppliedTag, theTime);
  }

  @Override
  public void requestAttributeValueUpdateWithRegions(
      ObjectClassHandle theClass,
      AttributeSetRegionSetPairList attributesAndRegions,
      byte[] userSuppliedTag)
      throws InvalidRegionContext,
          RegionNotCreatedByThisFederate,
          InvalidRegion,
          AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.requestAttributeValueUpdateWithRegions(theClass, attributesAndRegions, userSuppliedTag);
  }

  //////////////////////////
  // RTI Support Services //
  //////////////////////////
  @Override
  public ResignAction getAutomaticResignDirective()
      throws FederateNotExecutionMember, NotConnected, RTIinternalError {
    return rtiamb.getAutomaticResignDirective();
  }

  @Override
  public void setAutomaticResignDirective(ResignAction resignAction)
      throws InvalidResignAction, FederateNotExecutionMember, NotConnected, RTIinternalError {
    rtiamb.setAutomaticResignDirective(resignAction);
  }

  @Override
  public FederateHandle getFederateHandle(String theName)
      throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError {
    return rtiamb.getFederateHandle(theName);
  }

  @Override
  public String getFederateName(FederateHandle theHandle)
      throws InvalidFederateHandle,
          FederateHandleNotKnown,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.getFederateName(theHandle);
  }

  @Override
  public ObjectClassHandle getObjectClassHandle(String theName)
      throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError {
    return rtiamb.getObjectClassHandle(theName);
  }

  @Override
  public String getObjectClassName(ObjectClassHandle theHandle)
      throws InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
    return rtiamb.getObjectClassName(theHandle);
  }

  @Override
  public ObjectClassHandle getKnownObjectClassHandle(ObjectInstanceHandle theObject)
      throws ObjectInstanceNotKnown, FederateNotExecutionMember, NotConnected, RTIinternalError {
    return rtiamb.getKnownObjectClassHandle(theObject);
  }

  @Override
  public ObjectInstanceHandle getObjectInstanceHandle(String theName)
      throws ObjectInstanceNotKnown, FederateNotExecutionMember, NotConnected, RTIinternalError {
    return rtiamb.getObjectInstanceHandle(theName);
  }

  @Override
  public String getObjectInstanceName(ObjectInstanceHandle theHandle)
      throws ObjectInstanceNotKnown, FederateNotExecutionMember, NotConnected, RTIinternalError {
    return rtiamb.getObjectInstanceName(theHandle);
  }

  @Override
  public AttributeHandle getAttributeHandle(ObjectClassHandle whichClass, String theName)
      throws NameNotFound,
          InvalidObjectClassHandle,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.getAttributeHandle(whichClass, theName);
  }

  @Override
  public String getAttributeName(ObjectClassHandle whichClass, AttributeHandle theHandle)
      throws AttributeNotDefined,
          InvalidAttributeHandle,
          InvalidObjectClassHandle,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.getAttributeName(whichClass, theHandle);
  }

  @Override
  public double getUpdateRateValue(String updateRateDesignator)
      throws InvalidUpdateRateDesignator,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.getUpdateRateValue(updateRateDesignator);
  }

  @Override
  public double getUpdateRateValueForAttribute(
      ObjectInstanceHandle theObject, AttributeHandle theAttribute)
      throws ObjectInstanceNotKnown,
          AttributeNotDefined,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.getUpdateRateValueForAttribute(theObject, theAttribute);
  }

  @Override
  public InteractionClassHandle getInteractionClassHandle(String theName)
      throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError {
    return rtiamb.getInteractionClassHandle(theName);
  }

  @Override
  public String getInteractionClassName(InteractionClassHandle theHandle)
      throws InvalidInteractionClassHandle,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.getInteractionClassName(theHandle);
  }

  @Override
  public ParameterHandle getParameterHandle(InteractionClassHandle whichClass, String theName)
      throws NameNotFound,
          InvalidInteractionClassHandle,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.getParameterHandle(whichClass, theName);
  }

  @Override
  public String getParameterName(InteractionClassHandle whichClass, ParameterHandle theHandle)
      throws InteractionParameterNotDefined,
          InvalidParameterHandle,
          InvalidInteractionClassHandle,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.getParameterName(whichClass, theHandle);
  }

  @Override
  public OrderType getOrderType(String theName)
      throws InvalidOrderName, FederateNotExecutionMember, NotConnected, RTIinternalError {
    return rtiamb.getOrderType(theName);
  }

  @Override
  public String getOrderName(OrderType theType)
      throws InvalidOrderType, FederateNotExecutionMember, NotConnected, RTIinternalError {
    return rtiamb.getOrderName(theType);
  }

  @Override
  public TransportationTypeHandle getTransportationTypeHandle(String theName)
      throws InvalidTransportationName, FederateNotExecutionMember, NotConnected, RTIinternalError {
    return rtiamb.getTransportationTypeHandle(theName);
  }

  @Override
  public String getTransportationTypeName(TransportationTypeHandle theHandle)
      throws InvalidTransportationType, FederateNotExecutionMember, NotConnected, RTIinternalError {
    return rtiamb.getTransportationTypeName(theHandle);
  }

  @Override
  public DimensionHandleSet getAvailableDimensionsForClassAttribute(
      ObjectClassHandle whichClass, AttributeHandle theHandle)
      throws AttributeNotDefined,
          InvalidAttributeHandle,
          InvalidObjectClassHandle,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.getAvailableDimensionsForClassAttribute(whichClass, theHandle);
  }

  @Override
  public DimensionHandleSet getAvailableDimensionsForInteractionClass(
      InteractionClassHandle theHandle)
      throws InvalidInteractionClassHandle,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.getAvailableDimensionsForInteractionClass(theHandle);
  }

  @Override
  public DimensionHandle getDimensionHandle(String theName)
      throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError {
    return rtiamb.getDimensionHandle(theName);
  }

  @Override
  public String getDimensionName(DimensionHandle theHandle)
      throws InvalidDimensionHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
    return rtiamb.getDimensionName(theHandle);
  }

  @Override
  public long getDimensionUpperBound(DimensionHandle theHandle)
      throws InvalidDimensionHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
    return rtiamb.getDimensionUpperBound(theHandle);
  }

  @Override
  public DimensionHandleSet getDimensionHandleSet(RegionHandle region)
      throws InvalidRegion,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.getDimensionHandleSet(region);
  }

  @Override
  public RangeBounds getRangeBounds(RegionHandle region, DimensionHandle dimension)
      throws RegionDoesNotContainSpecifiedDimension,
          InvalidRegion,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    return rtiamb.getRangeBounds(region, dimension);
  }

  @Override
  public void setRangeBounds(RegionHandle region, DimensionHandle dimension, RangeBounds bounds)
      throws InvalidRangeBound,
          RegionDoesNotContainSpecifiedDimension,
          RegionNotCreatedByThisFederate,
          InvalidRegion,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.setRangeBounds(region, dimension, bounds);
  }

  @Override
  public long normalizeFederateHandle(FederateHandle federateHandle)
      throws InvalidFederateHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
    return rtiamb.normalizeFederateHandle(federateHandle);
  }

  @Override
  public long normalizeServiceGroup(ServiceGroup group)
      throws InvalidServiceGroup, FederateNotExecutionMember, NotConnected, RTIinternalError {
    return rtiamb.normalizeServiceGroup(group);
  }

  @Override
  public void enableObjectClassRelevanceAdvisorySwitch()
      throws ObjectClassRelevanceAdvisorySwitchIsOn,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.enableObjectClassRelevanceAdvisorySwitch();
  }

  @Override
  public void disableObjectClassRelevanceAdvisorySwitch()
      throws ObjectClassRelevanceAdvisorySwitchIsOff,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.disableObjectClassRelevanceAdvisorySwitch();
  }

  @Override
  public void enableAttributeRelevanceAdvisorySwitch()
      throws AttributeRelevanceAdvisorySwitchIsOn,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.enableAttributeRelevanceAdvisorySwitch();
  }

  @Override
  public void disableAttributeRelevanceAdvisorySwitch()
      throws AttributeRelevanceAdvisorySwitchIsOff,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.disableAttributeRelevanceAdvisorySwitch();
  }

  @Override
  public void enableAttributeScopeAdvisorySwitch()
      throws AttributeScopeAdvisorySwitchIsOn,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.enableAttributeScopeAdvisorySwitch();
  }

  @Override
  public void disableAttributeScopeAdvisorySwitch()
      throws AttributeScopeAdvisorySwitchIsOff,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.disableAttributeScopeAdvisorySwitch();
  }

  @Override
  public void enableInteractionRelevanceAdvisorySwitch()
      throws InteractionRelevanceAdvisorySwitchIsOn,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.enableInteractionRelevanceAdvisorySwitch();
  }

  @Override
  public void disableInteractionRelevanceAdvisorySwitch()
      throws InteractionRelevanceAdvisorySwitchIsOff,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    rtiamb.disableInteractionRelevanceAdvisorySwitch();
  }

  @Override
  public boolean evokeCallback(double approximateMinimumTimeInSeconds)
      throws CallNotAllowedFromWithinCallback, RTIinternalError {
    return rtiamb.evokeCallback(approximateMinimumTimeInSeconds);
  }

  @Override
  public boolean evokeMultipleCallbacks(
      double approximateMinimumTimeInSeconds, double approximateMaximumTimeInSeconds)
      throws CallNotAllowedFromWithinCallback, RTIinternalError {
    return rtiamb.evokeMultipleCallbacks(
        approximateMinimumTimeInSeconds, approximateMaximumTimeInSeconds);
  }

  @Override
  public void enableCallbacks() throws SaveInProgress, RestoreInProgress, RTIinternalError {
    rtiamb.enableCallbacks();
  }

  @Override
  public void disableCallbacks() throws SaveInProgress, RestoreInProgress, RTIinternalError {
    rtiamb.disableCallbacks();
  }

  @Override
  public AttributeHandleFactory getAttributeHandleFactory()
      throws FederateNotExecutionMember, NotConnected {
    return rtiamb.getAttributeHandleFactory();
  }

  @Override
  public AttributeHandleSetFactory getAttributeHandleSetFactory()
      throws FederateNotExecutionMember, NotConnected {
    return rtiamb.getAttributeHandleSetFactory();
  }

  @Override
  public AttributeHandleValueMapFactory getAttributeHandleValueMapFactory()
      throws FederateNotExecutionMember, NotConnected {
    return rtiamb.getAttributeHandleValueMapFactory();
  }

  @Override
  public AttributeSetRegionSetPairListFactory getAttributeSetRegionSetPairListFactory()
      throws FederateNotExecutionMember, NotConnected {
    return rtiamb.getAttributeSetRegionSetPairListFactory();
  }

  @Override
  public DimensionHandleFactory getDimensionHandleFactory()
      throws FederateNotExecutionMember, NotConnected {
    return rtiamb.getDimensionHandleFactory();
  }

  @Override
  public DimensionHandleSetFactory getDimensionHandleSetFactory()
      throws FederateNotExecutionMember, NotConnected {
    return rtiamb.getDimensionHandleSetFactory();
  }

  @Override
  public FederateHandleFactory getFederateHandleFactory()
      throws FederateNotExecutionMember, NotConnected {
    return rtiamb.getFederateHandleFactory();
  }

  @Override
  public FederateHandleSetFactory getFederateHandleSetFactory()
      throws FederateNotExecutionMember, NotConnected {
    return rtiamb.getFederateHandleSetFactory();
  }

  @Override
  public InteractionClassHandleFactory getInteractionClassHandleFactory()
      throws FederateNotExecutionMember, NotConnected {
    return rtiamb.getInteractionClassHandleFactory();
  }

  @Override
  public ObjectClassHandleFactory getObjectClassHandleFactory()
      throws FederateNotExecutionMember, NotConnected {
    return rtiamb.getObjectClassHandleFactory();
  }

  @Override
  public ObjectInstanceHandleFactory getObjectInstanceHandleFactory()
      throws FederateNotExecutionMember, NotConnected {
    return rtiamb.getObjectInstanceHandleFactory();
  }

  @Override
  public ParameterHandleFactory getParameterHandleFactory()
      throws FederateNotExecutionMember, NotConnected {
    return rtiamb.getParameterHandleFactory();
  }

  @Override
  public ParameterHandleValueMapFactory getParameterHandleValueMapFactory()
      throws FederateNotExecutionMember, NotConnected {
    return rtiamb.getParameterHandleValueMapFactory();
  }

  @Override
  public RegionHandleSetFactory getRegionHandleSetFactory()
      throws FederateNotExecutionMember, NotConnected {
    return rtiamb.getRegionHandleSetFactory();
  }

  @Override
  public TransportationTypeHandleFactory getTransportationTypeHandleFactory()
      throws FederateNotExecutionMember, NotConnected {
    return rtiamb.getTransportationTypeHandleFactory();
  }

  @Override
  public String getHLAversion() {
    return rtiamb.getHLAversion();
  }

  @Override
  public LogicalTimeFactory getTimeFactory() throws FederateNotExecutionMember, NotConnected {
    return rtiamb.getTimeFactory();
  }
}
