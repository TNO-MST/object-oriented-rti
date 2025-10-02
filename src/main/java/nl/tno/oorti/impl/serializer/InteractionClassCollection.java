package nl.tno.oorti.impl.serializer;

import hla.rti1516e.InteractionClassHandle;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe class to manage InteractionClasses. The collection is accessed by both the federate
 * and RTI ambassador threads.
 *
 * @author bergtwvd
 */
class InteractionClassCollection {

  final Map<Class, InteractionClass> clazz2class = new ConcurrentHashMap<>();
  final Map<InteractionClassHandle, InteractionClass> handle2class = new ConcurrentHashMap<>();

  public InteractionClass add(InteractionClass ic) {
    clazz2class.put(ic.getClazz(), ic);
    handle2class.put(ic.getClassHandle(), ic);
    return ic;
  }

  public InteractionClass remove(InteractionClass ic) {
    return handle2class.remove(clazz2class.remove(ic.getClazz()).getClassHandle());
  }

  public InteractionClass getClassByClazz(Class clazz) {
    return clazz2class.get(clazz);
  }

  public InteractionClass getClassByHandle(InteractionClassHandle classHandle) {
    return handle2class.get(classHandle);
  }
}
