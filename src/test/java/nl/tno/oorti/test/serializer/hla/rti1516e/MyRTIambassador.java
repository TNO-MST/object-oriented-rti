package nl.tno.oorti.test.serializer.hla.rti1516e;

import hla.rti1516e.*;
import hla.rti1516e.exceptions.*;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A bare bone RTI ambassador with enough interfaces and methods implemented to
 * perform the Serializer tests.
 *
 * @author bergtwvd
 */
public class MyRTIambassador implements RTIambassador {

	MyAttributeHandleValueMapFactory myAttributeHandleValueMapFactory = new MyAttributeHandleValueMapFactory();
	MyParameterHandleValueMapFactory myParameterHandleValueMapFactory = new MyParameterHandleValueMapFactory();
	MyAttributeHandleSetFactory myAttributeHandleSetFactory = new MyAttributeHandleSetFactory();

	Map<String, ObjectClassHandle> name2ObjectClassHandle = new HashMap<>();
	Map<String, InteractionClassHandle> name2InteractionClassHandle = new HashMap<>();
	Map<SimpleEntry, AttributeHandle> entry2AttributeHandle = new HashMap<>();
	Map<SimpleEntry, ParameterHandle> entry2ParameterHandle = new HashMap<>();
	Map<String, ObjectInstanceHandle> name2ObjectInstanceHandle = new HashMap<>();

	int nameCounter = 0;

	@Override
	public void connect(FederateAmbassador federateReference, CallbackModel callbackModel, String localSettingsDesignator) throws ConnectionFailed, InvalidLocalSettingsDesignator, UnsupportedCallbackModel, AlreadyConnected, CallNotAllowedFromWithinCallback, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void connect(FederateAmbassador federateReference, CallbackModel callbackModel) throws ConnectionFailed, InvalidLocalSettingsDesignator, UnsupportedCallbackModel, AlreadyConnected, CallNotAllowedFromWithinCallback, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void disconnect() throws FederateIsExecutionMember, CallNotAllowedFromWithinCallback, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void createFederationExecution(String federationExecutionName, URL[] fomModules, URL mimModule, String logicalTimeImplementationName) throws CouldNotCreateLogicalTimeFactory, InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, ErrorReadingMIM, CouldNotOpenMIM, DesignatorIsHLAstandardMIM, FederationExecutionAlreadyExists, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void createFederationExecution(String federationExecutionName, URL[] fomModules, String logicalTimeImplementationName) throws CouldNotCreateLogicalTimeFactory, InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, FederationExecutionAlreadyExists, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void createFederationExecution(String federationExecutionName, URL[] fomModules, URL mimModule) throws InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, ErrorReadingMIM, CouldNotOpenMIM, DesignatorIsHLAstandardMIM, FederationExecutionAlreadyExists, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void createFederationExecution(String federationExecutionName, URL[] fomModules) throws InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, FederationExecutionAlreadyExists, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void createFederationExecution(String federationExecutionName, URL fomModule) throws InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, FederationExecutionAlreadyExists, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void destroyFederationExecution(String federationExecutionName) throws FederatesCurrentlyJoined, FederationExecutionDoesNotExist, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void listFederationExecutions() throws NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public FederateHandle joinFederationExecution(String federateName, String federateType, String federationExecutionName, URL[] additionalFomModules) throws CouldNotCreateLogicalTimeFactory, FederateNameAlreadyInUse, FederationExecutionDoesNotExist, InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, SaveInProgress, RestoreInProgress, FederateAlreadyExecutionMember, NotConnected, CallNotAllowedFromWithinCallback, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public FederateHandle joinFederationExecution(String federateType, String federationExecutionName, URL[] additionalFomModules) throws CouldNotCreateLogicalTimeFactory, FederationExecutionDoesNotExist, InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, SaveInProgress, RestoreInProgress, FederateAlreadyExecutionMember, NotConnected, CallNotAllowedFromWithinCallback, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public FederateHandle joinFederationExecution(String federateName, String federateType, String federationExecutionName) throws CouldNotCreateLogicalTimeFactory, FederateNameAlreadyInUse, FederationExecutionDoesNotExist, SaveInProgress, RestoreInProgress, FederateAlreadyExecutionMember, NotConnected, CallNotAllowedFromWithinCallback, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public FederateHandle joinFederationExecution(String federateType, String federationExecutionName) throws CouldNotCreateLogicalTimeFactory, FederationExecutionDoesNotExist, SaveInProgress, RestoreInProgress, FederateAlreadyExecutionMember, NotConnected, CallNotAllowedFromWithinCallback, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void resignFederationExecution(ResignAction resignAction) throws InvalidResignAction, OwnershipAcquisitionPending, FederateOwnsAttributes, FederateNotExecutionMember, NotConnected, CallNotAllowedFromWithinCallback, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void registerFederationSynchronizationPoint(String synchronizationPointLabel, byte[] userSuppliedTag) throws SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void registerFederationSynchronizationPoint(String synchronizationPointLabel, byte[] userSuppliedTag, FederateHandleSet synchronizationSet) throws InvalidFederateHandle, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void synchronizationPointAchieved(String synchronizationPointLabel) throws SynchronizationPointLabelNotAnnounced, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void synchronizationPointAchieved(String synchronizationPointLabel, boolean successIndicator) throws SynchronizationPointLabelNotAnnounced, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void requestFederationSave(String label) throws SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void requestFederationSave(String label, LogicalTime theTime) throws LogicalTimeAlreadyPassed, InvalidLogicalTime, FederateUnableToUseTime, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void federateSaveBegun() throws SaveNotInitiated, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void federateSaveComplete() throws FederateHasNotBegunSave, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void federateSaveNotComplete() throws FederateHasNotBegunSave, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void abortFederationSave() throws SaveNotInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void queryFederationSaveStatus() throws RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void requestFederationRestore(String label) throws SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void federateRestoreComplete() throws RestoreNotRequested, SaveInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void federateRestoreNotComplete() throws RestoreNotRequested, SaveInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void abortFederationRestore() throws RestoreNotInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void queryFederationRestoreStatus() throws SaveInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void publishObjectClassAttributes(ObjectClassHandle theClass, AttributeHandleSet attributeList) throws AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void unpublishObjectClass(ObjectClassHandle theClass) throws OwnershipAcquisitionPending, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void unpublishObjectClassAttributes(ObjectClassHandle theClass, AttributeHandleSet attributeList) throws OwnershipAcquisitionPending, AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void publishInteractionClass(InteractionClassHandle theInteraction) throws InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void unpublishInteractionClass(InteractionClassHandle theInteraction) throws InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void subscribeObjectClassAttributes(ObjectClassHandle theClass, AttributeHandleSet attributeList) throws AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void subscribeObjectClassAttributes(ObjectClassHandle theClass, AttributeHandleSet attributeList, String updateRateDesignator) throws AttributeNotDefined, ObjectClassNotDefined, InvalidUpdateRateDesignator, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void subscribeObjectClassAttributesPassively(ObjectClassHandle theClass, AttributeHandleSet attributeList) throws AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void subscribeObjectClassAttributesPassively(ObjectClassHandle theClass, AttributeHandleSet attributeList, String updateRateDesignator) throws AttributeNotDefined, ObjectClassNotDefined, InvalidUpdateRateDesignator, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void unsubscribeObjectClass(ObjectClassHandle theClass) throws ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void unsubscribeObjectClassAttributes(ObjectClassHandle theClass, AttributeHandleSet attributeList) throws AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void subscribeInteractionClass(InteractionClassHandle theClass) throws FederateServiceInvocationsAreBeingReportedViaMOM, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void subscribeInteractionClassPassively(InteractionClassHandle theClass) throws FederateServiceInvocationsAreBeingReportedViaMOM, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void unsubscribeInteractionClass(InteractionClassHandle theClass) throws InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void reserveObjectInstanceName(String theObjectName) throws IllegalName, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void releaseObjectInstanceName(String theObjectInstanceName) throws ObjectInstanceNameNotReserved, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void reserveMultipleObjectInstanceName(Set<String> theObjectNames) throws IllegalName, NameSetWasEmpty, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void releaseMultipleObjectInstanceName(Set<String> theObjectNames) throws ObjectInstanceNameNotReserved, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ObjectInstanceHandle registerObjectInstance(ObjectClassHandle theClass) throws ObjectClassNotPublished, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		ObjectInstanceHandle h = new MyObjectInstanceHandle();
		this.name2ObjectInstanceHandle.put("MyName-" + this.nameCounter++, h);
		return h;
	}

	@Override
	public ObjectInstanceHandle registerObjectInstance(ObjectClassHandle theClass, String theObjectName) throws ObjectInstanceNameInUse, ObjectInstanceNameNotReserved, ObjectClassNotPublished, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		ObjectInstanceHandle h = this.name2ObjectInstanceHandle.get(theObjectName);
		if (h != null) {
			throw new ObjectInstanceNameInUse("Name already taken");
		}

		h = new MyObjectInstanceHandle();
		this.name2ObjectInstanceHandle.put(theObjectName, h);
		return h;
	}

	@Override
	public void updateAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes, byte[] userSuppliedTag) throws AttributeNotOwned, AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public MessageRetractionReturn updateAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes, byte[] userSuppliedTag, LogicalTime theTime) throws InvalidLogicalTime, AttributeNotOwned, AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void sendInteraction(InteractionClassHandle theInteraction, ParameterHandleValueMap theParameters, byte[] userSuppliedTag) throws InteractionClassNotPublished, InteractionParameterNotDefined, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public MessageRetractionReturn sendInteraction(InteractionClassHandle theInteraction, ParameterHandleValueMap theParameters, byte[] userSuppliedTag, LogicalTime theTime) throws InvalidLogicalTime, InteractionClassNotPublished, InteractionParameterNotDefined, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void deleteObjectInstance(ObjectInstanceHandle objectHandle, byte[] userSuppliedTag) throws DeletePrivilegeNotHeld, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public MessageRetractionReturn deleteObjectInstance(ObjectInstanceHandle objectHandle, byte[] userSuppliedTag, LogicalTime theTime) throws InvalidLogicalTime, DeletePrivilegeNotHeld, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void localDeleteObjectInstance(ObjectInstanceHandle objectHandle) throws OwnershipAcquisitionPending, FederateOwnsAttributes, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void requestAttributeValueUpdate(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes, byte[] userSuppliedTag) throws AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void requestAttributeValueUpdate(ObjectClassHandle theClass, AttributeHandleSet theAttributes, byte[] userSuppliedTag) throws AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void requestAttributeTransportationTypeChange(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes, TransportationTypeHandle theType) throws AttributeAlreadyBeingChanged, AttributeNotOwned, AttributeNotDefined, ObjectInstanceNotKnown, InvalidTransportationType, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void queryAttributeTransportationType(ObjectInstanceHandle theObject, AttributeHandle theAttribute) throws AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void requestInteractionTransportationTypeChange(InteractionClassHandle theClass, TransportationTypeHandle theType) throws InteractionClassAlreadyBeingChanged, InteractionClassNotPublished, InteractionClassNotDefined, InvalidTransportationType, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void queryInteractionTransportationType(FederateHandle theFederate, InteractionClassHandle theInteraction) throws InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void unconditionalAttributeOwnershipDivestiture(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws AttributeNotOwned, AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void negotiatedAttributeOwnershipDivestiture(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes, byte[] userSuppliedTag) throws AttributeAlreadyBeingDivested, AttributeNotOwned, AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void confirmDivestiture(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes, byte[] userSuppliedTag) throws NoAcquisitionPending, AttributeDivestitureWasNotRequested, AttributeNotOwned, AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void attributeOwnershipAcquisition(ObjectInstanceHandle theObject, AttributeHandleSet desiredAttributes, byte[] userSuppliedTag) throws AttributeNotPublished, ObjectClassNotPublished, FederateOwnsAttributes, AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void attributeOwnershipAcquisitionIfAvailable(ObjectInstanceHandle theObject, AttributeHandleSet desiredAttributes) throws AttributeAlreadyBeingAcquired, AttributeNotPublished, ObjectClassNotPublished, FederateOwnsAttributes, AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void attributeOwnershipReleaseDenied(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws AttributeNotOwned, AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public AttributeHandleSet attributeOwnershipDivestitureIfWanted(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws AttributeNotOwned, AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void cancelNegotiatedAttributeOwnershipDivestiture(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws AttributeDivestitureWasNotRequested, AttributeNotOwned, AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void cancelAttributeOwnershipAcquisition(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws AttributeAcquisitionWasNotRequested, AttributeAlreadyOwned, AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void queryAttributeOwnership(ObjectInstanceHandle theObject, AttributeHandle theAttribute) throws AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isAttributeOwnedByFederate(ObjectInstanceHandle theObject, AttributeHandle theAttribute) throws AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void enableTimeRegulation(LogicalTimeInterval theLookahead) throws InvalidLookahead, InTimeAdvancingState, RequestForTimeRegulationPending, TimeRegulationAlreadyEnabled, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void disableTimeRegulation() throws TimeRegulationIsNotEnabled, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void enableTimeConstrained() throws InTimeAdvancingState, RequestForTimeConstrainedPending, TimeConstrainedAlreadyEnabled, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void disableTimeConstrained() throws TimeConstrainedIsNotEnabled, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void timeAdvanceRequest(LogicalTime theTime) throws LogicalTimeAlreadyPassed, InvalidLogicalTime, InTimeAdvancingState, RequestForTimeRegulationPending, RequestForTimeConstrainedPending, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void timeAdvanceRequestAvailable(LogicalTime theTime) throws LogicalTimeAlreadyPassed, InvalidLogicalTime, InTimeAdvancingState, RequestForTimeRegulationPending, RequestForTimeConstrainedPending, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void nextMessageRequest(LogicalTime theTime) throws LogicalTimeAlreadyPassed, InvalidLogicalTime, InTimeAdvancingState, RequestForTimeRegulationPending, RequestForTimeConstrainedPending, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void nextMessageRequestAvailable(LogicalTime theTime) throws LogicalTimeAlreadyPassed, InvalidLogicalTime, InTimeAdvancingState, RequestForTimeRegulationPending, RequestForTimeConstrainedPending, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void flushQueueRequest(LogicalTime theTime) throws LogicalTimeAlreadyPassed, InvalidLogicalTime, InTimeAdvancingState, RequestForTimeRegulationPending, RequestForTimeConstrainedPending, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void enableAsynchronousDelivery() throws AsynchronousDeliveryAlreadyEnabled, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void disableAsynchronousDelivery() throws AsynchronousDeliveryAlreadyDisabled, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public TimeQueryReturn queryGALT() throws SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public LogicalTime queryLogicalTime() throws SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public TimeQueryReturn queryLITS() throws SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void modifyLookahead(LogicalTimeInterval theLookahead) throws InvalidLookahead, InTimeAdvancingState, TimeRegulationIsNotEnabled, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public LogicalTimeInterval queryLookahead() throws TimeRegulationIsNotEnabled, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void retract(MessageRetractionHandle theHandle) throws MessageCanNoLongerBeRetracted, InvalidMessageRetractionHandle, TimeRegulationIsNotEnabled, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void changeAttributeOrderType(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes, OrderType theType) throws AttributeNotOwned, AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void changeInteractionOrderType(InteractionClassHandle theClass, OrderType theType) throws InteractionClassNotPublished, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public RegionHandle createRegion(DimensionHandleSet dimensions) throws InvalidDimensionHandle, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void commitRegionModifications(RegionHandleSet regions) throws RegionNotCreatedByThisFederate, InvalidRegion, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void deleteRegion(RegionHandle theRegion) throws RegionInUseForUpdateOrSubscription, RegionNotCreatedByThisFederate, InvalidRegion, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ObjectInstanceHandle registerObjectInstanceWithRegions(ObjectClassHandle theClass, AttributeSetRegionSetPairList attributesAndRegions) throws InvalidRegionContext, RegionNotCreatedByThisFederate, InvalidRegion, AttributeNotPublished, ObjectClassNotPublished, AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ObjectInstanceHandle registerObjectInstanceWithRegions(ObjectClassHandle theClass, AttributeSetRegionSetPairList attributesAndRegions, String theObject) throws ObjectInstanceNameInUse, ObjectInstanceNameNotReserved, InvalidRegionContext, RegionNotCreatedByThisFederate, InvalidRegion, AttributeNotPublished, ObjectClassNotPublished, AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void associateRegionsForUpdates(ObjectInstanceHandle theObject, AttributeSetRegionSetPairList attributesAndRegions) throws InvalidRegionContext, RegionNotCreatedByThisFederate, InvalidRegion, AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void unassociateRegionsForUpdates(ObjectInstanceHandle theObject, AttributeSetRegionSetPairList attributesAndRegions) throws RegionNotCreatedByThisFederate, InvalidRegion, AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void subscribeObjectClassAttributesWithRegions(ObjectClassHandle theClass, AttributeSetRegionSetPairList attributesAndRegions) throws InvalidRegionContext, RegionNotCreatedByThisFederate, InvalidRegion, AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void subscribeObjectClassAttributesWithRegions(ObjectClassHandle theClass, AttributeSetRegionSetPairList attributesAndRegions, String updateRateDesignator) throws InvalidRegionContext, RegionNotCreatedByThisFederate, InvalidRegion, AttributeNotDefined, ObjectClassNotDefined, InvalidUpdateRateDesignator, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void subscribeObjectClassAttributesPassivelyWithRegions(ObjectClassHandle theClass, AttributeSetRegionSetPairList attributesAndRegions) throws InvalidRegionContext, RegionNotCreatedByThisFederate, InvalidRegion, AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void subscribeObjectClassAttributesPassivelyWithRegions(ObjectClassHandle theClass, AttributeSetRegionSetPairList attributesAndRegions, String updateRateDesignator) throws InvalidRegionContext, RegionNotCreatedByThisFederate, InvalidRegion, AttributeNotDefined, ObjectClassNotDefined, InvalidUpdateRateDesignator, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void unsubscribeObjectClassAttributesWithRegions(ObjectClassHandle theClass, AttributeSetRegionSetPairList attributesAndRegions) throws RegionNotCreatedByThisFederate, InvalidRegion, AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void subscribeInteractionClassWithRegions(InteractionClassHandle theClass, RegionHandleSet regions) throws FederateServiceInvocationsAreBeingReportedViaMOM, InvalidRegionContext, RegionNotCreatedByThisFederate, InvalidRegion, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void subscribeInteractionClassPassivelyWithRegions(InteractionClassHandle theClass, RegionHandleSet regions) throws FederateServiceInvocationsAreBeingReportedViaMOM, InvalidRegionContext, RegionNotCreatedByThisFederate, InvalidRegion, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void unsubscribeInteractionClassWithRegions(InteractionClassHandle theClass, RegionHandleSet regions) throws RegionNotCreatedByThisFederate, InvalidRegion, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void sendInteractionWithRegions(InteractionClassHandle theInteraction, ParameterHandleValueMap theParameters, RegionHandleSet regions, byte[] userSuppliedTag) throws InvalidRegionContext, RegionNotCreatedByThisFederate, InvalidRegion, InteractionClassNotPublished, InteractionParameterNotDefined, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public MessageRetractionReturn sendInteractionWithRegions(InteractionClassHandle theInteraction, ParameterHandleValueMap theParameters, RegionHandleSet regions, byte[] userSuppliedTag, LogicalTime theTime) throws InvalidLogicalTime, InvalidRegionContext, RegionNotCreatedByThisFederate, InvalidRegion, InteractionClassNotPublished, InteractionParameterNotDefined, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void requestAttributeValueUpdateWithRegions(ObjectClassHandle theClass, AttributeSetRegionSetPairList attributesAndRegions, byte[] userSuppliedTag) throws InvalidRegionContext, RegionNotCreatedByThisFederate, InvalidRegion, AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ResignAction getAutomaticResignDirective() throws FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setAutomaticResignDirective(ResignAction resignAction) throws InvalidResignAction, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public FederateHandle getFederateHandle(String theName) throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getFederateName(FederateHandle theHandle) throws InvalidFederateHandle, FederateHandleNotKnown, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ObjectClassHandle getObjectClassHandle(String theName) throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError {
		ObjectClassHandle h = this.name2ObjectClassHandle.get(theName);
		if (h == null) {
			h = new MyObjectClassHandle();
			this.name2ObjectClassHandle.put(theName, h);
		}
		return h;
	}

	@Override
	public String getObjectClassName(ObjectClassHandle theHandle) throws InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ObjectClassHandle getKnownObjectClassHandle(ObjectInstanceHandle theObject) throws ObjectInstanceNotKnown, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ObjectInstanceHandle getObjectInstanceHandle(String theName) throws ObjectInstanceNotKnown, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getObjectInstanceName(ObjectInstanceHandle theHandle) throws ObjectInstanceNotKnown, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public AttributeHandle getAttributeHandle(ObjectClassHandle whichClass, String theName) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
		SimpleEntry<ObjectClassHandle, String> entry = new SimpleEntry<>(whichClass, theName);
		AttributeHandle h = this.entry2AttributeHandle.get(entry);
		if (h == null) {
			h = new MyAttributeHandle();
			this.entry2AttributeHandle.put(entry, h);
		}
		return h;
	}

	@Override
	public String getAttributeName(ObjectClassHandle whichClass, AttributeHandle theHandle) throws AttributeNotDefined, InvalidAttributeHandle, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public double getUpdateRateValue(String updateRateDesignator) throws InvalidUpdateRateDesignator, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public double getUpdateRateValueForAttribute(ObjectInstanceHandle theObject, AttributeHandle theAttribute) throws ObjectInstanceNotKnown, AttributeNotDefined, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public InteractionClassHandle getInteractionClassHandle(String theName) throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError {
		InteractionClassHandle h = this.name2InteractionClassHandle.get(theName);
		if (h == null) {
			h = new MyInteractionClassHandle();
			this.name2InteractionClassHandle.put(theName, h);
		}
		return h;
	}

	@Override
	public String getInteractionClassName(InteractionClassHandle theHandle) throws InvalidInteractionClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ParameterHandle getParameterHandle(InteractionClassHandle whichClass, String theName) throws NameNotFound, InvalidInteractionClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
		SimpleEntry<InteractionClassHandle, String> entry = new SimpleEntry<>(whichClass, theName);
		ParameterHandle h = this.entry2ParameterHandle.get(entry);
		if (h == null) {
			h = new MyParameterHandle();
			this.entry2ParameterHandle.put(entry, h);
		}
		return h;
	}

	@Override
	public String getParameterName(InteractionClassHandle whichClass, ParameterHandle theHandle) throws InteractionParameterNotDefined, InvalidParameterHandle, InvalidInteractionClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public OrderType getOrderType(String theName) throws InvalidOrderName, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getOrderName(OrderType theType) throws InvalidOrderType, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public TransportationTypeHandle getTransportationTypeHandle(String theName) throws InvalidTransportationName, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getTransportationTypeName(TransportationTypeHandle theHandle) throws InvalidTransportationType, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public DimensionHandleSet getAvailableDimensionsForClassAttribute(ObjectClassHandle whichClass, AttributeHandle theHandle) throws AttributeNotDefined, InvalidAttributeHandle, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public DimensionHandleSet getAvailableDimensionsForInteractionClass(InteractionClassHandle theHandle) throws InvalidInteractionClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public DimensionHandle getDimensionHandle(String theName) throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getDimensionName(DimensionHandle theHandle) throws InvalidDimensionHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public long getDimensionUpperBound(DimensionHandle theHandle) throws InvalidDimensionHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public DimensionHandleSet getDimensionHandleSet(RegionHandle region) throws InvalidRegion, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public RangeBounds getRangeBounds(RegionHandle region, DimensionHandle dimension) throws RegionDoesNotContainSpecifiedDimension, InvalidRegion, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setRangeBounds(RegionHandle region, DimensionHandle dimension, RangeBounds bounds) throws InvalidRangeBound, RegionDoesNotContainSpecifiedDimension, RegionNotCreatedByThisFederate, InvalidRegion, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public long normalizeFederateHandle(FederateHandle federateHandle) throws InvalidFederateHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public long normalizeServiceGroup(ServiceGroup group) throws InvalidServiceGroup, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void enableObjectClassRelevanceAdvisorySwitch() throws ObjectClassRelevanceAdvisorySwitchIsOn, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void disableObjectClassRelevanceAdvisorySwitch() throws ObjectClassRelevanceAdvisorySwitchIsOff, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void enableAttributeRelevanceAdvisorySwitch() throws AttributeRelevanceAdvisorySwitchIsOn, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void disableAttributeRelevanceAdvisorySwitch() throws AttributeRelevanceAdvisorySwitchIsOff, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void enableAttributeScopeAdvisorySwitch() throws AttributeScopeAdvisorySwitchIsOn, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void disableAttributeScopeAdvisorySwitch() throws AttributeScopeAdvisorySwitchIsOff, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void enableInteractionRelevanceAdvisorySwitch() throws InteractionRelevanceAdvisorySwitchIsOn, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void disableInteractionRelevanceAdvisorySwitch() throws InteractionRelevanceAdvisorySwitchIsOff, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean evokeCallback(double approximateMinimumTimeInSeconds) throws CallNotAllowedFromWithinCallback, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean evokeMultipleCallbacks(double approximateMinimumTimeInSeconds, double approximateMaximumTimeInSeconds) throws CallNotAllowedFromWithinCallback, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void enableCallbacks() throws SaveInProgress, RestoreInProgress, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void disableCallbacks() throws SaveInProgress, RestoreInProgress, RTIinternalError {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public AttributeHandleFactory getAttributeHandleFactory() throws FederateNotExecutionMember, NotConnected {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public AttributeHandleSetFactory getAttributeHandleSetFactory() throws FederateNotExecutionMember, NotConnected {
		return this.myAttributeHandleSetFactory;
	}

	@Override
	public AttributeHandleValueMapFactory getAttributeHandleValueMapFactory() throws FederateNotExecutionMember, NotConnected {
		return this.myAttributeHandleValueMapFactory;
	}

	@Override
	public AttributeSetRegionSetPairListFactory getAttributeSetRegionSetPairListFactory() throws FederateNotExecutionMember, NotConnected {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public DimensionHandleFactory getDimensionHandleFactory() throws FederateNotExecutionMember, NotConnected {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public DimensionHandleSetFactory getDimensionHandleSetFactory() throws FederateNotExecutionMember, NotConnected {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public FederateHandleFactory getFederateHandleFactory() throws FederateNotExecutionMember, NotConnected {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public FederateHandleSetFactory getFederateHandleSetFactory() throws FederateNotExecutionMember, NotConnected {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public InteractionClassHandleFactory getInteractionClassHandleFactory() throws FederateNotExecutionMember, NotConnected {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ObjectClassHandleFactory getObjectClassHandleFactory() throws FederateNotExecutionMember, NotConnected {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ObjectInstanceHandleFactory getObjectInstanceHandleFactory() throws FederateNotExecutionMember, NotConnected {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ParameterHandleFactory getParameterHandleFactory() throws FederateNotExecutionMember, NotConnected {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ParameterHandleValueMapFactory getParameterHandleValueMapFactory() throws FederateNotExecutionMember, NotConnected {
		return this.myParameterHandleValueMapFactory;
	}

	@Override
	public RegionHandleSetFactory getRegionHandleSetFactory() throws FederateNotExecutionMember, NotConnected {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public TransportationTypeHandleFactory getTransportationTypeHandleFactory() throws FederateNotExecutionMember, NotConnected {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getHLAversion() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public LogicalTimeFactory getTimeFactory() throws FederateNotExecutionMember, NotConnected {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
