package nl.tno.oorti.impl;

import nl.tno.oorti.accessor.Accessor;
import hla.rti1516e.ParameterHandle;
import nl.tno.oorti.OOparameter;
import nl.tno.oorti.ooencoder.OOencoder;

/**
 * @author bergtwvd
 */
public class Parameter implements OOparameter {

  // static properties
  private final InteractionClass interactionClass;
  private final String name;
  private final ParameterHandle parameterHandle;
  private final Accessor accessor;
  private final OOencoder encoder;
  
  // dynamic properties
  private volatile Object cookie = null;

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
  
  InteractionClass getInteractionClass() {
    return interactionClass;
  }

  ParameterHandle getParameterHandle() {
    return parameterHandle;
  }

  Accessor getAccessor() {
    return accessor;
  }

  OOencoder getEncoder() {
    return encoder;
  }

  @Override
  public Object getCookie() {
    return cookie;
  }

  void setCookie(Object cookie) {
    this.cookie = cookie;
  }

}
