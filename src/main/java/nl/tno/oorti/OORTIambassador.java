package nl.tno.oorti;

import hla.rti1516e.CallbackModel;
import hla.rti1516e.FederateHandle;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.MessageRetractionReturn;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.exceptions.AlreadyConnected;
import hla.rti1516e.exceptions.AttributeAcquisitionWasNotRequested;
import hla.rti1516e.exceptions.AttributeAlreadyBeingAcquired;
import hla.rti1516e.exceptions.AttributeAlreadyBeingDivested;
import hla.rti1516e.exceptions.AttributeAlreadyOwned;
import hla.rti1516e.exceptions.AttributeDivestitureWasNotRequested;
import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.AttributeNotOwned;
import hla.rti1516e.exceptions.AttributeNotPublished;
import hla.rti1516e.exceptions.CallNotAllowedFromWithinCallback;
import hla.rti1516e.exceptions.ConnectionFailed;
import hla.rti1516e.exceptions.CouldNotCreateLogicalTimeFactory;
import hla.rti1516e.exceptions.CouldNotOpenFDD;
import hla.rti1516e.exceptions.DeletePrivilegeNotHeld;
import hla.rti1516e.exceptions.ErrorReadingFDD;
import hla.rti1516e.exceptions.FederateAlreadyExecutionMember;
import hla.rti1516e.exceptions.FederateNameAlreadyInUse;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateOwnsAttributes;
import hla.rti1516e.exceptions.FederateServiceInvocationsAreBeingReportedViaMOM;
import hla.rti1516e.exceptions.FederationExecutionDoesNotExist;
import hla.rti1516e.exceptions.InconsistentFDD;
import hla.rti1516e.exceptions.InteractionClassNotDefined;
import hla.rti1516e.exceptions.InteractionClassNotPublished;
import hla.rti1516e.exceptions.InteractionParameterNotDefined;
import hla.rti1516e.exceptions.InvalidLocalSettingsDesignator;
import hla.rti1516e.exceptions.InvalidLogicalTime;
import hla.rti1516e.exceptions.NoAcquisitionPending;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.ObjectClassNotPublished;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import hla.rti1516e.exceptions.OwnershipAcquisitionPending;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.exceptions.UnsupportedCallbackModel;
import java.net.URL;
import java.util.Set;
import nl.tno.oorti.exceptions.InteractionEncodingError;
import nl.tno.oorti.exceptions.ObjectEncodingError;

/**
 * The OORTIambassador defines the interface that the OORTI ambassador must implement for receiving
 * calls from a federate. This interface extends to RTIambassador interface with additional methods
 * to handle Java Beans.
 *
 * @author bergtwvd
 */
public interface OORTIambassador extends RTIambassador {

  ////////////////////////////////////
  // Federation Management Services //
  ////////////////////////////////////
  void connect(
      OOFederateAmbassador federateReference,
      CallbackModel callbackModel,
      String localSettingsDesignator)
      throws ConnectionFailed,
          InvalidLocalSettingsDesignator,
          UnsupportedCallbackModel,
          AlreadyConnected,
          CallNotAllowedFromWithinCallback,
          RTIinternalError;

  void connect(OOFederateAmbassador federateReference, CallbackModel callbackModel)
      throws ConnectionFailed,
          InvalidLocalSettingsDesignator,
          UnsupportedCallbackModel,
          AlreadyConnected,
          CallNotAllowedFromWithinCallback,
          RTIinternalError;

  FederateHandle joinFederationExecutionWithCurrentFDD(
      String federateName,
      String federateType,
      String federationExecutionName,
      URL[] additionalFomModules,
      URL[] currentFddModules)
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
          FederateNameAlreadyInUse,
          RTIinternalError;

  FederateHandle joinFederationExecutionWithCurrentFDD(
      String federateType,
      String federationExecutionName,
      URL[] additionalFomModules,
      URL[] currentFddModules)
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
          FederateNameAlreadyInUse,
          RTIinternalError;

  FederateHandle joinFederationExecutionWithCurrentFDD(
      String federateName,
      String federateType,
      String federationExecutionName,
      URL[] currentFddModules)
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
          FederateNameAlreadyInUse,
          RTIinternalError;

  FederateHandle joinFederationExecutionWithCurrentFDD(
      String federateType, String federationExecutionName, URL[] currentFddModules)
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
          FederateNameAlreadyInUse,
          RTIinternalError;

  /////////////////////////////////////
  // Declaration Management Services //
  /////////////////////////////////////

  void publishObjectClass(Class clazz)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void publishObjectClass(Class clazz, Set<OOattribute> attributes)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void subscribeObjectClass(Class clazz)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void subscribeObjectClass(Class clazz, Set<OOattribute> attributes)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void unpublishObjectClass(Class clazz)
      throws OwnershipAcquisitionPending,
          AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void unsubscribeObjectClass(Class clazz)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void publishInteractionClass(Class clazz)
      throws InteractionClassNotDefined,
          InteractionParameterNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void publishInteractionClass(Class clazz, Set<OOparameter> parameters)
      throws InteractionClassNotDefined,
          InteractionParameterNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void subscribeInteractionClass(Class clazz)
      throws FederateServiceInvocationsAreBeingReportedViaMOM,
          InteractionClassNotDefined,
          InteractionParameterNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void subscribeInteractionClass(Class clazz, Set<OOparameter> parameters)
      throws FederateServiceInvocationsAreBeingReportedViaMOM,
          InteractionClassNotDefined,
          InteractionParameterNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void unpublishInteractionClass(Class clazz)
      throws InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void unsubscribeInteractionClass(Class clazz)
      throws InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  ////////////////////////////////
  // Object Management Services //
  ////////////////////////////////

  void registerObjectInstance(Object theObject)
      throws ObjectClassNotPublished,
          ObjectClassNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void updateAttributeValues(Object theObject, byte[] userSuppliedTag)
      throws AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          ObjectEncodingError,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void updateAttributeValues(String theObjectName, Object theObject, byte[] userSuppliedTag)
      throws AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          ObjectEncodingError,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void updateAttributeValues(
      Object theObject, Set<OOattribute> theAttributes, byte[] userSuppliedTag)
      throws AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          ObjectEncodingError,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  MessageRetractionReturn updateAttributeValues(
      Object theObject, byte[] userSuppliedTag, LogicalTime theTime)
      throws InvalidLogicalTime,
          AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          ObjectEncodingError,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  MessageRetractionReturn updateAttributeValues(
      String theObjectName, Object theObject, byte[] userSuppliedTag, LogicalTime theTime)
      throws InvalidLogicalTime,
          AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          ObjectEncodingError,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  MessageRetractionReturn updateAttributeValues(
      Object theObject, Set<OOattribute> theAttributes, byte[] userSuppliedTag, LogicalTime theTime)
      throws InvalidLogicalTime,
          AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          ObjectEncodingError,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void deleteObjectInstance(Object theObject, byte[] userSuppliedTag)
      throws DeletePrivilegeNotHeld,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void deleteObjectInstance(String theObjectName, byte[] userSuppliedTag)
      throws DeletePrivilegeNotHeld,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  MessageRetractionReturn deleteObjectInstance(
      Object theObject, byte[] userSuppliedTag, LogicalTime theTime)
      throws InvalidLogicalTime,
          DeletePrivilegeNotHeld,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  MessageRetractionReturn deleteObjectInstance(
      String theObjectName, byte[] userSuppliedTag, LogicalTime theTime)
      throws InvalidLogicalTime,
          DeletePrivilegeNotHeld,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void localDeleteObjectInstance(Object theObject)
      throws OwnershipAcquisitionPending,
          FederateOwnsAttributes,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void requestAttributeValueUpdate(Class clazz, byte[] userSuppliedTag)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void requestAttributeValueUpdate(
      Class clazz, Set<OOattribute> theAttributes, byte[] userSuppliedTag)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void requestAttributeValueUpdate(Object theObject, byte[] userSuppliedTag)
      throws AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void requestAttributeValueUpdate(
      Object theObject, Set<OOattribute> theAttributes, byte[] userSuppliedTag)
      throws AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void sendInteraction(Object theInteraction, byte[] userSuppliedTag)
      throws InteractionClassNotPublished,
          InteractionParameterNotDefined,
          InteractionClassNotDefined,
          InteractionEncodingError,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void sendInteraction(
      Object theInteraction, Set<OOparameter> theParameters, byte[] userSuppliedTag)
      throws InteractionClassNotPublished,
          InteractionParameterNotDefined,
          InteractionClassNotDefined,
          InteractionEncodingError,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  MessageRetractionReturn sendInteraction(
      Object theInteraction, byte[] userSuppliedTag, LogicalTime theTime)
      throws InvalidLogicalTime,
          InteractionClassNotPublished,
          InteractionParameterNotDefined,
          InteractionClassNotDefined,
          InteractionEncodingError,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  MessageRetractionReturn sendInteraction(
      Object theInteraction,
      Set<OOparameter> theParameters,
      byte[] userSuppliedTag,
      LogicalTime theTime)
      throws InvalidLogicalTime,
          InteractionClassNotPublished,
          InteractionParameterNotDefined,
          InteractionClassNotDefined,
          InteractionEncodingError,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  ///////////////////////////////////
  // Ownership Management Services //
  ///////////////////////////////////

  void unconditionalAttributeOwnershipDivestiture(Object theObject, Set<OOattribute> theAttributes)
      throws AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void negotiatedAttributeOwnershipDivestiture(
      Object theObject, Set<OOattribute> theAttributes, byte[] userSuppliedTag)
      throws AttributeAlreadyBeingDivested,
          AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void confirmDivestiture(Object theObject, Set<OOattribute> theAttributes, byte[] userSuppliedTag)
      throws NoAcquisitionPending,
          AttributeDivestitureWasNotRequested,
          AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void attributeOwnershipAcquisition(
      Object theObject, Set<OOattribute> desiredAttributes, byte[] userSuppliedTag)
      throws AttributeNotPublished,
          ObjectClassNotPublished,
          FederateOwnsAttributes,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void attributeOwnershipAcquisitionIfAvailable(
      Object theObject, Set<OOattribute> desiredAttributes)
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
          RTIinternalError;

  void attributeOwnershipReleaseDenied(Object theObject, Set<OOattribute> theAttributes)
      throws AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  Set<OOattribute> attributeOwnershipDivestitureIfWanted(
      Object theObject, Set<OOattribute> theAttributes)
      throws AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void cancelNegotiatedAttributeOwnershipDivestiture(
      Object theObject, Set<OOattribute> theAttributes)
      throws AttributeDivestitureWasNotRequested,
          AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void cancelAttributeOwnershipAcquisition(Object theObject, Set<OOattribute> theAttributes)
      throws AttributeAcquisitionWasNotRequested,
          AttributeAlreadyOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  void queryAttributeOwnership(Object theObject, OOattribute theAttribute)
      throws AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  boolean isAttributeOwnedByFederate(Object theObject, OOattribute theAttribute)
      throws AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  //////////////////////////
  // RTI Support Services //
  //////////////////////////

  String getObjectClassName(Class theClass)
      throws ObjectClassNotDefined, FederateNotExecutionMember, NotConnected, RTIinternalError;

  Class getObjectClass(String theClassName)
      throws ObjectClassNotDefined, FederateNotExecutionMember, NotConnected, RTIinternalError;

  String getInteractionClassName(Class theClass)
      throws InteractionClassNotDefined, FederateNotExecutionMember, NotConnected, RTIinternalError;

  Class getInteractionClass(String theClassName)
      throws InteractionClassNotDefined, FederateNotExecutionMember, NotConnected, RTIinternalError;

  String getObjectName(Object theObject)
      throws ObjectInstanceNotKnown, FederateNotExecutionMember, NotConnected, RTIinternalError;

  Object getObject(String theObjectName)
      throws ObjectInstanceNotKnown, FederateNotExecutionMember, NotConnected, RTIinternalError;

  OOattribute getAttribute(Class clazz, String attributeName)
      throws ObjectClassNotDefined,
          AttributeNotDefined,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  Set<OOattribute> getAttributes(Class clazz, String... attributeName)
      throws ObjectClassNotDefined,
          AttributeNotDefined,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  Set<OOattribute> getAttributes(Class clazz)
      throws ObjectClassNotDefined,
          AttributeNotDefined,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  OOparameter getParameter(Class clazz, String parameterName)
      throws InteractionClassNotDefined,
          InteractionParameterNotDefined,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  Set<OOparameter> getParameters(Class clazz, String... parameterName)
      throws InteractionClassNotDefined,
          InteractionParameterNotDefined,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  Set<OOparameter> getParameters(Class clazz)
      throws InteractionClassNotDefined,
          InteractionParameterNotDefined,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;
}
