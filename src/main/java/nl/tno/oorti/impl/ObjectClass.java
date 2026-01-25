package nl.tno.oorti.impl;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleSetFactory;
import hla.rti1516e.AttributeHandleValueMapFactory;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.exceptions.AttributeNotDefined;
import java.util.Arrays;
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
      this.name2attribute.put(attribute.getName(), attribute);
      this.handle2attribute.put(attribute.getAttributeHandle(), attribute);
    }
  }

  public Class getClazz() {
    return this.clazz;
  }

  public String getName() {
    return this.name;
  }

  public ObjectClassHandle getClassHandle() {
    return this.classHandle;
  }

  public Set<Attribute> getAttributes() {
    return this.attributes;
  }

  public AttributeHandleValueMapFactory getAttributeHandleValueMapFactory() {
    return this.ahvmFactory;
  }

  public Attribute getAttributeByName(String name) {
    return this.name2attribute.get(name);
  }

  public Attribute getAttributeIfExists(String name) throws AttributeNotDefined {
    Attribute attribute = this.name2attribute.get(name);
    if (attribute != null) return attribute;
    else throw new AttributeNotDefined(name);
  }

  public Attribute getAttributeByHandle(AttributeHandle handle) {
    return this.handle2attribute.get(handle);
  }

  public Attribute getAttributeIfExists(AttributeHandle handle) throws AttributeNotDefined {
    Attribute attribute = this.handle2attribute.get(handle);
    if (attribute != null) return attribute;
    else throw new AttributeNotDefined(handle.toString());
  }

  public Set<OOattribute> getSubscriptions() {
    return this.subAttributeSet;
  }

  public Set<OOattribute> getPublications() {
    return this.pubAttributeSet;
  }

  public void addPublications() {
    this.pubAttributeSet.addAll(this.attributes);
  }

  public void addPublications(Set<OOattribute> attributes) {
    this.pubAttributeSet.addAll(attributes);
  }

  public void addPublications(OOattribute ... attributes) {
    pubAttributeSet.addAll(Arrays.asList(attributes));
  }

  public void addSubscriptions() {
    this.subAttributeSet.addAll(this.attributes);
  }

  public void addSubscriptions(Set<OOattribute> attributes) {
    this.subAttributeSet.addAll(attributes);
  }

 public void addSubscriptions(OOattribute ... attributes) {
    this.subAttributeSet.addAll(Arrays.asList(attributes));
  }

 public void removePublications() {
    this.pubAttributeSet.clear();
  }

  public void removePublications(Set<OOattribute> attributes) {
    pubAttributeSet.removeAll(attributes);
  }

  public void removePublications(OOattribute ... attributes) {
    pubAttributeSet.removeAll(Arrays.asList(attributes));
  }

  public void removeSubscriptions() {
    this.subAttributeSet.clear();
  }

  public void removeSubscriptions(Set<OOattribute> attributes) {
    subAttributeSet.removeAll(attributes);
  }

  public void removeSubscriptions(OOattribute ... attributes) {
    subAttributeSet.removeAll(Arrays.asList(attributes));
  }

  public AttributeHandleSet createAttributeHandleSet() {
    AttributeHandleSet ahs = this.ahsFactory.create();
    for (Attribute attribute : this.attributes) {
      ahs.add(attribute.getAttributeHandle());
    }
    return ahs;
  }

  public AttributeHandleSet createAttributeHandleSet(Set<OOattribute> attributes) {
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
      attributeSet.add(this.getAttributeIfExists(attributeHandle));
    }
    return attributeSet;
  }
}
