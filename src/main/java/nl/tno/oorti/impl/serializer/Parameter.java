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
  private final Object cookie;
  private final OOencoder dataElementCodec;

  Parameter(
      InteractionClass interactionClass,
      String name,
      ParameterHandle parameterHandle,
      Object cookie,
      Accessor accessor,
      OOencoder dataElementCodec) {
    this.interactionClass = interactionClass;
    this.name = name;
    this.parameterHandle = parameterHandle;
    this.cookie = cookie;
    this.accessor = accessor;
    this.dataElementCodec = dataElementCodec;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public Object getCookie() {
    return this.cookie;
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

  public OOencoder getDataElementCodec() {
    return dataElementCodec;
  }
}
