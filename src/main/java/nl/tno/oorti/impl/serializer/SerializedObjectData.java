package nl.tno.oorti.impl.serializer;

import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.ObjectInstanceHandle;

/**
 * @author bergtwvd
 */
public class SerializedObjectData {

  private ObjectInstanceHandle instanceHandle;
  private AttributeHandleValueMap attributeValueMap;

  public SerializedObjectData() {}

  public SerializedObjectData(
      ObjectInstanceHandle instanceHandle, AttributeHandleValueMap attributeValueMap) {
    this.instanceHandle = instanceHandle;
    this.attributeValueMap = attributeValueMap;
  }

  public void set(ObjectInstanceHandle instanceHandle, AttributeHandleValueMap attributeValueMap) {
    this.instanceHandle = instanceHandle;
    this.attributeValueMap = attributeValueMap;
  }

  public ObjectInstanceHandle getInstanceHandle() {
    return instanceHandle;
  }

  public AttributeHandleValueMap getAttributeValueMap() {
    return attributeValueMap;
  }
}
