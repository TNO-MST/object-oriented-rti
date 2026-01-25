package nl.tno.oorti.impl;

import nl.tno.oorti.accessor.Accessor;
import hla.rti1516e.AttributeHandle;
import nl.tno.oorti.OOattribute;
import nl.tno.oorti.ooencoder.OOencoder;

/**
 * @author bergtwvd
 */
public class Attribute implements OOattribute {

  // immutable properties
  private final String name;
  private final AttributeHandle attributeHandle;
  private final Accessor accessor;
  private final OOencoder encoder;

  // mutable properties
  private volatile Object cookie = null;

  Attribute(
      String name,
      AttributeHandle attributeHandle,
      Accessor accessor,
      OOencoder encoder) {
    this.name = name;
    this.attributeHandle = attributeHandle;
    this.accessor = accessor;
    this.encoder = encoder;
  }

  @Override
  public String getName() {
    return this.name;
  }

  public AttributeHandle getAttributeHandle() {
    return this.attributeHandle;
  }

  public Accessor getAccessor() {
    return this.accessor;
  }

  public OOencoder getEncoder() {
    return this.encoder;
  }
  
  @Override
  public Object getCookie() {
    return this.cookie;
  }

  @Override
  public void setCookie(Object cookie) {
    this.cookie = cookie;
  }
}
