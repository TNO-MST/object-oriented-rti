package nl.tno.oorti.impl;

import hla.rti1516e.RTIambassador;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.NotConnected;
import nl.tno.omt.ObjectModelType;
import nl.tno.oorti.OOproperties;
import nl.tno.oorti.accessor.AccessorFactory;
import nl.tno.oorti.ooencoder.OOencoderFactory;

/**
 * @author bergtwvd
 */
class ExecutionContextManager {

  // mutable properties
  volatile ExecutionContext context = null;

  ExecutionContext getExecutionContext() {
    return this.context;
  }

  ExecutionContext getExecutionContextIfExists() throws FederateNotExecutionMember {
    // take a local copy of the volatile variable before testing it
    ExecutionContext tmp = this.context;
    if (tmp == null) throw new FederateNotExecutionMember("No execution context.");
    else return tmp;
  }

  ExecutionContext create(
      RTIambassador rtiamb,
      AccessorFactory accessorFactory,
      OOencoderFactory encoderFactory,
      ObjectModelType[] modules,
      OOproperties properties) throws FederateNotExecutionMember, NotConnected {

    ObjectClassManager ocm =
        new ObjectClassManager(rtiamb, accessorFactory, encoderFactory, modules);
    InteractionClassManager icm =
        new InteractionClassManager(rtiamb, accessorFactory, encoderFactory, modules);
    ObjectInstanceManager oim = new ObjectInstanceManager(properties);

    return this.context = new ExecutionContext(ocm, icm, oim);
  }

  void clear() {
    this.context = null;
  }
}
