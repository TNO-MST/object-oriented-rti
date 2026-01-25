package nl.tno.oorti.impl;

import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import nl.tno.oorti.OOproperties;

/**
 * Thread-safe class to manage ObjectInstances.
 *
 * @author bergtwvd
 */
public class ObjectInstanceManager {

  // immutable properties
  private final OOproperties properties;

  // mutable properties
  private final Map<ObjectInstanceHandle, ObjectInstance> handle2instance =
      new ConcurrentHashMap<>();
  private final Map<Object, ObjectInstance> object2instance = new ConcurrentHashMap<>();
  private final Map<String, ObjectInstance> name2instance = new ConcurrentHashMap<>();

  public ObjectInstanceManager(OOproperties properties) {
    this.properties = properties;
  }

  public OOproperties getProperties() {
    return this.properties;
  }

  public ObjectInstance create(
      ObjectClass oc, ObjectInstanceHandle instanceHandle, Object theObject, String theName) {

    ObjectInstance oi = new ObjectInstance(this, oc, instanceHandle, theObject, theName);

    this.handle2instance.put(oi.getInstanceHandle(), oi);
    this.name2instance.put(oi.getName(), oi);
    this.object2instance.put(oi.getObject(), oi);

    return oi;
  }

  public ObjectInstance getObjectInstanceByHandle(ObjectInstanceHandle handle) {
    return this.handle2instance.get(handle);
  }

  public ObjectInstance getObjectInstanceByObject(Object object) {
    return this.object2instance.get(object);
  }

  public ObjectInstance getObjectInstanceByName(String name) {
    return this.name2instance.get(name);
  }

  public ObjectInstance getObjectInstanceIfExists(Object theObject) throws ObjectInstanceNotKnown {
    ObjectInstance oi = this.object2instance.get(theObject);
    if (oi == null) {
      throw new ObjectInstanceNotKnown(
          "Unknown object of class " + theObject.getClass().getSimpleName());
    } else {
      return oi;
    }
  }

  public ObjectInstance getObjectInstanceIfExists(String theName) throws ObjectInstanceNotKnown {
    ObjectInstance oi = this.name2instance.get(theName);
    if (oi == null) {
      throw new ObjectInstanceNotKnown("Unknown object " + theName);
    } else {
      return oi;
    }
  }

  public Collection<ObjectInstance> getObjectInstances() {
    return this.object2instance.values();
  }

  public ObjectInstance removeObjectInstance(ObjectInstance objectInstance) {
    // remove in reverse order in the create
    if (this.object2instance.remove(objectInstance.getObject()) != null)
      return this.name2instance.remove(
          this.handle2instance.remove(objectInstance.getInstanceHandle()).getName());
    else return null;
  }
}
