package nl.tno.oorti.impl.serializer;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleSetFactory;
import hla.rti1516e.ObjectClassHandle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import nl.tno.oorti.OOattribute;

/**
 * @author bergtwvd
 */
public class ObjectClass {

  private final Class clazz;
  private final String name;
  private final ObjectClassHandle classHandle;
  private final AttributeHandleSetFactory attributeHandleSetFactory;

  // These are filled during ObjectClass initialization, and after that these are static.
  private final Set<OOattribute> attributeSet = new HashSet<>();
  private final Map<AttributeHandle, Attribute> handle2Attribute = new HashMap<>();

  ObjectClass(
      Class clazz,
      String name,
      ObjectClassHandle classHandle,
      AttributeHandleSetFactory attribHandleSetFactory) {
    this.clazz = clazz;
    this.name = name;
    this.classHandle = classHandle;
    this.attributeHandleSetFactory = attribHandleSetFactory;
  }

  void addAttribute(Attribute attribute) {
    attributeSet.add(attribute);
    handle2Attribute.put(attribute.getAttributeHandle(), attribute);
  }

  public AttributeHandleSet createAttributeHandleSet(Set<OOattribute> attributeSet) {
    AttributeHandleSet result = this.attributeHandleSetFactory.create();
    for (OOattribute ooAttribute : attributeSet) {
      result.add(((Attribute) ooAttribute).getAttributeHandle());
    }

    return result;
  }

  public Attribute getAttributeByHandle(AttributeHandle attributeHandle) {
    return this.handle2Attribute.get(attributeHandle);
  }

  public Class getClazz() {
    return clazz;
  }

  public String getName() {
    return name;
  }

  public ObjectClassHandle getClassHandle() {
    return classHandle;
  }

  public Set<OOattribute> getAttributeSet() {
    return attributeSet;
  }
}
