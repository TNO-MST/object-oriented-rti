package nl.tno.oorti.impl;

import nl.tno.oorti.accessor.Accessor;
import hla.rti1516e.ParameterHandle;
import nl.tno.oorti.OOparameter;
import nl.tno.oorti.ooencoder.OOencoder;

/**
 * @author bergtwvd
 */
public class Parameter implements OOparameter {

  // immutable properties
  private final String name;
  private final ParameterHandle parameterHandle;
  private final Accessor accessor;
  private final OOencoder encoder;
  
  // mutable properties
  private volatile Object cookie = null;

  Parameter(
      String name,
      ParameterHandle parameterHandle,
      Accessor accessor,
      OOencoder encoder) {
    this.name = name;
    this.parameterHandle = parameterHandle;
    this.accessor = accessor;
    this.encoder = encoder;
  }

  @Override
  public String getName() {
    return this.name;
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

  @Override
  public void setCookie(Object cookie) {
    this.cookie = cookie;
  }

}
