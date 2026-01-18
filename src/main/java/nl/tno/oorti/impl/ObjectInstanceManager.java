package nl.tno.oorti.impl;

import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import nl.tno.oorti.OOproperties;

/**
 * Thread-safe class to manage ObjectInstances. The collection is accessed by both the federate and
 * RTI ambassador threads.
 *
 * @author bergtwvd
 */
public class ObjectInstanceManager {
  
  // static properties
  private final OOproperties properties;

  // dynamic properties
  private final Map<ObjectInstanceHandle, ObjectInstance> handle2instance =
      new ConcurrentHashMap<>();
  private final Map<Object, ObjectInstance> object2instance = new ConcurrentHashMap<>();
  private final Map<String, ObjectInstance> name2instance = new ConcurrentHashMap<>();

  public ObjectInstanceManager(OOproperties properties) {
    this.properties = properties;
  }

  public OOproperties getProperties() {
    return properties;
  }

  public ObjectInstance create(
      ObjectClass oc, ObjectInstanceHandle instanceHandle, Object theObject, String theName)
      throws ObjectClassNotDefined {

    if (!oc.getClazz().isInstance(theObject)) {
      throw new ObjectClassNotDefined("Object " + theName + " not of type " + oc.getName());
    }

    ObjectInstance objectInstance = new ObjectInstance(this, oc, instanceHandle, theObject, theName);

    handle2instance.put(objectInstance.getInstanceHandle(), objectInstance);
    name2instance.put(objectInstance.getName(), objectInstance);
    object2instance.put(objectInstance.getObject(), objectInstance);

    return objectInstance;
  }

  public ObjectInstance getObjectInstanceByHandle(ObjectInstanceHandle handle) {
    return handle2instance.get(handle);
  }

  public ObjectInstance getObjectInstanceByObject(Object object) {
    return object2instance.get(object);
  }

  public ObjectInstance getObjectInstanceByName(String name) {
    return name2instance.get(name);
  }

  public ObjectInstance getObjectInstanceIfExists(Object theObject) throws ObjectInstanceNotKnown {
    ObjectInstance oi = object2instance.get(theObject);
    if (oi == null) {
      throw new ObjectInstanceNotKnown(
          "Unknown object of class " + theObject.getClass().getSimpleName());
    } else {
      return oi;
    }
  }
  
  public ObjectInstance getObjectInstanceIfExists(String theName) throws ObjectInstanceNotKnown {
    ObjectInstance oi = name2instance.get(theName);
    if (oi == null) {
      throw new ObjectInstanceNotKnown("Unknown object " + theName);
    } else {
      return oi;
    }
  }
  
  public Collection<ObjectInstance> getObjectInstances() {
    return object2instance.values();
  }
  
  public ObjectInstance removeObjectInstance(ObjectInstance objectInstance) {
    if (object2instance.remove(objectInstance.getObject()) != null)
      return name2instance.remove(
          handle2instance.remove(objectInstance.getInstanceHandle()).getName());
    else return null;
  }
}
