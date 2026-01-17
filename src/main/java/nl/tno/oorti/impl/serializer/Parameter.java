package nl.tno.oorti.impl.serializer;

import nl.tno.oorti.accessor.Accessor;
import hla.rti1516e.ParameterHandle;
import nl.tno.oorti.OOparameter;
import nl.tno.oorti.ooencoder.OOencoder;

/**
 * @author bergtwvd
 */
public class Parameter implements OOparameter {

  private final InteractionClass interactionClass;
  private final String name;
  private final ParameterHandle parameterHandle;
  private final Accessor accessor;
  private final OOencoder encoder;

  Parameter(
      InteractionClass interactionClass,
      String name,
      ParameterHandle parameterHandle,
      Accessor accessor,
      OOencoder encoder) {
    this.interactionClass = interactionClass;
    this.name = name;
    this.parameterHandle = parameterHandle;
    this.accessor = accessor;
    this.encoder = encoder;
  }

  @Override
  public String getName() {
    return this.name;
  }
  
  public InteractionClass getInteractionClass() {
    return interactionClass;
  }

  public ParameterHandle getParameterHandle() {
    return parameterHandle;
  }

  public Accessor getAccessor() {
    return accessor;
  }

  public OOencoder getEncoder() {
    return encoder;
  }
}
