package nl.tno.oorti.impl.serializer;

import hla.rti1516e.ObjectInstanceHandle;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe class to manage ObjectInstances. The collection is accessed by both the federate and
 * RTI ambassador threads.
 *
 * @author bergtwvd
 */
class ObjectInstanceCollection {

  private final Map<ObjectInstanceHandle, ObjectInstance> handle2instance =
      new ConcurrentHashMap<>();
  private final Map<Object, ObjectInstance> object2instance = new ConcurrentHashMap<>();
  private final Map<String, ObjectInstance> name2instance = new ConcurrentHashMap<>();

  public ObjectInstance add(ObjectInstance objectInstance) {
    handle2instance.put(objectInstance.getInstanceHandle(), objectInstance);
    object2instance.put(objectInstance.getObject(), objectInstance);
    name2instance.put(objectInstance.getName(), objectInstance);
    return objectInstance;
  }

  public ObjectInstance getObjectInstanceByHandle(ObjectInstanceHandle instanceHandle) {
    return handle2instance.get(instanceHandle);
  }

  public ObjectInstance getObjectInstanceByObject(Object theObject) {
    return object2instance.get(theObject);
  }

  public ObjectInstance getObjectInstanceByName(String theName) {
    return name2instance.get(theName);
  }

  public ObjectInstance removeObjectInstance(ObjectInstance objectInstance) {
    if (object2instance.remove(objectInstance.getObject()) != null)
      return name2instance.remove(
          handle2instance.remove(objectInstance.getInstanceHandle()).getName());
    else return null;
  }

  public Collection<ObjectInstance> getObjectInstances() {
    return this.object2instance.values();
  }
}
