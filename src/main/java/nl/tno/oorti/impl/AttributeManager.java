package nl.tno.oorti.impl;

import hla.rti1516e.AttributeHandle;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import nl.tno.oorti.accessor.Accessor;
import nl.tno.oorti.ooencoder.OOencoder;

/**
 * @author bergtwvd
 */
class AttributeManager {

  // mutable properties
  private final Map<AttributeHandle, Attribute> handle2attribute = new ConcurrentHashMap<>();

  AttributeManager() {}

  Attribute create(
      String attributeName, AttributeHandle attributeHandle, Accessor accessor, OOencoder codec) {

    Attribute attribute = this.handle2attribute.get(attributeHandle);
    if (attribute == null) {
      attribute = new Attribute(attributeName, attributeHandle, accessor, codec);
      this.handle2attribute.put(attributeHandle, attribute);
    }

    return attribute;
  }

  Attribute getAttributeByHandle(AttributeHandle attributeHandle) {
    return this.handle2attribute.get(attributeHandle);
  }
}
