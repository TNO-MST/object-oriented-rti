package nl.tno.oorti.impl.serializer;

import hla.rti1516e.ObjectClassHandle;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe class to manage ObjectClasses. The collection is accessed by both the federate and
 * RTI ambassador threads.
 *
 * @author bergtwvd
 */
class ObjectClassCollection {

  private final Map<Class, ObjectClass> clazz2class = new ConcurrentHashMap<>();
  private final Map<ObjectClassHandle, ObjectClass> handle2class = new ConcurrentHashMap<>();

  public ObjectClass add(ObjectClass objectClass) {
    clazz2class.put(objectClass.getClazz(), objectClass);
    handle2class.put(objectClass.getClassHandle(), objectClass);
    return objectClass;
  }

  public ObjectClass remove(ObjectClass objectClass) {
    return handle2class.remove(clazz2class.remove(objectClass.getClazz()).getClassHandle());
  }

  public ObjectClass getClassByClazz(Class clazz) {
    return clazz2class.get(clazz);
  }

  public ObjectClass getClassByHandle(ObjectClassHandle classHandle) {
    return handle2class.get(classHandle);
  }

  public Collection<ObjectClass> getClasses() {
    return handle2class.values();
  }
}
