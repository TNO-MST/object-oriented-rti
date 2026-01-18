package nl.tno.oorti.impl;

import nl.tno.oorti.accessor.Accessor;
import hla.rti1516e.AttributeHandle;
import nl.tno.oorti.OOattribute;
import nl.tno.oorti.ooencoder.OOencoder;

/**
 * @author bergtwvd
 */
public class Attribute implements OOattribute {

  // static properties
  private final ObjectClass objectClass;
  private final String name;
  private final AttributeHandle attributeHandle;
  private final Accessor accessor;
  private final OOencoder encoder;

  // dynamic properties
  private volatile Object cookie = null;

  Attribute(
      ObjectClass oc,
      String name,
      AttributeHandle attributeHandle,
      Accessor accessor,
      OOencoder encoder) {
    this.objectClass = oc;
    this.name = name;
    this.attributeHandle = attributeHandle;
    this.accessor = accessor;
    this.encoder = encoder;
  }

  @Override
  public String getName() {
    return this.name;
  }

  public ObjectClass getObjectClass() {
    return objectClass;
  }

  public AttributeHandle getAttributeHandle() {
    return attributeHandle;
  }

  public Accessor getAccessor() {
    return accessor;
  }

  public OOencoder getEncoder() {
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
