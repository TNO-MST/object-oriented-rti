package nl.tno.oorti.impl;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.exceptions.RTIinternalError;
import java.util.HashSet;
import java.util.Set;
import nl.tno.oorti.OOattribute;
import nl.tno.oorti.OOproperties;
import nl.tno.oorti.exceptions.ObjectDecodingError;
import nl.tno.oorti.exceptions.ObjectEncodingError;
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

  public AttributeHandleValueMap serialize() throws RTIinternalError, ObjectEncodingError {
    return serialize(this.theObject, this.objectClass.getPublications());
  }

  public AttributeHandleValueMap serialize(Object referencedObject)
      throws RTIinternalError, ObjectEncodingError {
    return serialize(referencedObject, this.objectClass.getPublications());
  }

  public AttributeHandleValueMap serialize(Set<OOattribute> attributeSet)
      throws RTIinternalError, ObjectEncodingError {
    return this.serialize(this.theObject, attributeSet);
  }

  public AttributeHandleValueMap serialize(Object referencedObject, Set<OOattribute> attributeSet)
      throws RTIinternalError, ObjectEncodingError {

    AttributeHandleValueMap ahvm =
        this.objectClass.getAttributeHandleValueMapFactory().create(attributeSet.size());

    try {
      for (OOattribute ooAttribute : attributeSet) {
        Attribute attribute = (Attribute) ooAttribute;

        // get the attribute value
        Object value = attribute.getValue(referencedObject);
        if (value == null) {
          // do not serialize null value; skip
          continue;
        }

        // encode the attribute value
        try {
          byte[] bytes = attribute.getEncoder().encode(value);
          ahvm.put(attribute.getAttributeHandle(), bytes);
        } catch (OOcodecException ex) {
          throw new ObjectEncodingError(ex.getMessage(), ex, referencedObject, attribute);
        }
      }

      return ahvm;
    } catch (ReflectiveOperationException ex) {
      throw new RTIinternalError(ex.getMessage(), ex);
    }
  }

  public Set<OOattribute> deserialize(AttributeHandleValueMap attributeValueMap)
      throws RTIinternalError, ObjectDecodingError {
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
            (this.properties.isUseInPlaceCopy()) ? attribute.getValue(theObject) : null;

        // decode bytes to an attribute value
        try {
          value = attribute.getEncoder().decode(bytes, value, theObject);
        } catch (OOcodecException ex) {
          throw new ObjectDecodingError(
              ex.getMessage(), ex, theObject, attribute, HelperFunctions.bytesToHex(bytes));
        }

        // set the new attribute value
        attribute.setValue(this.theObject, value);

        // add the attribute to the set of deserialized attributes
        attributeSet.add(attribute);
      }

      return attributeSet;
    } catch (ReflectiveOperationException ex) {
      throw new RTIinternalError(ex.getMessage(), ex);
    }
  }
}
