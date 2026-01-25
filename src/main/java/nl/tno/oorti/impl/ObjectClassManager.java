package nl.tno.oorti.impl;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSetFactory;
import hla.rti1516e.AttributeHandleValueMapFactory;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.RTIinternalError;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.omt.helpers.OmtJavaMapping;
import nl.tno.oorti.accessor.Accessor;
import nl.tno.oorti.accessor.AccessorFactory;
import nl.tno.oorti.accessor.ClassUtils;
import nl.tno.oorti.ooencoder.OOencoder;
import nl.tno.oorti.ooencoder.OOencoderFactory;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * Thread-safe class to manage ObjectClasses.
 *
 * @author bergtwvd
 */
public class ObjectClassManager {

  // immutable properties
  private final AttributeManager atm;
  private final RTIambassador rtiamb;
  private final AccessorFactory accessorFactory;
  private final OOencoderFactory encoderFactory;
  private final ObjectModelType[] modules;
  private final AttributeHandleValueMapFactory ahvmFactory;
  private final AttributeHandleSetFactory ahsFactory;

  // mutable properties
  private final Map<Class, ObjectClass> clazz2class = new ConcurrentHashMap<>();
  private final Map<ObjectClassHandle, ObjectClass> handle2class = new ConcurrentHashMap<>();

  public ObjectClassManager(
      RTIambassador rtiamb,
      AccessorFactory accessorFactory,
      OOencoderFactory encoderFactory,
      ObjectModelType[] modules)
      throws FederateNotExecutionMember, NotConnected {

    this.atm = new AttributeManager();
    this.rtiamb = rtiamb;
    this.accessorFactory = accessorFactory;
    this.encoderFactory = encoderFactory;
    this.modules = modules;
    this.ahvmFactory = rtiamb.getAttributeHandleValueMapFactory();
    this.ahsFactory = rtiamb.getAttributeHandleSetFactory();
  }

  public ObjectClass create(Class clazz)
      throws FederateNotExecutionMember,
          NotConnected,
          RTIinternalError,
          ObjectClassNotDefined,
          AttributeNotDefined {

    ObjectClass oc = this.clazz2class.get(clazz);
    if (oc != null) return oc;

    try {
      String classsName = HelperFunctions.getFullyQualifiedObjectClassName(clazz);
      ObjectClassHandle classHandle = this.rtiamb.getObjectClassHandle(classsName);
      Set<Attribute> attributeSet = this.createAttributeSet(clazz, classsName, classHandle);

      oc =
          new ObjectClass(
              clazz, classsName, classHandle, attributeSet, this.ahvmFactory, this.ahsFactory);

      this.clazz2class.put(oc.getClazz(), oc);
      this.handle2class.put(oc.getClassHandle(), oc);

      return oc;
    } catch (NameNotFound ex) {
      throw new ObjectClassNotDefined(ex.getMessage(), ex);
    }
  }

  /** This method returns the OMT attribute, given its name, or null when none found. */
  private nl.tno.omt.Attribute getOmtAttributeByName(
      Set<nl.tno.omt.Attribute> attributes, String name) throws AttributeNotDefined {
    for (nl.tno.omt.Attribute attribute : attributes) {
      if (attribute.getName().getValue().equals(name)) {
        return attribute;
      }
    }
    return null;
  }

  private Set<Attribute> createAttributeSet(
      Class clazz, String className, ObjectClassHandle classHandle)
      throws ObjectClassNotDefined,
          RTIinternalError,
          FederateNotExecutionMember,
          NotConnected,
          AttributeNotDefined {

    // get the attributes as defined in the FOM
    Set<nl.tno.omt.Attribute> omtAttributeSet =
        OmtFunctions.getObjectClassAttributes(this.modules, className);
    if (omtAttributeSet == null) {
      // something went wrong
      throw new ObjectClassNotDefined("Cannot get attributes of Class " + className);
    }

    Set<Attribute> attributeSet = new HashSet<>();

    // create an attribute for each Java class property
    for (Field field : ClassUtils.getFields(clazz)) {
      try {
        String attributeName = OmtJavaMapping.toOmtName(field.getName());

        nl.tno.omt.Attribute omtAttribute =
            this.getOmtAttributeByName(omtAttributeSet, attributeName);
        if (omtAttribute == null) {
          // Java property is not in the FOM, skip
          continue;
        }

        AttributeHandle attributeHandle =
            this.rtiamb.getAttributeHandle(classHandle, attributeName);

        Attribute attribute = this.atm.getAttributeByHandle(attributeHandle);
        if (attribute == null) {
          // create accessor for the Java Class field
          Accessor accessor = this.accessorFactory.createAccessor(field);

          // create codec for the Java Class field
          OOencoder codec =
              this.encoderFactory.createOOencoder(
                  field.getGenericType(), omtAttribute.getDataType().getValue());

          attribute = this.atm.create(attributeName, attributeHandle, accessor, codec);
        }

        attributeSet.add(attribute);
      } catch (OOcodecException | ReflectiveOperationException ex) {
        throw new RTIinternalError(ex.getMessage(), ex);
      } catch (InvalidObjectClassHandle ex) {
        throw new ObjectClassNotDefined(ex.getMessage(), ex);
      } catch (NameNotFound ex) {
        throw new AttributeNotDefined(ex.getMessage(), ex);
      }
    }

    return attributeSet;
  }

  public ObjectClass getObjectClassByHandle(ObjectClassHandle handle) {
    return this.handle2class.get(handle);
  }

  public ObjectClass getObjectClassIfExists(Class clazz) throws ObjectClassNotDefined {
    ObjectClass oc = this.clazz2class.get(clazz);
    if (oc == null) {
      throw new ObjectClassNotDefined("Unknown class " + clazz.getSimpleName());
    } else {
      return oc;
    }
  }
}
