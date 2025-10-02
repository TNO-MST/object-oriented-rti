package nl.tno.oorti.impl;

import hla.rti1516e.CallbackModel;
import hla.rti1516e.FederateHandle;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.MessageRetractionReturn;
import hla.rti1516e.ObjectInstanceHandle;
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
import hla.rti1516e.exceptions.FederateOwnsAttributes;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URL;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.oorti.OOFederateAmbassador;
import nl.tno.oorti.OORTIambassador;
import nl.tno.oorti.OOattribute;
import nl.tno.oorti.OOobjectFactory;
import nl.tno.oorti.OOparameter;
import nl.tno.oorti.OOproperties;
import nl.tno.oorti.impl.mim.objects.HLAfederation;
import nl.tno.oorti.impl.serializer.InteractionClass;
import nl.tno.oorti.impl.serializer.ObjectClass;
import nl.tno.oorti.impl.serializer.ObjectInstance;
import nl.tno.oorti.impl.serializer.SerializedInteractionData;
import nl.tno.oorti.impl.serializer.SerializedObjectData;
import nl.tno.oorti.impl.serializer.Serializer;

/**
 * @author bergtwvd
 */
public class OORTIambassadorImpl extends NullRTIambassador implements OORTIambassador {

  // Construction arguments
  final OOobjectFactory objectFactory;
  final OOproperties properties;

  // the FederateAmbassador for handling the OO-specific callbacks
  OOFederateAmbassadorImpl fedamb;

  // the callback model used
  CallbackModel callbackModel;

  // the Serializers
  Serializer serializer;
  Serializer momSerializer;

  // MOM related information, shared between the ambassador threads
  ObjectInstanceHandle hlaFederationInstanceHandle;
  HLAfederation hlaFederation;

  /**
   * Constructs an Object Oriented RTI ambassador.
   *
   * @param rtiamb: the RTI Ambassador to use
   * @param objectFactory: use this factory for creating HLA object class instances and HLA
   *     interactions
   * @param properties: properties for constructing the RTI ambassador
   */
  public OORTIambassadorImpl(
      RTIambassador rtiamb, OOobjectFactory objectFactory, OOproperties properties) {
    super(rtiamb);
    this.objectFactory = objectFactory;
    this.properties = properties;
  }

  ////////////////////////////////////
  // Federation Management Services //
  ////////////////////////////////////
  @Override
  public void connect(
      OOFederateAmbassador federateReference,
      CallbackModel callbackModel,
      String localSettingsDesignator)
      throws ConnectionFailed,
          InvalidLocalSettingsDesignator,
          UnsupportedCallbackModel,
          AlreadyConnected,
          CallNotAllowedFromWithinCallback,
          RTIinternalError {
    this.fedamb = new OOFederateAmbassadorImpl(this, federateReference);

    if (localSettingsDesignator == null) {
      this.rtiamb.connect(this.fedamb, callbackModel);
    } else {
      this.rtiamb.connect(this.fedamb, callbackModel, localSettingsDesignator);
    }

    this.callbackModel = callbackModel;
  }

  @Override
  public void connect(OOFederateAmbassador federateReference, CallbackModel callbackModel)
      throws ConnectionFailed,
          InvalidLocalSettingsDesignator,
          UnsupportedCallbackModel,
          AlreadyConnected,
          CallNotAllowedFromWithinCallback,
          RTIinternalError {
    connect(federateReference, callbackModel, null);
  }

  @Override
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
          RTIinternalError {

    FederateHandle handle =
        (federateName == null)
            ? (additionalFomModules == null)
                ? this.rtiamb.joinFederationExecution(federateType, federationExecutionName)
                : this.rtiamb.joinFederationExecution(
                    federateType, federationExecutionName, additionalFomModules)
            : (additionalFomModules == null)
                ? this.rtiamb.joinFederationExecution(
                    federateName, federateType, federationExecutionName)
                : this.rtiamb.joinFederationExecution(
                    federateName, federateType, federationExecutionName, additionalFomModules);

    ObjectModelType[] modules =
        properties.isUseRtiForCurrentFdd()
            ? this.getCurrectFDD()
            : this.getCurrentFDD(additionalFomModules, currentFddModules);

    try {
      this.serializer = new Serializer(this.rtiamb, modules, this.objectFactory, this.properties);
    } catch (FederateNotExecutionMember ex) {
      throw new RTIinternalError(ex.getMessage(), ex);
    }

    return handle;
  }

  private ObjectModelType[] getCurrentFDD(URL[] additionalFomModules, URL[] currentFddModules)
      throws RTIinternalError {
    try {
      ObjectModelType mim =
          OmtFunctions.readOmt(this.getClass().getResource("/foms/HLAstandardMIM.xml"));

      if (additionalFomModules != null) {
        if (currentFddModules != null) {
          ObjectModelType[] modules =
              new ObjectModelType[additionalFomModules.length + currentFddModules.length + 1];

          for (int i = 0; i < additionalFomModules.length; i++) {
            modules[i] = OmtFunctions.readOmt(additionalFomModules[i]);
          }

          for (int i = 0; i < currentFddModules.length; i++) {
            modules[additionalFomModules.length + i] = OmtFunctions.readOmt(currentFddModules[i]);
          }

          modules[additionalFomModules.length + currentFddModules.length] = mim;

          return modules;
        } else {
          ObjectModelType[] modules = new ObjectModelType[additionalFomModules.length + 1];

          for (int i = 0; i < additionalFomModules.length; i++) {
            modules[i] = OmtFunctions.readOmt(additionalFomModules[i]);
          }

          modules[additionalFomModules.length] = mim;

          return modules;
        }
      } else {
        if (currentFddModules != null) {
          ObjectModelType[] modules = new ObjectModelType[currentFddModules.length + 1];

          for (int i = 0; i < currentFddModules.length; i++) {
            modules[i] = OmtFunctions.readOmt(currentFddModules[i]);
          }

          modules[currentFddModules.length] = mim;

          return modules;
        } else {
          return new ObjectModelType[] {mim};
        }
      }
    } catch (IOException ex) {
      throw new RTIinternalError(ex.getMessage(), ex);
    }
  }

  private ObjectModelType[] getCurrectFDD() throws RTIinternalError {
    try {
      Logger.getLogger(this.getClass().getName()).log(Level.FINE, "Request CurrectFDD");

      ObjectModelType mim =
          OmtFunctions.readOmt(this.getClass().getResource("/foms/HLAstandardMIM.xml"));
      this.momSerializer =
          new Serializer(
              this.rtiamb, new ObjectModelType[] {mim}, this.objectFactory, this.properties);

      // subscribe to MOM HLAmanager.HLAfederation to get the HLAcurrentFDD
      ObjectClass objectClass =
          this.momSerializer.addObjectClass(
              false,
              this.momSerializer.createObjectClass(HLAfederation.class, Set.of("HLAcurrentFDD")));

      synchronized (this) {
        rtiamb.subscribeObjectClassAttributes(
            objectClass.getClassHandle(),
            objectClass.createAttributeHandleSet(objectClass.getAttributeSet()));

        for (int i = 0; i < 1000; i++) {
          if (this.callbackModel == CallbackModel.HLA_EVOKED) {
            // Although the IEEE specification states that evokeCallback
            // can be done regardless of the callback mode, the Pitch RTI
            // (tested for 5.5.9-12) hangs when we make this call in
            // Immediate mode.
            // Possibly because the callback thread is already waiting
            // and blocked. Hence we only make this call if and only if
            // in Evoked mode.
            rtiamb.evokeCallback(0);
          }

          // wait at most 10 ms for a notification from the FederateAmbassador
          this.wait(10);

          if (this.hlaFederation != null && this.hlaFederation.getHLAcurrentFDD() != null) {
            break;
          }
        }

        // unsubscribe as we are not interested in further updates
        rtiamb.unsubscribeObjectClass(objectClass.getClassHandle());

        // if we got an instance, delete it
        if (this.hlaFederationInstanceHandle != null) {
          // We need to locally delete the object instance, so that
          // subscribers on the MIM can re-discover the instance.
          // Also, the local delete must be done after we unsubscribe.
          rtiamb.localDeleteObjectInstance(this.hlaFederationInstanceHandle);
          this.hlaFederationInstanceHandle = null;
        }
      }

      if (hlaFederation == null || hlaFederation.getHLAcurrentFDD() == null) {
        throw new RTIinternalError("Did not receive CurrentFDD");
      }

      Logger.getLogger(this.getClass().getName()).log(Level.FINE, "Received CurrentFDD");

      if (Logger.getLogger(this.getClass().getName()).isLoggable(Level.FINE)) {
        try (PrintWriter out = new PrintWriter("currentFDD.xml")) {
          out.println(this.hlaFederation.getHLAcurrentFDD());
          out.flush();
        }
      }

      return new ObjectModelType[] {
        OmtFunctions.readOmt(new StringReader(this.hlaFederation.getHLAcurrentFDD()))
      };
    } catch (IOException
        | ObjectClassNotDefined
        | SaveInProgress
        | RestoreInProgress
        | FederateNotExecutionMember
        | NotConnected
        | AttributeNotDefined
        | CallNotAllowedFromWithinCallback
        | InterruptedException
        | OwnershipAcquisitionPending
        | FederateOwnsAttributes
        | ObjectInstanceNotKnown ex) {
      throw new RTIinternalError(ex.getMessage(), ex);
    }
  }

  @Override
  public FederateHandle joinFederationExecution(
      String federateName,
      String federateType,
      String federationExecutionName,
      URL[] additionalFomModules)
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
          RTIinternalError,
          FederateNameAlreadyInUse {
    return joinFederationExecution(
        federateName, federateType, federationExecutionName, additionalFomModules, null);
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
    try {
      return joinFederationExecution(
          null, federateType, federationExecutionName, additionalFomModules, null);
    } catch (FederateNameAlreadyInUse ex) {
      // should not get here, since we pass nulls
      throw new RTIinternalError(ex.getMessage(), ex);
    }
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
    try {
      return joinFederationExecution(
          federateName, federateType, federationExecutionName, null, null);
    } catch (InconsistentFDD | ErrorReadingFDD | CouldNotOpenFDD ex) {
      // should not get here, since we pass nulls
      throw new RTIinternalError(ex.getMessage(), ex);
    }
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
    try {
      return joinFederationExecution(null, federateType, federationExecutionName, null, null);
    } catch (InconsistentFDD | ErrorReadingFDD | CouldNotOpenFDD | FederateNameAlreadyInUse ex) {
      // should not get here, since we pass nulls
      throw new RTIinternalError(ex.getMessage(), ex);
    }
  }

  /////////////////////////////////////
  // Declaration Management Services //
  /////////////////////////////////////
  ////////////////////
  // Object classes //
  ////////////////////
  @Override
  public Set<OOattribute> publishObjectClass(Class clazz)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    if (serializer.getObjectClass(true, clazz) != null) {
      throw new RTIinternalError("Class " + clazz.getSimpleName() + " already created");
    }

    ObjectClass oc = serializer.createObjectClass(clazz);
    rtiamb.publishObjectClassAttributes(
        oc.getClassHandle(), oc.createAttributeHandleSet(oc.getAttributeSet()));
    return serializer.addObjectClass(true, oc).getAttributeSet();
  }

  @Override
  public Set<OOattribute> publishObjectClass(Class clazz, Set<? extends Object> theAttributes)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    if (serializer.getObjectClass(true, clazz) != null) {
      throw new RTIinternalError("Class " + clazz.getSimpleName() + " already created");
    }

    ObjectClass oc = serializer.createObjectClass(clazz, theAttributes);
    rtiamb.publishObjectClassAttributes(
        oc.getClassHandle(), oc.createAttributeHandleSet(oc.getAttributeSet()));
    return serializer.addObjectClass(true, oc).getAttributeSet();
  }

  @Override
  public void unpublishObjectClass(Class clazz)
      throws OwnershipAcquisitionPending,
          AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    ObjectClass oc = serializer.getObjectClassIfExists(true, clazz);
    rtiamb.unpublishObjectClassAttributes(
        oc.getClassHandle(), oc.createAttributeHandleSet(oc.getAttributeSet()));
    serializer.removeObjectClass(true, oc);
  }

  @Override
  public Set<OOattribute> subscribeObjectClass(Class clazz)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    if (serializer.getObjectClass(false, clazz) != null) {
      throw new RTIinternalError("Class " + clazz.getSimpleName() + " already created");
    }

    ObjectClass oc = serializer.createObjectClass(clazz);
    rtiamb.subscribeObjectClassAttributes(
        oc.getClassHandle(), oc.createAttributeHandleSet(oc.getAttributeSet()));
    return serializer.addObjectClass(false, oc).getAttributeSet();
  }

  @Override
  public Set<OOattribute> subscribeObjectClass(Class clazz, Set<? extends Object> theAttributes)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    if (serializer.getObjectClass(false, clazz) != null) {
      throw new RTIinternalError("Class " + clazz.getSimpleName() + " already created");
    }

    ObjectClass oc = serializer.createObjectClass(clazz, theAttributes);
    rtiamb.subscribeObjectClassAttributes(
        oc.getClassHandle(), oc.createAttributeHandleSet(oc.getAttributeSet()));
    return serializer.addObjectClass(false, oc).getAttributeSet();
  }

  @Override
  public void unsubscribeObjectClass(Class clazz)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    ObjectClass oc = serializer.getObjectClassIfExists(false, clazz);
    rtiamb.unsubscribeObjectClass(oc.getClassHandle());
    serializer.removeObjectClass(false, oc);

    // also delete all remote object instances of this class
    for (ObjectInstance oi : serializer.getObjectInstances()) {
      if (oi.getObjectClass().equals(oc)) {
        try {
          rtiamb.localDeleteObjectInstance(oi.getInstanceHandle());
          serializer.removeObjectInstance(oi);
        } catch (OwnershipAcquisitionPending | FederateOwnsAttributes | ObjectInstanceNotKnown ex) {
          // ignore these exceptions and continue with the next object instance
        }
      }
    }
  }

  /////////////////////////
  // Interaction classes //
  /////////////////////////
  @Override
  public Set<OOparameter> publishInteractionClass(Class clazz)
      throws InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError,
          InteractionParameterNotDefined {

    if (serializer.getInteractionClass(true, clazz) != null) {
      throw new RTIinternalError("Class " + clazz.getSimpleName() + " already created");
    }

    InteractionClass interactionClass = serializer.createInteractionClass(clazz);
    rtiamb.publishInteractionClass(interactionClass.getClassHandle());
    return serializer.addInteractionClass(true, interactionClass).getParameterSet();
  }

  @Override
  public Set<OOparameter> publishInteractionClass(Class clazz, Set<? extends Object> theParameters)
      throws FederateNotExecutionMember,
          NotConnected,
          RTIinternalError,
          InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          InteractionParameterNotDefined {

    if (serializer.getInteractionClass(true, clazz) != null) {
      throw new RTIinternalError("Class " + clazz.getSimpleName() + " already created");
    }

    InteractionClass interactionClass = serializer.createInteractionClass(clazz, theParameters);
    rtiamb.publishInteractionClass(interactionClass.getClassHandle());
    return serializer.addInteractionClass(true, interactionClass).getParameterSet();
  }

  @Override
  public void unpublishInteractionClass(Class clazz)
      throws InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    InteractionClass interactionClass = serializer.getInteractionClassIfExists(true, clazz);
    rtiamb.unpublishInteractionClass(interactionClass.getClassHandle());
    serializer.removeInteractionClass(true, interactionClass);
  }

  @Override
  public Set<OOparameter> subscribeInteractionClass(Class clazz)
      throws FederateNotExecutionMember,
          NotConnected,
          RTIinternalError,
          InteractionParameterNotDefined,
          InteractionClassNotDefined,
          FederateServiceInvocationsAreBeingReportedViaMOM,
          SaveInProgress,
          RestoreInProgress {

    if (serializer.getInteractionClass(false, clazz) != null) {
      throw new RTIinternalError("Class " + clazz.getSimpleName() + " already created");
    }

    InteractionClass interactionClass = serializer.createInteractionClass(clazz);
    rtiamb.subscribeInteractionClass(interactionClass.getClassHandle());
    return serializer.addInteractionClass(false, interactionClass).getParameterSet();
  }

  @Override
  public Set<OOparameter> subscribeInteractionClass(
      Class clazz, Set<? extends Object> theParameters)
      throws FederateNotExecutionMember,
          NotConnected,
          RTIinternalError,
          InteractionParameterNotDefined,
          InteractionClassNotDefined,
          FederateServiceInvocationsAreBeingReportedViaMOM,
          SaveInProgress,
          RestoreInProgress {

    if (serializer.getInteractionClass(false, clazz) != null) {
      throw new RTIinternalError("Class " + clazz.getSimpleName() + " already created");
    }

    InteractionClass interactionClass = serializer.createInteractionClass(clazz, theParameters);
    rtiamb.subscribeInteractionClass(interactionClass.getClassHandle());
    return serializer.addInteractionClass(false, interactionClass).getParameterSet();
  }

  @Override
  public void unsubscribeInteractionClass(Class clazz)
      throws InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    InteractionClass interactionClass = serializer.getInteractionClassIfExists(false, clazz);
    rtiamb.unsubscribeInteractionClass(interactionClass.getClassHandle());
    serializer.removeInteractionClass(false, interactionClass);
  }

  ////////////////////////////////
  // Object Management Services //
  ////////////////////////////////
  @Override
  public void registerObjectInstance(Object theObject)
      throws ObjectClassNotPublished,
          ObjectClassNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    ObjectClass oc =
        serializer.getObjectClassIfExists(true, this.objectFactory.getObjectClass(theObject));
    ObjectInstanceHandle instanceHandle = rtiamb.registerObjectInstance(oc.getClassHandle());
    serializer.createObjectInstance(
        oc, instanceHandle, theObject, rtiamb.getObjectInstanceName(instanceHandle));
  }

  @Override
  public void updateAttributeValues(String theObjectName, Object theObject, byte[] userSuppliedTag)
      throws AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    SerializedObjectData objectData =
        serializer.serializeObject(theObjectName, theObject, new SerializedObjectData());
    rtiamb.updateAttributeValues(
        objectData.getInstanceHandle(), objectData.getAttributeValueMap(), userSuppliedTag);
  }

  @Override
  public void updateAttributeValues(Object theObject, byte[] userSuppliedTag)
      throws AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    SerializedObjectData objectData =
        serializer.serializeObject(theObject, new SerializedObjectData());
    rtiamb.updateAttributeValues(
        objectData.getInstanceHandle(), objectData.getAttributeValueMap(), userSuppliedTag);
  }

  @Override
  public void updateAttributeValues(
      Object theObject, Set<OOattribute> theAttributes, byte[] userSuppliedTag)
      throws AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    SerializedObjectData objectData =
        serializer.serializeObject(theObject, theAttributes, new SerializedObjectData());
    rtiamb.updateAttributeValues(
        objectData.getInstanceHandle(), objectData.getAttributeValueMap(), userSuppliedTag);
  }

  @Override
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
          RTIinternalError {
    SerializedObjectData objectData =
        serializer.serializeObject(theObject, new SerializedObjectData());
    return rtiamb.updateAttributeValues(
        objectData.getInstanceHandle(),
        objectData.getAttributeValueMap(),
        userSuppliedTag,
        theTime);
  }

  @Override
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
          RTIinternalError {
    SerializedObjectData objectData =
        serializer.serializeObject(theObjectName, theObject, new SerializedObjectData());
    return rtiamb.updateAttributeValues(
        objectData.getInstanceHandle(),
        objectData.getAttributeValueMap(),
        userSuppliedTag,
        theTime);
  }

  @Override
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
          RTIinternalError {
    SerializedObjectData objectData =
        serializer.serializeObject(theObject, theAttributes, new SerializedObjectData());
    return rtiamb.updateAttributeValues(
        objectData.getInstanceHandle(),
        objectData.getAttributeValueMap(),
        userSuppliedTag,
        theTime);
  }

  @Override
  public void deleteObjectInstance(Object theObject, byte[] userSuppliedTag)
      throws DeletePrivilegeNotHeld,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    ObjectInstance oi = serializer.getObjectInstanceIfExists(theObject);
    rtiamb.deleteObjectInstance(oi.getInstanceHandle(), userSuppliedTag);
    serializer.removeObjectInstance(oi);
  }

  @Override
  public void deleteObjectInstance(String theObjectName, byte[] userSuppliedTag)
      throws DeletePrivilegeNotHeld,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    ObjectInstance oi = serializer.getObjectInstanceIfExists(theObjectName);
    rtiamb.deleteObjectInstance(oi.getInstanceHandle(), userSuppliedTag);
    serializer.removeObjectInstance(oi);
  }

  @Override
  public MessageRetractionReturn deleteObjectInstance(
      Object theObject, byte[] userSuppliedTag, LogicalTime theTime)
      throws InvalidLogicalTime,
          DeletePrivilegeNotHeld,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    ObjectInstance oi = serializer.getObjectInstanceIfExists(theObject);
    MessageRetractionReturn retraction =
        rtiamb.deleteObjectInstance(oi.getInstanceHandle(), userSuppliedTag, theTime);
    serializer.removeObjectInstance(oi);
    return retraction;
  }

  @Override
  public MessageRetractionReturn deleteObjectInstance(
      String theObjectName, byte[] userSuppliedTag, LogicalTime theTime)
      throws InvalidLogicalTime,
          DeletePrivilegeNotHeld,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    ObjectInstance oi = serializer.getObjectInstanceIfExists(theObjectName);
    MessageRetractionReturn retraction =
        rtiamb.deleteObjectInstance(oi.getInstanceHandle(), userSuppliedTag, theTime);
    serializer.removeObjectInstance(oi);
    return retraction;
  }

  @Override
  public void requestAttributeValueUpdate(Class clazz, byte[] userSuppliedTag)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    ObjectClass oc = serializer.getObjectClassIfExists(false, clazz);
    rtiamb.requestAttributeValueUpdate(
        oc.getClassHandle(), oc.createAttributeHandleSet(oc.getAttributeSet()), userSuppliedTag);
  }

  @Override
  public void requestAttributeValueUpdate(
      Class clazz, Set<OOattribute> theAttributes, byte[] userSuppliedTag)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    ObjectClass oc = serializer.getObjectClassIfExists(false, clazz);
    rtiamb.requestAttributeValueUpdate(
        oc.getClassHandle(), oc.createAttributeHandleSet(theAttributes), userSuppliedTag);
  }

  @Override
  public void requestAttributeValueUpdate(Object theObject, byte[] userSuppliedTag)
      throws AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    ObjectInstance oi = serializer.getObjectInstanceIfExists(theObject);
    rtiamb.requestAttributeValueUpdate(
        oi.getInstanceHandle(),
        oi.getObjectClass().createAttributeHandleSet(oi.getObjectClass().getAttributeSet()),
        userSuppliedTag);
  }

  @Override
  public void requestAttributeValueUpdate(
      Object theObject, Set<OOattribute> theAttributes, byte[] userSuppliedTag)
      throws AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    ObjectInstance oi = serializer.getObjectInstanceIfExists(theObject);
    rtiamb.requestAttributeValueUpdate(
        oi.getInstanceHandle(),
        oi.getObjectClass().createAttributeHandleSet(theAttributes),
        userSuppliedTag);
  }

  @Override
  public void sendInteraction(Object theInteraction, byte[] userSuppliedTag)
      throws InteractionClassNotPublished,
          InteractionParameterNotDefined,
          InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    SerializedInteractionData interactionData =
        serializer.serializeInteraction(theInteraction, new SerializedInteractionData());
    rtiamb.sendInteraction(
        interactionData.getClassHandle(), interactionData.getParameterValueMap(), userSuppliedTag);
  }

  @Override
  public void sendInteraction(
      Object theInteraction, Set<OOparameter> theParameters, byte[] userSuppliedTag)
      throws InteractionClassNotPublished,
          InteractionParameterNotDefined,
          InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    SerializedInteractionData interactionData =
        serializer.serializeInteraction(
            theInteraction, theParameters, new SerializedInteractionData());
    rtiamb.sendInteraction(
        interactionData.getClassHandle(), interactionData.getParameterValueMap(), userSuppliedTag);
  }

  @Override
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
          RTIinternalError {
    SerializedInteractionData interactionData =
        serializer.serializeInteraction(theInteraction, new SerializedInteractionData());
    return rtiamb.sendInteraction(
        interactionData.getClassHandle(),
        interactionData.getParameterValueMap(),
        userSuppliedTag,
        theTime);
  }

  @Override
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
          RTIinternalError {
    SerializedInteractionData interactionData =
        serializer.serializeInteraction(
            theInteraction, theParameters, new SerializedInteractionData());
    return rtiamb.sendInteraction(
        interactionData.getClassHandle(),
        interactionData.getParameterValueMap(),
        userSuppliedTag,
        theTime);
  }

  //////////////////////////
  // RTI Support Services //
  //////////////////////////
  @Override
  public String getObjectName(Object theObject) throws ObjectInstanceNotKnown {
    return this.serializer.getObjectInstanceIfExists(theObject).getName();
  }

  @Override
  public Object getObject(String theObjectName) throws ObjectInstanceNotKnown {
    return this.serializer.getObjectInstanceIfExists(theObjectName).getObject();
  }
}
