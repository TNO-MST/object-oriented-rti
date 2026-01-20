package nl.tno.oorti.impl;

import hla.rti1516e.ParameterHandle;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import nl.tno.oorti.accessor.Accessor;
import nl.tno.oorti.ooencoder.OOencoder;

/**
 * @author bergtwvd
 */
class ParameterManager {

  // mutable properties
  private final Map<ParameterHandle, Parameter> handle2parameter = new ConcurrentHashMap<>();

  ParameterManager() {}

  Parameter create(
      String parameterName,
      ParameterHandle parameterHandle,
      Accessor accessor,
      OOencoder codec) {

    Parameter parameter = this.handle2parameter.get(parameterHandle);
    if (parameter == null) {
      parameter = new Parameter(parameterName, parameterHandle, accessor, codec);
      this.handle2parameter.put(parameterHandle, parameter);
    }
    
    return parameter;
  }

  Parameter getParameterByHandle(ParameterHandle parameterHandle) {
    return handle2parameter.get(parameterHandle);
  }
}
