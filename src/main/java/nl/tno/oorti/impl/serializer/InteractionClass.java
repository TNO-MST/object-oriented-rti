package nl.tno.oorti.impl.serializer;

import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.ParameterHandleValueMapFactory;
import hla.rti1516e.exceptions.RTIinternalError;
import jakarta.json.bind.JsonbBuilder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.tno.oorti.OOparameter;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * @author bergtwvd
 */
public class InteractionClass {

  private final Class clazz;
  private final String name;
  private final InteractionClassHandle classHandle;
  private final ParameterHandleValueMapFactory factory;
  private final Set<Parameter> parameters = new HashSet<>();
  private final Map<String, Parameter> name2parameter = new HashMap<>();
  private final Map<ParameterHandle, Parameter> handle2parameter = new HashMap<>();

  private final Set<OOparameter> pubParmSet = ConcurrentHashMap.newKeySet();
  private final Set<OOparameter> subParmSet = ConcurrentHashMap.newKeySet();

  InteractionClass(
      Class clazz,
      String className,
      InteractionClassHandle classHandle,
      ParameterHandleValueMapFactory factory) {
    this.clazz = clazz;
    this.name = className;
    this.classHandle = classHandle;
    this.factory = factory;
  }

  void addParameter(Parameter parameter) {
    parameters.add(parameter);
    name2parameter.put(parameter.getName(), parameter);
    handle2parameter.put(parameter.getParameterHandle(), parameter);
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

  public Set<Parameter> getParameters() {
    return parameters;
  }

  public void addPublications() {
    for (Parameter parameter : this.parameters) {
      pubParmSet.add(parameter);
    }
  }

  public void addPublications(Set<String> theParameterNames) {
    for (String parameterName : theParameterNames) {
      Parameter parameter = this.name2parameter.get(parameterName);
      if (parameter != null) {
        pubParmSet.add(parameter);
      }
    }
  }

  public void addSubscriptions() {
    for (Parameter parameter : this.parameters) {
      subParmSet.add(parameter);
    }
  }

  public void addSubscriptions(Set<String> theParameterNames) {
    for (String parameterName : theParameterNames) {
      Parameter parameter = this.name2parameter.get(parameterName);
      if (parameter != null) {
        subParmSet.add(parameter);
      }
    }
  }

  public void removePublications(Set<String> theParameterNames) {
    for (String parameterName : theParameterNames) {
      Parameter parameter = this.name2parameter.get(parameterName);
      if (parameter != null) {
        pubParmSet.remove(parameter);
      }
    }
  }

  public void removeSubscriptions(Set<String> theParameterNames) {
    for (String parameterName : theParameterNames) {
      Parameter parameter = this.name2parameter.get(parameterName);
      if (parameter != null) {
        subParmSet.remove(parameter);
      }
    }
  }

  public void removePublications() {
    this.pubParmSet.clear();
  }

  public void removeSubscriptions() {
    this.subParmSet.clear();
  }

  public Set<OOparameter> getSubscriptions() {
    return subParmSet;
  }

  public Set<OOparameter> getPublications() {
    return pubParmSet;
  }

  public ParameterHandleValueMap serialize(Object theInteraction) throws RTIinternalError {
    return serialize(theInteraction, pubParmSet);
  }

  public ParameterHandleValueMap serialize(Object theInteraction, Set<OOparameter> parameterSet)
      throws RTIinternalError {
    try {
      ParameterHandleValueMap parameterValueMap = factory.create(parameterSet.size());

      for (OOparameter ooParameter : parameterSet) {
        Parameter parameter = (Parameter) ooParameter;

        // get the parameter valuea
        Object value = parameter.getAccessor().get(theInteraction);
        if (value == null) {
          // do not serialize null value; skip
          continue;
        }

        try {
          // encode the parameter value to bytes
          byte[] bytes = parameter.getEncoder().encode(value);

          // add to results
          parameterValueMap.put(parameter.getParameterHandle(), bytes);
        } catch (OOcodecException ex) {
          Logger.getLogger(InteractionClass.class.getName())
              .log(
                  Level.WARNING,
                  "Error encoding class={0}, parameter={1}, codec={2}, value={3}",
                  new Object[] {
                    this.name,
                    parameter.getName(),
                    parameter.getEncoder().toString(),
                    JsonbBuilder.create().toJson(value)
                  });
          throw new RTIinternalError(ex.getMessage(), ex);
        }
      }

      return parameterValueMap;
    } catch (ReflectiveOperationException ex) {
      throw new RTIinternalError(ex.getMessage(), ex);
    }
  }

  public Set<OOparameter> deserialize(
      ParameterHandleValueMap parameterValueMap, Object theInteraction) throws RTIinternalError {
    try {
      Set<OOparameter> parameterSet = new HashSet<>();

      for (Entry<ParameterHandle, byte[]> entry : parameterValueMap.entrySet()) {
        Parameter parameter = this.handle2parameter.get(entry.getKey());
        if (parameter == null) {
          // received parameter not in Java Class, so skip
          continue;
        }

        // decode the bytes to a parameter value
        Object value;
        try {
          value = parameter.getEncoder().decode(entry.getValue(), null, theInteraction);
        } catch (OOcodecException ex) {
          Logger.getLogger(InteractionClass.class.getName())
              .log(
                  Level.WARNING,
                  "Error decoding class={0}, parameter={1}, codec={2}, len={3}, bytes={4}",
                  new Object[] {
                    this.getName(),
                    parameter.getName(),
                    parameter.getEncoder().toString(),
                    entry.getValue().length,
                    Serializer.bytesToHex(entry.getValue())
                  });
          throw new RTIinternalError(ex.getMessage(), ex);
        }

        // set the new parameter value
        parameter.getAccessor().set(theInteraction, value);

        parameterSet.add(parameter);
      }

      return parameterSet;
    } catch (ReflectiveOperationException ex) {
      throw new RTIinternalError(ex.getMessage(), ex);
    }
  }
}
