package nl.tno.oorti.impl.serializer;

import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.ParameterHandleValueMap;

/**
 * @author bergtwvd
 */
public class SerializedInteractionData {

  private InteractionClassHandle classHandle;
  private ParameterHandleValueMap parameterValueMap;

  public SerializedInteractionData() {}

  public SerializedInteractionData(
      InteractionClassHandle classHandle, ParameterHandleValueMap theParameters) {
    this.classHandle = classHandle;
    this.parameterValueMap = theParameters;
  }

  public void set(InteractionClassHandle classHandle, ParameterHandleValueMap parameterValueMap) {
    this.classHandle = classHandle;
    this.parameterValueMap = parameterValueMap;
  }

  public InteractionClassHandle getClassHandle() {
    return classHandle;
  }

  public ParameterHandleValueMap getParameterValueMap() {
    return parameterValueMap;
  }
}
