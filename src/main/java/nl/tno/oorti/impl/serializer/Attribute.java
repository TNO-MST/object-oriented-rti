package nl.tno.oorti.impl.serializer;

import nl.tno.oorti.accessor.Accessor;
import hla.rti1516e.AttributeHandle;
import nl.tno.oorti.OOattribute;
import nl.tno.oorti.ooencoder.OOencoder;

/**
 * @author bergtwvd
 */
public class Attribute implements OOattribute {

  private final ObjectClass objectClass;
  private final String name;
  private final AttributeHandle attributeHandle;
  private final Accessor accessor;
  private final Object cookie;
  private final OOencoder dataElementCodec;

  Attribute(
      ObjectClass oc,
      String name,
      AttributeHandle attributeHandle,
      Object cookie,
      Accessor accessor,
      OOencoder dataElementCodec) {
    this.objectClass = oc;
    this.name = name;
    this.attributeHandle = attributeHandle;
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

  public ObjectClass getObjectClass() {
    return objectClass;
  }

  public AttributeHandle getAttributeHandle() {
    return attributeHandle;
  }

  public Accessor getAccessor() {
    return accessor;
  }

  public OOencoder getDataElementCodec() {
    return dataElementCodec;
  }
}
