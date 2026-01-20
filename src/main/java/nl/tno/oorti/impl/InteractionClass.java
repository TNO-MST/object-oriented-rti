package nl.tno.oorti.impl;

import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.ParameterHandleValueMapFactory;
import hla.rti1516e.exceptions.InteractionParameterNotDefined;
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

  // immutable properties
  private final Class clazz;
  private final String name;
  private final InteractionClassHandle classHandle;
  private final Set<Parameter> parameters;
  private final ParameterHandleValueMapFactory factory;
  private final Map<String, Parameter> name2parameter = new HashMap<>();
  private final Map<ParameterHandle, Parameter> handle2parameter = new HashMap<>();

  // mutable properties
  private final Set<OOparameter> pubParmSet = ConcurrentHashMap.newKeySet();
  private final Set<OOparameter> subParmSet = ConcurrentHashMap.newKeySet();

  public InteractionClass(
      Class clazz,
      String className,
      InteractionClassHandle classHandle,
      Set<Parameter> parameterSet,
      ParameterHandleValueMapFactory factory) {
    this.clazz = clazz;
    this.name = className;
    this.classHandle = classHandle;
    this.parameters = parameterSet;
    this.factory = factory;

    for (Parameter parameter : parameterSet) {
      name2parameter.put(parameter.getName(), parameter);
      handle2parameter.put(parameter.getParameterHandle(), parameter);
    }
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

  public Parameter getParameterByName(String name) {
    return this.name2parameter.get(name);
  }

  public Parameter getParameterByNameIfExists(String name) throws InteractionParameterNotDefined {
    Parameter paraneter = name2parameter.get(name);
    if (paraneter != null) return paraneter;
    else throw new InteractionParameterNotDefined(name);
  }

  public Set<OOparameter> getSubscriptions() {
    return subParmSet;
  }

  public Set<OOparameter> getPublications() {
    return pubParmSet;
  }

  public void addPublications() {
    for (Parameter parameter : this.parameters) {
      pubParmSet.add(parameter);
    }
  }

  public void addPublications(Set<? extends Object> cookies) throws InteractionParameterNotDefined {
    for (Object cookie : cookies) {
      String parameterName = cookie.toString();
      Parameter parameter = this.name2parameter.get(parameterName);
      if (parameter != null) {
        parameter.setCookie(cookie);
        pubParmSet.add(parameter);
      } else
        throw new InteractionParameterNotDefined(
            "Unknown parameter " + parameterName + " for class " + name);
    }
  }

  public void addSubscriptions() {
    for (Parameter parameter : this.parameters) {
      subParmSet.add(parameter);
    }
  }

  public void addSubscriptions(Set<? extends Object> cookies)
      throws InteractionParameterNotDefined {
    for (Object cookie : cookies) {
      String parameterName = cookie.toString();
      Parameter parameter = this.name2parameter.get(parameterName);
      if (parameter != null) {
        parameter.setCookie(cookie);
        subParmSet.add(parameter);
      } else
        throw new InteractionParameterNotDefined(
            "Unknown parameter " + parameterName + " for class " + name);
    }
  }

  public void removePublications() {
    this.pubParmSet.clear();
  }

  public void removePublications(Set<String> theParameterNames)
      throws InteractionParameterNotDefined {
    for (String parameterName : theParameterNames) {
      Parameter parameter = this.name2parameter.get(parameterName);
      if (parameter != null) {
        pubParmSet.remove(parameter);
      } else
        throw new InteractionParameterNotDefined(
            "Unknown parameter " + parameterName + " for class " + name);
    }
  }

  public void removeSubscriptions() {
    this.subParmSet.clear();
  }

  public void removeSubscriptions(Set<String> theParameterNames)
      throws InteractionParameterNotDefined {
    for (String parameterName : theParameterNames) {
      Parameter parameter = this.name2parameter.get(parameterName);
      if (parameter != null) {
        subParmSet.remove(parameter);
      } else
        throw new InteractionParameterNotDefined(
            "Unknown parameter " + parameterName + " for class " + name);
    }
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

        Object value = parameter.getAccessor().get(theInteraction);
        if (value == null) {
          // do not serialize null value; skip
          continue;
        }

        try {
          byte[] bytes = parameter.getEncoder().encode(value);
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
                    Helpers.bytesToHex(entry.getValue())
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
