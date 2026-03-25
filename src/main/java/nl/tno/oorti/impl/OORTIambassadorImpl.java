package nl.tno.oorti.impl;

import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.CallbackModel;
import hla.rti1516e.FederateHandle;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.MessageRetractionReturn;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.ResignAction;
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
import hla.rti1516e.exceptions.FederateIsExecutionMember;
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
import hla.rti1516e.exceptions.InvalidResignAction;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.omt.helpers.OmtJavaMapping;
import nl.tno.oorti.OOFederateAmbassador;
import nl.tno.oorti.OORTIambassador;
import nl.tno.oorti.OOattribute;
import nl.tno.oorti.OOobjectFactory;
import nl.tno.oorti.OOparameter;
import nl.tno.oorti.OOproperties;
import nl.tno.oorti.accessor.AccessorFactory;
import nl.tno.oorti.accessor.AccessorFactoryFactory;
import nl.tno.oorti.impl.mim.objects.HLAfederation;
import nl.tno.oorti.ooencoder.OOencoderFactory;
import nl.tno.oorti.ooencoder.OOencoderFactoryFactory;

/**
 * @author bergtwvd
 */
public class OORTIambassadorImpl extends NullRTIambassador implements OORTIambassador {

  // Construction arguments
  final OOobjectFactory objectFactory;
  final OOproperties properties;
  final ExecutionContextManager ecm = new ExecutionContextManager();
  final ExecutionContextManager ecmInitial = new ExecutionContextManager();

  // the callback model used
  CallbackModel callbackModel;

  // MOM related information, shared between the two ambassador threads while getting the current
  // FDD from the RTI in a join federation
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
    OOFederateAmbassadorImpl fedamb = new OOFederateAmbassadorImpl(this, federateReference);

    if (localSettingsDesignator == null) {
      this.rtiamb.connect(fedamb, callbackModel);
    } else {
      this.rtiamb.connect(fedamb, callbackModel, localSettingsDesignator);
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
  public void disconnect()
      throws FederateIsExecutionMember, CallNotAllowedFromWithinCallback, RTIinternalError {
    this.rtiamb.disconnect();
    this.ecm.clear();
  }

  @Override
  public FederateHandle joinFederationExecutionWithCurrentFDD(
      String federateName, // nullable
      String federateType,
      String federationExecutionName,
      URL[] additionalFomModules, // nullable
      URL[] currentFddModules) // nullable
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

    AccessorFactory accessorFactory =
        AccessorFactoryFactory.getAccessorFactory(properties.getAccessorType());

    OOencoderFactory encoderFactory =
        OOencoderFactoryFactory.getOOencoderFactory(
            properties.getEncodingType(), accessorFactory, modules);

    try {
      this.ecm.create(rtiamb, accessorFactory, encoderFactory, modules, properties);
    } catch (FederateNotExecutionMember ex) {
      throw new RTIinternalError(ex.getMessage(), ex);
    }

    return handle;
  }

  @Override
  public FederateHandle joinFederationExecutionWithCurrentFDD(
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
    return this.joinFederationExecutionWithCurrentFDD(
        null, federateType, federationExecutionName, additionalFomModules, currentFddModules);
  }

  @Override
  public FederateHandle joinFederationExecutionWithCurrentFDD(
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
          RTIinternalError {
    return this.joinFederationExecutionWithCurrentFDD(
        federateName, federateType, federationExecutionName, null, currentFddModules);
  }

  @Override
  public FederateHandle joinFederationExecutionWithCurrentFDD(
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
          RTIinternalError {
    return this.joinFederationExecutionWithCurrentFDD(
        null, federateType, federationExecutionName, null, currentFddModules);
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

      ObjectModelType modules[] =
          new ObjectModelType[] {
            OmtFunctions.readOmt(this.getClass().getResource("/foms/HLAstandardMIM.xml"))
          };

      AccessorFactory accessorFactory =
          AccessorFactoryFactory.getAccessorFactory(properties.getAccessorType());

      OOencoderFactory encoderFactory =
          OOencoderFactoryFactory.getOOencoderFactory(
              properties.getEncodingType(), accessorFactory, modules);

      ExecutionContext ec =
          this.ecmInitial.create(rtiamb, accessorFactory, encoderFactory, modules, properties);

      // subscribe to MOM HLAmanager.HLAfederation to get the HLAcurrentFDD
      ObjectClass oc = ec.getOcm().create(HLAfederation.class);
      oc.addSubscriptions(oc.getAttributeByName("HLAcurrentFDD"));

      synchronized (this) {
        this.rtiamb.subscribeObjectClassAttributes(
            oc.getClassHandle(), oc.createAttributeHandleSet(oc.getSubscriptions()));

        for (int i = 0; i < 1000; i++) {
          if (this.callbackModel == CallbackModel.HLA_EVOKED) {
            // Although the IEEE specification states that evokeCallback
            // can be done regardless of the callback mode, the Pitch RTI
            // (tested for 5.5.9-12) hangs when we make this call in
            // Immediate mode.
            // Possibly because the callback thread is already waiting
            // and blocked. Hence we only make this call if and only if
            // in Evoked mode.
            this.rtiamb.evokeCallback(0);
          }

          // wait at most 10 ms for a notification from the FederateAmbassador
          this.wait(10);

          if (this.hlaFederation != null && this.hlaFederation.getHLAcurrentFDD() != null) {
            // we received the current FDD from the RTI and break out of the wait loop
            break;
          }
        }

        // unsubscribe as we are not interested in further updates
        this.rtiamb.unsubscribeObjectClass(oc.getClassHandle());

        // if we got an instance, delete it
        if (this.hlaFederationInstanceHandle != null) {
          // We need to locally delete the object instance, so that
          // subscribers on the MIM can re-discover the instance.
          // Also, the local delete must be done after we unsubscribe.
          this.rtiamb.localDeleteObjectInstance(this.hlaFederationInstanceHandle);
          this.hlaFederationInstanceHandle = null;
        }

        if (this.hlaFederation == null || this.hlaFederation.getHLAcurrentFDD() == null) {
          throw new RTIinternalError("Did not receive CurrentFDD");
        }
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
    return joinFederationExecutionWithCurrentFDD(
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
      return joinFederationExecutionWithCurrentFDD(
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
      return joinFederationExecutionWithCurrentFDD(
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
      return joinFederationExecutionWithCurrentFDD(
          null, federateType, federationExecutionName, null, null);
    } catch (InconsistentFDD | ErrorReadingFDD | CouldNotOpenFDD | FederateNameAlreadyInUse ex) {
      // should not get here, since we pass nulls
      throw new RTIinternalError(ex.getMessage(), ex);
    }
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
    this.rtiamb.resignFederationExecution(resignAction);
    this.ecm.clear();
  }

  /////////////////////////////////////
  // Declaration Management Services //
  /////////////////////////////////////

  ////////////////////
  // Object classes //
  ////////////////////

  @Override
  public void publishObjectClass(Class clazz)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectClass oc = ec.getOcm().create(clazz);
    rtiamb.publishObjectClassAttributes(oc.getClassHandle(), oc.createAttributeHandleSet());
    oc.addPublications();
  }

  @Override
  public void publishObjectClass(Class clazz, Set<OOattribute> attributes)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectClass oc = ec.getOcm().create(clazz);
    rtiamb.publishObjectClassAttributes(
        oc.getClassHandle(), oc.createAttributeHandleSet(attributes));
    oc.addPublications(attributes);
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

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectClass oc = ec.getOcm().getObjectClassIfExists(clazz);
    rtiamb.unpublishObjectClassAttributes(oc.getClassHandle(), oc.createAttributeHandleSet());
    oc.removePublications();
  }

  @Override
  public void subscribeObjectClass(Class clazz)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectClass oc = ec.getOcm().create(clazz);
    rtiamb.subscribeObjectClassAttributes(oc.getClassHandle(), oc.createAttributeHandleSet());
    oc.addPublications();
  }

  @Override
  public void subscribeObjectClass(Class clazz, Set<OOattribute> attributes)
      throws AttributeNotDefined,
          ObjectClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectClass oc = ec.getOcm().create(clazz);
    rtiamb.subscribeObjectClassAttributes(
        oc.getClassHandle(), oc.createAttributeHandleSet(attributes));
    oc.addPublications(attributes);
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

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectClass oc = ec.getOcm().getObjectClassIfExists(clazz);
    rtiamb.unsubscribeObjectClass(oc.getClassHandle());
    oc.removeSubscriptions();

    // also delete all remote object instances of this class
    for (ObjectInstance oi : ec.getOim().getObjectInstances()) {
      if (oi.getObjectClass().equals(oc)) {
        try {
          rtiamb.localDeleteObjectInstance(oi.getInstanceHandle());
          ec.getOim().removeObjectInstance(oi);
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
  public void publishInteractionClass(Class clazz)
      throws InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError,
          InteractionParameterNotDefined {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    InteractionClass interactionClass = ec.getIcm().create(clazz);
    rtiamb.publishInteractionClass(interactionClass.getClassHandle());
    interactionClass.addPublications();
  }

  @Override
  public void publishInteractionClass(Class clazz, Set<OOparameter> parameters)
      throws FederateNotExecutionMember,
          NotConnected,
          RTIinternalError,
          InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          InteractionParameterNotDefined {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    InteractionClass interactionClass = ec.getIcm().create(clazz);
    rtiamb.publishInteractionClass(interactionClass.getClassHandle());
    interactionClass.addPublications(parameters);
  }

  @Override
  public void unpublishInteractionClass(Class clazz)
      throws InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    InteractionClass interactionClass = ec.getIcm().getInteractionClassIfExists(clazz);
    rtiamb.unpublishInteractionClass(interactionClass.getClassHandle());
    interactionClass.removePublications();
  }

  @Override
  public void subscribeInteractionClass(Class clazz)
      throws FederateNotExecutionMember,
          NotConnected,
          RTIinternalError,
          InteractionParameterNotDefined,
          InteractionClassNotDefined,
          FederateServiceInvocationsAreBeingReportedViaMOM,
          SaveInProgress,
          RestoreInProgress {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    InteractionClass interactionClass = ec.getIcm().create(clazz);
    rtiamb.subscribeInteractionClass(interactionClass.getClassHandle());
    interactionClass.addSubscriptions();
  }

  @Override
  public void subscribeInteractionClass(Class clazz, Set<OOparameter> parameters)
      throws FederateNotExecutionMember,
          NotConnected,
          RTIinternalError,
          InteractionParameterNotDefined,
          InteractionClassNotDefined,
          FederateServiceInvocationsAreBeingReportedViaMOM,
          SaveInProgress,
          RestoreInProgress {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    InteractionClass interactionClass = ec.getIcm().create(clazz);
    rtiamb.subscribeInteractionClass(interactionClass.getClassHandle());
    interactionClass.addSubscriptions(parameters);
  }

  @Override
  public void unsubscribeInteractionClass(Class clazz)
      throws InteractionClassNotDefined,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    InteractionClass interactionClass = ec.getIcm().getInteractionClassIfExists(clazz);
    rtiamb.unsubscribeInteractionClass(interactionClass.getClassHandle());
    interactionClass.removeSubscriptions();
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

    ExecutionContext ec = this.ecm.getExecutionContextIfExists();

    ObjectClass oc =
        ec.getOcm().getObjectClassIfExists(this.objectFactory.getObjectClass(theObject));
    ObjectInstanceHandle instanceHandle = this.rtiamb.registerObjectInstance(oc.getClassHandle());
    ec.getOim()
        .create(oc, instanceHandle, theObject, this.rtiamb.getObjectInstanceName(instanceHandle));
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

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObjectName);
    rtiamb.updateAttributeValues(oi.getInstanceHandle(), oi.serialize(theObject), userSuppliedTag);
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

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObject);
    rtiamb.updateAttributeValues(oi.getInstanceHandle(), oi.serialize(), userSuppliedTag);
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

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObject);
    rtiamb.updateAttributeValues(
        oi.getInstanceHandle(), oi.serialize(theAttributes), userSuppliedTag);
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

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObject);
    return rtiamb.updateAttributeValues(
        oi.getInstanceHandle(), oi.serialize(), userSuppliedTag, theTime);
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

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObjectName);
    return rtiamb.updateAttributeValues(
        oi.getInstanceHandle(), oi.serialize(theObject), userSuppliedTag, theTime);
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

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObject);
    return rtiamb.updateAttributeValues(
        oi.getInstanceHandle(), oi.serialize(theAttributes), userSuppliedTag, theTime);
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

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObject);
    rtiamb.deleteObjectInstance(oi.getInstanceHandle(), userSuppliedTag);
    ec.getOim().removeObjectInstance(oi);
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

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObjectName);
    rtiamb.deleteObjectInstance(oi.getInstanceHandle(), userSuppliedTag);
    ec.getOim().removeObjectInstance(oi);
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

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObject);
    MessageRetractionReturn retraction =
        rtiamb.deleteObjectInstance(oi.getInstanceHandle(), userSuppliedTag, theTime);
    ec.getOim().removeObjectInstance(oi);
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

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObjectName);
    MessageRetractionReturn retraction =
        rtiamb.deleteObjectInstance(oi.getInstanceHandle(), userSuppliedTag, theTime);
    ec.getOim().removeObjectInstance(oi);
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

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectClass oc = ec.getOcm().getObjectClassIfExists(clazz);
    rtiamb.requestAttributeValueUpdate(
        oc.getClassHandle(), oc.createAttributeHandleSet(), userSuppliedTag);
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

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectClass oc = ec.getOcm().getObjectClassIfExists(clazz);
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

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObject);
    rtiamb.requestAttributeValueUpdate(
        oi.getInstanceHandle(), oi.getObjectClass().createAttributeHandleSet(), userSuppliedTag);
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

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObject);
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

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    InteractionClass ic =
        ec.getIcm()
            .getInteractionClassIfExists(this.objectFactory.getInteractionClass(theInteraction));
    rtiamb.sendInteraction(ic.getClassHandle(), ic.serialize(theInteraction), userSuppliedTag);
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

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    InteractionClass ic =
        ec.getIcm()
            .getInteractionClassIfExists(this.objectFactory.getInteractionClass(theInteraction));
    rtiamb.sendInteraction(
        ic.getClassHandle(), ic.serialize(theInteraction, theParameters), userSuppliedTag);
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

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    InteractionClass ic =
        ec.getIcm()
            .getInteractionClassIfExists(this.objectFactory.getInteractionClass(theInteraction));
    return rtiamb.sendInteraction(
        ic.getClassHandle(), ic.serialize(theInteraction), userSuppliedTag, theTime);
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

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    InteractionClass ic =
        ec.getIcm()
            .getInteractionClassIfExists(this.objectFactory.getInteractionClass(theInteraction));
    return rtiamb.sendInteraction(
        ic.getClassHandle(), ic.serialize(theInteraction, theParameters), userSuppliedTag, theTime);
  }

  ///////////////////////////////////
  // Ownership Management Services //
  ///////////////////////////////////

  @Override
  public void unconditionalAttributeOwnershipDivestiture(
      Object theObject, Set<OOattribute> theAttributes)
      throws AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObject);
    AttributeHandleSet ahs = oi.getObjectClass().createAttributeHandleSet(theAttributes);
    this.rtiamb.unconditionalAttributeOwnershipDivestiture(oi.getInstanceHandle(), ahs);
  }

  @Override
  public void negotiatedAttributeOwnershipDivestiture(
      Object theObject, Set<OOattribute> theAttributes, byte[] userSuppliedTag)
      throws AttributeAlreadyBeingDivested,
          AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObject);
    AttributeHandleSet ahs = oi.getObjectClass().createAttributeHandleSet(theAttributes);
    this.rtiamb.negotiatedAttributeOwnershipDivestiture(
        oi.getInstanceHandle(), ahs, userSuppliedTag);
  }

  @Override
  public void confirmDivestiture(
      Object theObject, Set<OOattribute> theAttributes, byte[] userSuppliedTag)
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

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObject);
    AttributeHandleSet ahs = oi.getObjectClass().createAttributeHandleSet(theAttributes);
    this.rtiamb.confirmDivestiture(oi.getInstanceHandle(), ahs, userSuppliedTag);
  }

  @Override
  public void attributeOwnershipAcquisition(
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
          RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObject);
    AttributeHandleSet ahs = oi.getObjectClass().createAttributeHandleSet(desiredAttributes);
    this.rtiamb.attributeOwnershipAcquisition(oi.getInstanceHandle(), ahs, userSuppliedTag);
  }

  @Override
  public void attributeOwnershipAcquisitionIfAvailable(
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
          RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObject);
    AttributeHandleSet ahs = oi.getObjectClass().createAttributeHandleSet(desiredAttributes);
    this.rtiamb.attributeOwnershipAcquisitionIfAvailable(oi.getInstanceHandle(), ahs);
  }

  @Override
  public void attributeOwnershipReleaseDenied(Object theObject, Set<OOattribute> theAttributes)
      throws AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObject);
    AttributeHandleSet ahs = oi.getObjectClass().createAttributeHandleSet(theAttributes);
    this.rtiamb.attributeOwnershipReleaseDenied(oi.getInstanceHandle(), ahs);
  }

  @Override
  public Set<OOattribute> attributeOwnershipDivestitureIfWanted(
      Object theObject, Set<OOattribute> theAttributes)
      throws AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObject);
    AttributeHandleSet ahs = oi.getObjectClass().createAttributeHandleSet(theAttributes);
    AttributeHandleSet ahs2 =
        this.rtiamb.attributeOwnershipDivestitureIfWanted(oi.getInstanceHandle(), ahs);
    return oi.getObjectClass().createAttributeSet(ahs2);
  }

  @Override
  public void cancelNegotiatedAttributeOwnershipDivestiture(
      Object theObject, Set<OOattribute> theAttributes)
      throws AttributeDivestitureWasNotRequested,
          AttributeNotOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObject);
    AttributeHandleSet ahs = oi.getObjectClass().createAttributeHandleSet(theAttributes);
    this.rtiamb.cancelNegotiatedAttributeOwnershipDivestiture(oi.getInstanceHandle(), ahs);
  }

  @Override
  public void cancelAttributeOwnershipAcquisition(Object theObject, Set<OOattribute> theAttributes)
      throws AttributeAcquisitionWasNotRequested,
          AttributeAlreadyOwned,
          AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObject);
    AttributeHandleSet ahs = oi.getObjectClass().createAttributeHandleSet(theAttributes);
    this.rtiamb.cancelAttributeOwnershipAcquisition(oi.getInstanceHandle(), ahs);
  }

  @Override
  public void queryAttributeOwnership(Object theObject, OOattribute theAttribute)
      throws AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObject);
    this.rtiamb.queryAttributeOwnership(
        oi.getInstanceHandle(), Attribute.class.cast(theAttribute).getAttributeHandle());
  }

  @Override
  public boolean isAttributeOwnedByFederate(Object theObject, OOattribute theAttribute)
      throws AttributeNotDefined,
          ObjectInstanceNotKnown,
          SaveInProgress,
          RestoreInProgress,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    ObjectInstance oi = ec.getOim().getObjectInstanceIfExists(theObject);
    return this.rtiamb.isAttributeOwnedByFederate(
        oi.getInstanceHandle(), Attribute.class.cast(theAttribute).getAttributeHandle());
  }

  //////////////////////////
  // RTI Support Services //
  //////////////////////////

  @Override
  public String getObjectClassName(Class theClass)
      throws ObjectClassNotDefined, FederateNotExecutionMember, NotConnected, RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    try {
      return ec.getOcm().create(theClass).getName();
    } catch (AttributeNotDefined ex) {
      throw new ObjectClassNotDefined(ex.getMessage(), ex);
    }
  }

  @Override
  public Class getObjectClass(String theClassName)
      throws ObjectClassNotDefined, FederateNotExecutionMember, NotConnected, RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    return ec.getOcm().getObjectClassIfExists(theClassName).getClazz();
  }

  @Override
  public String getInteractionClassName(Class theClass)
      throws InteractionClassNotDefined,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    try {
      return ec.getIcm().create(theClass).getName();
    } catch (InteractionParameterNotDefined ex) {
      throw new InteractionClassNotDefined(ex.getMessage(), ex);
    }
  }

  @Override
  public Class getInteractionClass(String theClassName)
      throws InteractionClassNotDefined,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    return ec.getIcm().getInteractionClassIfExists(theClassName).getClazz();
  }

  @Override
  public String getObjectName(Object theObject)
      throws ObjectInstanceNotKnown, FederateNotExecutionMember {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    return ec.getOim().getObjectInstanceIfExists(theObject).getName();
  }

  @Override
  public Object getObject(String theObjectName)
      throws ObjectInstanceNotKnown, FederateNotExecutionMember {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    return ec.getOim().getObjectInstanceIfExists(theObjectName).getObject();
  }

  @Override
  public OOattribute getAttribute(Class clazz, String attributeName)
      throws ObjectClassNotDefined,
          AttributeNotDefined,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    ExecutionContext ec = ecm.getExecutionContextIfExists();

    return ec.getOcm().create(clazz).getAttributeIfExists(OmtJavaMapping.toOmtName(attributeName));
  }

  @Override
  public Set<OOattribute> getAttributes(Class clazz, String... attributeName)
      throws ObjectClassNotDefined,
          AttributeNotDefined,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    Set<OOattribute> attributes = new HashSet<>();
    for (String name : attributeName) {
      Attribute a = ec.getOcm().create(clazz).getAttributeIfExists(OmtJavaMapping.toOmtName(name));
      attributes.add(a);
    }
    return attributes;
  }

  @Override
  public Set<OOattribute> getAttributes(Class clazz)
      throws ObjectClassNotDefined,
          AttributeNotDefined,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    Set<OOattribute> attributes = new HashSet<>();
    for (Attribute attribute : ec.getOcm().create(clazz).getAttributes()) {
      attributes.add(attribute);
    }
    return attributes;
  }

  @Override
  public OOparameter getParameter(Class clazz, String parameterName)
      throws InteractionClassNotDefined,
          InteractionParameterNotDefined,
          NotConnected,
          RTIinternalError,
          FederateNotExecutionMember {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    return ec.getIcm().create(clazz).getParameterIfExists(OmtJavaMapping.toOmtName(parameterName));
  }

  @Override
  public Set<OOparameter> getParameters(Class clazz, String... parameterName)
      throws InteractionClassNotDefined,
          InteractionParameterNotDefined,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {

    ExecutionContext ec = ecm.getExecutionContextIfExists();

    Set<OOparameter> parameters = new HashSet<>();
    for (String name : parameterName) {
      Parameter p = ec.getIcm().create(clazz).getParameterIfExists(OmtJavaMapping.toOmtName(name));
      parameters.add(p);
    }
    return parameters;
  }

  @Override
  public Set<OOparameter> getParameters(Class clazz)
      throws InteractionClassNotDefined,
          InteractionParameterNotDefined,
          FederateNotExecutionMember,
          NotConnected,
          RTIinternalError {
    ExecutionContext ec = ecm.getExecutionContextIfExists();

    Set<OOparameter> parameters = new HashSet<>();
    for (Parameter attribute : ec.getIcm().create(clazz).getParameters()) {
      parameters.add(attribute);
    }
    return parameters;
  }
}
