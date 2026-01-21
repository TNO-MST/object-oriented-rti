package nl.tno.oorti.impl;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleSetFactory;
import hla.rti1516e.AttributeHandleValueMapFactory;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.exceptions.AttributeNotDefined;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import nl.tno.oorti.OOattribute;

/**
 * @author bergtwvd
 */
public class ObjectClass {

  // immutable properties
  private final Class clazz;
  private final String name;
  private final ObjectClassHandle classHandle;
  private final Set<Attribute> attributes;
  private final AttributeHandleValueMapFactory ahvmFactory;
  private final AttributeHandleSetFactory ahsFactory;
  private final Map<String, Attribute> name2attribute = new HashMap<>();
  private final Map<AttributeHandle, Attribute> handle2attribute = new HashMap<>();

  // mutable properties
  private final Set<OOattribute> pubAttributeSet = ConcurrentHashMap.newKeySet();
  private final Set<OOattribute> subAttributeSet = ConcurrentHashMap.newKeySet();

  public ObjectClass(
      Class clazz,
      String name,
      ObjectClassHandle classHandle,
      Set<Attribute> attributeSet,
      AttributeHandleValueMapFactory ahvmFactory,
      AttributeHandleSetFactory ahsFactory) {
    this.clazz = clazz;
    this.name = name;
    this.classHandle = classHandle;
    this.attributes = attributeSet;
    this.ahvmFactory = ahvmFactory;
    this.ahsFactory = ahsFactory;

    for (Attribute attribute : attributeSet) {
      name2attribute.put(attribute.getName(), attribute);
      handle2attribute.put(attribute.getAttributeHandle(), attribute);
    }
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

  public Set<Attribute> getAttributes() {
    return attributes;
  }

  public AttributeHandleValueMapFactory getAttributeHandleValueMapFactory() {
    return this.ahvmFactory;
  }

  public Attribute getAttributeByName(String name) {
    return this.name2attribute.get(name);
  }

  public Attribute getAttributeByNameIfExists(String name) throws AttributeNotDefined {
    Attribute attribute = name2attribute.get(name);
    if (attribute != null) return attribute;
    else throw new AttributeNotDefined(name);
  }

  public Attribute getAttributeByHandle(AttributeHandle handle) {
    return this.handle2attribute.get(handle);
  }

  public Attribute getAttributeByHandleIfExists(AttributeHandle handle) throws AttributeNotDefined {
    Attribute attribute = handle2attribute.get(handle);
    if (attribute != null) return attribute;
    else throw new AttributeNotDefined(handle.toString());
  }

  public Set<OOattribute> getSubscriptions() {
    return subAttributeSet;
  }

  public Set<OOattribute> getPublications() {
    return pubAttributeSet;
  }

  public void addPublications() {
    for (Attribute attribute : this.attributes) {
      pubAttributeSet.add(attribute);
    }
  }

  public void addPublications(Set<? extends Object> cookies) throws AttributeNotDefined {
    // first check the inputs
    for (Object cookie : cookies) {
      this.getAttributeByNameIfExists(cookie.toString());
    }

    for (Object cookie : cookies) {
      pubAttributeSet.add(this.getAttributeByNameIfExists(cookie.toString()));
    }
  }

  public void addSubscriptions() {
    for (Attribute attribute : this.attributes) {
      subAttributeSet.add(attribute);
    }
  }

  public void addSubscriptions(Set<? extends Object> cookies) throws AttributeNotDefined {
    // first check the inputs
    for (Object cookie : cookies) {
      this.getAttributeByNameIfExists(cookie.toString());
    }

    for (Object cookie : cookies) {
      subAttributeSet.add(this.getAttributeByNameIfExists(cookie.toString()));
    }
  }

  public void removePublications() {
    this.pubAttributeSet.clear();
  }

  public void removePublications(Set<String> theAttributeNames) throws AttributeNotDefined {
    // first check the inputs
    for (String attributeName : theAttributeNames) {
      this.getAttributeByNameIfExists(attributeName);
    }

    for (String attributeName : theAttributeNames) {
      pubAttributeSet.remove(this.getAttributeByNameIfExists(attributeName));
    }
  }

  public void removeSubscriptions() {
    this.subAttributeSet.clear();
  }

  public void removeSubscriptions(Set<String> theAttributeNames) throws AttributeNotDefined {
    // first check the inputs
    for (String attributeName : theAttributeNames) {
      this.getAttributeByNameIfExists(attributeName);
    }

    for (String attributeName : theAttributeNames) {
      subAttributeSet.remove(this.getAttributeByNameIfExists(attributeName));
    }
  }

  public AttributeHandleSet createAttributeHandleSet() {
    AttributeHandleSet ahs = this.ahsFactory.create();
    for (Attribute attribute : this.attributes) {
      ahs.add(attribute.getAttributeHandle());
    }
    return ahs;
  }

  public AttributeHandleSet createAttributeHandleSetFromNames(Set<? extends Object> cookies)
      throws AttributeNotDefined {
    AttributeHandleSet ahs = this.ahsFactory.create();
    for (Object cookie : cookies) {
      ahs.add(this.getAttributeByNameIfExists(cookie.toString()).getAttributeHandle());
    }
    return ahs;
  }

  public AttributeHandleSet createAttributeHandleSetFromAttributes(Set<OOattribute> attributes) {
    AttributeHandleSet ahs = this.ahsFactory.create();
    for (OOattribute attribute : attributes) {
      ahs.add(((Attribute) attribute).getAttributeHandle());
    }
    return ahs;
  }

  public Set<OOattribute> createAttributeSet(AttributeHandleSet handleSet)
      throws AttributeNotDefined {
    Set<OOattribute> attributeSet = new HashSet<>();
    for (AttributeHandle attributeHandle : handleSet) {
      attributeSet.add(this.getAttributeByHandleIfExists(attributeHandle));
    }
    return attributeSet;
  }
}
