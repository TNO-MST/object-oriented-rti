package nl.tno.oorti.impl.serializer;

import java.util.Set;
import nl.tno.oorti.OOattribute;

/**
 * @author bergtwvd
 */
public class DeserializedObjectData {

  private Object theObject;
  private Set<OOattribute> attributeSet;

  public DeserializedObjectData() {}

  public DeserializedObjectData(Object theObject, Set<OOattribute> attributeSet) {
    this.theObject = theObject;
    this.attributeSet = attributeSet;
  }

  public void set(Object theObject, Set<OOattribute> attributeSet) {
    this.theObject = theObject;
    this.attributeSet = attributeSet;
  }

  public Object getTheObject() {
    return theObject;
  }

  public Set<OOattribute> getAttributeSet() {
    return attributeSet;
  }
}
