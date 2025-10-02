package nl.tno.oorti.impl.serializer;

import hla.rti1516e.ObjectInstanceHandle;

/**
 * @author bergtwvd
 */
public class ObjectInstance {

  private final ObjectClass objectClass;
  private final ObjectInstanceHandle instanceHandle;
  private final Object theObject;
  private final String theName;

  ObjectInstance(
      ObjectClass objectClass,
      ObjectInstanceHandle instanceHandle,
      Object theObject,
      String theName) {
    this.objectClass = objectClass;
    this.instanceHandle = instanceHandle;
    this.theObject = theObject;
    this.theName = theName;
  }

  public ObjectClass getObjectClass() {
    return this.objectClass;
  }

  public ObjectInstanceHandle getInstanceHandle() {
    return this.instanceHandle;
  }

  public Object getObject() {
    return this.theObject;
  }

  public String getName() {
    return this.theName;
  }
}
