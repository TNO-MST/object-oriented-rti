package nl.tno.oorti.impl;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.exceptions.RTIinternalError;
import jakarta.json.bind.JsonbBuilder;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.tno.oorti.OOattribute;
import nl.tno.oorti.OOproperties;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * @author bergtwvd
 */
public class ObjectInstance {

  // static properties
  private final OOproperties properties;
  private final ObjectClass objectClass;
  private final ObjectInstanceHandle instanceHandle;
  private final Object theObject;
  private final String theName;

  public ObjectInstance(
      ObjectInstanceManager oim,
      ObjectClass objectClass,
      ObjectInstanceHandle instanceHandle,
      Object theObject,
      String theName) {
    this.properties = oim.getProperties();
    this.objectClass = objectClass;
    this.instanceHandle = instanceHandle;
    this.theObject = theObject;
    this.theName = theName;
  }

  public ObjectClass getObjectClass() {
    return this.objectClass;
  }

  public ObjectInstanceHandle getInstanceHandle() {
    return this.instanceHandle;
  }

  public Object getObject() {
    return this.theObject;
  }

  public String getName() {
    return this.theName;
  }

  public AttributeHandleValueMap serialize() throws RTIinternalError {
    return serialize(this.theObject, this.objectClass.getPublications());
  }

  public AttributeHandleValueMap serialize(Object referencedObject) throws RTIinternalError {
    return serialize(referencedObject, this.objectClass.getPublications());
  }

  public AttributeHandleValueMap serialize(Set<OOattribute> attributeSet) throws RTIinternalError {
    return this.serialize(this.theObject, attributeSet);
  }

  public AttributeHandleValueMap serialize(Object referencedObject, Set<OOattribute> attributeSet)
      throws RTIinternalError {

    AttributeHandleValueMap ahvm =
        this.objectClass.getAttributeHandleValueMapFactory().create(attributeSet.size());

    try {
      for (OOattribute ooAttribute : attributeSet) {
        Attribute attribute = (Attribute) ooAttribute;

        // get the attribute value
        Object value = attribute.getAccessor().get(referencedObject);
        if (value == null) {
          // do not serialize null value; skip
          continue;
        }

        // encode the attribute value
        try {
          byte[] bytes = attribute.getEncoder().encode(value);
          ahvm.put(attribute.getAttributeHandle(), bytes);
        } catch (OOcodecException ex) {
          Logger.getLogger(ObjectInstance.class.getName())
              .log(
                  Level.WARNING,
                  "Error encoding class={0}, instanceHandle={1}, attribute={2}, codec={3}, value={4}",
                  new Object[] {
                    this.objectClass.getName(),
                    this.instanceHandle.toString(),
                    attribute.getName(),
                    attribute.getEncoder().toString(),
                    JsonbBuilder.create().toJson(value)
                  });

          throw new RTIinternalError(ex.getMessage(), ex);
        }
      }

      return ahvm;
    } catch (ReflectiveOperationException ex) {
      throw new RTIinternalError(ex.getMessage(), ex);
    }
  }

  public Set<OOattribute> deserialize(AttributeHandleValueMap attributeValueMap)
      throws RTIinternalError {
    try {
      Set<OOattribute> attributeSet = new HashSet<>();

      for (AttributeHandle attributeHandle : attributeValueMap.keySet()) {
        // the bytes to deserialize
        byte[] bytes = attributeValueMap.get(attributeHandle);

        // get the attribute
        Attribute attribute = this.objectClass.getAttributeByHandle(attributeHandle);
        if (attribute == null) {
          // attribute not in Java Class, so skip
          continue;
        }

        // use current value on in place copy, otherwise create new value
        Object value =
            (this.properties.isUseInPlaceCopy()) ? attribute.getAccessor().get(theObject) : null;

        // decode bytes to an attribute value
        try {
          value = attribute.getEncoder().decode(bytes, value, theObject);
        } catch (OOcodecException ex) {
          Logger.getLogger(ObjectInstance.class.getName())
              .log(
                  Level.WARNING,
                  "Error decoding class={0}, instanceHandle={1}, attribute={2}, codec={3}, len={4}, bytes={5}",
                  new Object[] {
                    this.objectClass.getName(),
                    this.instanceHandle.toString(),
                    attribute.getName(),
                    attribute.getEncoder().toString(),
                    bytes.length,
                    HelperFunctions.bytesToHex(bytes)
                  });
          throw new RTIinternalError(ex.getMessage(), ex);
        }

        // set the new attribute value
        attribute.getAccessor().set(this.theObject, value);

        // add the attrbute to the set of deserialized attributes
        attributeSet.add(attribute);
      }

      return attributeSet;
    } catch (ReflectiveOperationException ex) {
      throw new RTIinternalError(ex.getMessage(), ex);
    }
  }
}
