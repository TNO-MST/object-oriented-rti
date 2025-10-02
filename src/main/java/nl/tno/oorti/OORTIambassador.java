package nl.tno.oorti;

import hla.rti1516e.CallbackModel;
import hla.rti1516e.FederateHandle;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.MessageRetractionReturn;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.exceptions.AlreadyConnected;
import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.AttributeNotOwned;
import hla.rti1516e.exceptions.CallNotAllowedFromWithinCallback;
import hla.rti1516e.exceptions.ConnectionFailed;
import hla.rti1516e.exceptions.CouldNotCreateLogicalTimeFactory;
import hla.rti1516e.exceptions.CouldNotOpenFDD;
import hla.rti1516e.exceptions.DeletePrivilegeNotHeld;
import hla.rti1516e.exceptions.ErrorReadingFDD;
import hla.rti1516e.exceptions.FederateAlreadyExecutionMember;
import hla.rti1516e.exceptions.FederateNameAlreadyInUse;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateServiceInvocationsAreBeingReportedViaMOM;
import hla.rti1516e.exceptions.FederationExecutionDoesNotExist;
import hla.rti1516e.exceptions.InconsistentFDD;
import hla.rti1516e.exceptions.InteractionClassNotDefined;
import hla.rti1516e.exceptions.InteractionClassNotPublished;
import hla.rti1516e.exceptions.InteractionParameterNotDefined;
import hla.rti1516e.exceptions.InvalidLocalSettingsDesignator;
import hla.rti1516e.exceptions.InvalidLogicalTime;
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

/**
 * The OORTIambassador defines the interface that the OORTI ambassador must implement for receiving
 * calls from a federate. This interface extends to RTIambassador interface with additional methods
 * to handle Java Bean classes.
 *
 * @author bergtwvd
 */
public interface OORTIambassador extends RTIambassador {

  ////////////////////////////////////
  // Federation Management Services //
  ////////////////////////////////////
  public void connect(
      OOFederateAmbassador federateReference,
      CallbackModel callbackModel,
      String localSettingsDesignator)
      throws ConnectionFailed,
          InvalidLocalSettingsDesignator,
          UnsupportedCallbackModel,
          AlreadyConnected,
          CallNotAllowedFromWithinCallback,
          RTIinternalError;

  public void connect(OOFederateAmbassador federateReference, CallbackModel callbackModel)
      throws ConnectionFailed,
          InvalidLocalSettingsDesignator,
          UnsupportedCallbackModel,
          AlreadyConnected,
          CallNotAllowedFromWithinCallback,
          RTIinternalError;

  public FederateHandle joinFederationExecution(
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

  /////////////////////////////////////
  // Declaration Management Services //
  /////////////////////////////////////
  // Object classes
  public Set<OOattribute> publishObjectClass(Class clazz)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public Set<OOattribute> publishObjectClass(Class clazz, Set<? extends Object> cookies)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public Set<OOattribute> subscribeObjectClass(Class clazz)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public Set<OOattribute> subscribeObjectClass(Class clazz, Set<? extends Object> cookies)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public void unpublishObjectClass(Class clazz)
      throws OwnershipAcquisitionPending,
          AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public void unsubscribeObjectClass(Class clazz)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  // Interaction classes
  public Set<OOparameter> publishInteractionClass(Class clazz)
      throws InteractionClassNotDefined,
          InteractionParameterNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public Set<OOparameter> publishInteractionClass(Class clazz, Set<? extends Object> cookies)
      throws InteractionClassNotDefined,
          InteractionParameterNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public Set<OOparameter> subscribeInteractionClass(Class clazz)
      throws FederateServiceInvocationsAreBeingReportedViaMOM,
          InteractionClassNotDefined,
          InteractionParameterNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public Set<OOparameter> subscribeInteractionClass(Class clazz, Set<? extends Object> cookies)
      throws FederateServiceInvocationsAreBeingReportedViaMOM,
          InteractionClassNotDefined,
          InteractionParameterNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public void unpublishInteractionClass(Class clazz)
      throws InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public void unsubscribeInteractionClass(Class clazz)
      throws InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  ////////////////////////////////
  // Object Management Services //
  ////////////////////////////////
  // Objects
  public void registerObjectInstance(Object theObject)
      throws ObjectClassNotPublished,
          ObjectClassNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public void updateAttributeValues(Object theObject, byte[] userSuppliedTag)
      throws AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public void updateAttributeValues(String theObjectName, Object theObject, byte[] userSuppliedTag)
      throws AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public void updateAttributeValues(
      Object theObject, Set<OOattribute> theAttributes, byte[] userSuppliedTag)
      throws AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public MessageRetractionReturn updateAttributeValues(
      Object theObject, byte[] userSuppliedTag, LogicalTime theTime)
      throws InvalidLogicalTime,
          AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public MessageRetractionReturn updateAttributeValues(
      String theObjectName, Object theObject, byte[] userSuppliedTag, LogicalTime theTime)
      throws InvalidLogicalTime,
          AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public MessageRetractionReturn updateAttributeValues(
      Object theObject, Set<OOattribute> theAttributes, byte[] userSuppliedTag, LogicalTime theTime)
      throws InvalidLogicalTime,
          AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public void deleteObjectInstance(Object theObject, byte[] userSuppliedTag)
      throws DeletePrivilegeNotHeld,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public void deleteObjectInstance(String theObjectName, byte[] userSuppliedTag)
      throws DeletePrivilegeNotHeld,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public MessageRetractionReturn deleteObjectInstance(
      Object theObject, byte[] userSuppliedTag, LogicalTime theTime)
      throws InvalidLogicalTime,
          DeletePrivilegeNotHeld,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public MessageRetractionReturn deleteObjectInstance(
      String theObjectName, byte[] userSuppliedTag, LogicalTime theTime)
      throws InvalidLogicalTime,
          DeletePrivilegeNotHeld,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public void requestAttributeValueUpdate(Class clazz, byte[] userSuppliedTag)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public void requestAttributeValueUpdate(
      Class clazz, Set<OOattribute> theAttributes, byte[] userSuppliedTag)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public void requestAttributeValueUpdate(Object theObject, byte[] userSuppliedTag)
      throws AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public void requestAttributeValueUpdate(
      Object theObject, Set<OOattribute> theAttributes, byte[] userSuppliedTag)
      throws AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  // Interactions
  public void sendInteraction(Object theInteraction, byte[] userSuppliedTag)
      throws InteractionClassNotPublished,
          InteractionParameterNotDefined,
          InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public void sendInteraction(
      Object theInteraction, Set<OOparameter> theParameters, byte[] userSuppliedTag)
      throws InteractionClassNotPublished,
          InteractionParameterNotDefined,
          InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public MessageRetractionReturn sendInteraction(
      Object theInteraction, byte[] userSuppliedTag, LogicalTime theTime)
      throws InvalidLogicalTime,
          InteractionClassNotPublished,
          InteractionParameterNotDefined,
          InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError;

  public MessageRetractionReturn sendInteraction(
      Object theInteraction,
      Set<OOparameter> theParameters,
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
          RTIinternalError;

  //////////////////////////
  // RTI Support Services //
  //////////////////////////
  public String getObjectName(Object theObject) throws ObjectInstanceNotKnown;

  public Object getObject(String theObjectName) throws ObjectInstanceNotKnown;
}
