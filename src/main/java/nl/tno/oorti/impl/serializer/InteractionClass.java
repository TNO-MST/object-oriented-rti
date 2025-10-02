package nl.tno.oorti.impl.serializer;

import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.ParameterHandle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import nl.tno.oorti.OOparameter;

/**
 * @author bergtwvd
 */
public class InteractionClass {

  private final Class clazz;
  private final String name;
  private final InteractionClassHandle classHandle;

  // there are filled during InteractionClass initialization, and after that are static
  // only after the initialization completes, this data will be accessed
  private final Set<OOparameter> parameterSet = new HashSet<>();
  private final Map<ParameterHandle, Parameter> handle2parameter = new HashMap<>();

  InteractionClass(Class clazz, String className, InteractionClassHandle classHandle) {
    this.clazz = clazz;
    this.name = className;
    this.classHandle = classHandle;
  }

  void addParameter(Parameter parameter) {
    parameterSet.add(parameter);
    handle2parameter.put(parameter.getParameterHandle(), parameter);
  }

  public Parameter getParameterByHandle(ParameterHandle handle) {
    return this.handle2parameter.get(handle);
  }

  public Class getClazz() {
    return clazz;
  }

  public String getName() {
    return name;
  }

  public InteractionClassHandle getClassHandle() {
    return classHandle;
  }

  public Set<OOparameter> getParameterSet() {
    return parameterSet;
  }
}
