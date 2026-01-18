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
import java.util.Collection;
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
 * Thread-safe class to manage ObjectClasses. The collection is accessed by both the federate and
 * RTI ambassador threads.
 *
 * @author bergtwvd
 */
public class ObjectClassManager {

  // static properties
  private final RTIambassador rtiamb;
  private final AccessorFactory accessorFactory;
  private final OOencoderFactory encoderFactory;
  private final ObjectModelType[] modules;
  private final AttributeHandleValueMapFactory ahvmFactory;
  private final AttributeHandleSetFactory ahsFactory;

  // dynamic properties
  private final Map<Class, ObjectClass> clazz2class = new ConcurrentHashMap<>();
  private final Map<ObjectClassHandle, ObjectClass> handle2class = new ConcurrentHashMap<>();

  public ObjectClassManager(
      RTIambassador rtiamb,
      AccessorFactory accessorFactory,
      OOencoderFactory encoderFactory,
      ObjectModelType[] modules)
      throws FederateNotExecutionMember, NotConnected {

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
      String fqClassName = Helpers.getFullyQualifiedObjectClassName(clazz);
      ObjectClassHandle classHandle = rtiamb.getObjectClassHandle(fqClassName);
      oc = new ObjectClass(clazz, fqClassName, classHandle, this.ahvmFactory, this.ahsFactory);

      this.createAttributeSet(oc);

      clazz2class.put(oc.getClazz(), oc);
      handle2class.put(oc.getClassHandle(), oc);

      return oc;
    } catch (NameNotFound ex) {
      throw new ObjectClassNotDefined(ex.getMessage(), ex);
    }
  }

  /** This method returns the OMT attribute, given its name, or null when none found. */
  private nl.tno.omt.Attribute getOmtAttributeByName(
      Set<nl.tno.omt.Attribute> attributes, String name) {
    for (nl.tno.omt.Attribute attribute : attributes) {
      if (attribute.getName().getValue().equals(name)) {
        return attribute;
      }
    }
    return null;
  }

  private void createAttributeSet(ObjectClass oc)
      throws ObjectClassNotDefined,
          RTIinternalError,
          FederateNotExecutionMember,
          NotConnected,
          AttributeNotDefined {

    // get the attributes as defined in the FOM
    Set<nl.tno.omt.Attribute> omtAttributeSet =
        OmtFunctions.getObjectClassAttributes(modules, oc.getName());
    if (omtAttributeSet == null) {
      // something went wrong
      throw new ObjectClassNotDefined("Cannot get attributes of Class " + oc.getName());
    }

    // create an attribute for each Java class field
    Collection<Field> fields = ClassUtils.getFields(oc.getClazz());

    for (Field field : fields) {
      try {
        String fieldName = field.getName();
        String attributeName = OmtJavaMapping.toOmtName(fieldName);

        // check if the name exists in the FOM
        nl.tno.omt.Attribute omtAttribute =
            this.getOmtAttributeByName(omtAttributeSet, OmtJavaMapping.toOmtName(fieldName));
        if (omtAttribute == null) {
          throw new AttributeNotDefined(
              "Java Class attribute " + fieldName + " not defined in FOM");
        }

        // create accessor for the Java Class field
        Accessor accessor = accessorFactory.createAccessor(field);

        // create codec for the Java Class field
        OOencoder codec =
            encoderFactory.createOOencoder(
                field.getGenericType(), omtAttribute.getDataType().getValue());

        AttributeHandle attributeHandle =
            rtiamb.getAttributeHandle(oc.getClassHandle(), attributeName);

        Attribute attribute = new Attribute(oc, attributeName, attributeHandle, accessor, codec);

        oc.addAttribute(attribute);
      } catch (OOcodecException | ReflectiveOperationException ex) {
        throw new RTIinternalError(ex.getMessage(), ex);
      } catch (InvalidObjectClassHandle ex) {
        throw new ObjectClassNotDefined(ex.getMessage(), ex);
      } catch (NameNotFound ex) {
        throw new AttributeNotDefined(ex.getMessage(), ex);
      }
    }
  }

  public ObjectClass getClassByClazz(Class clazz) {
    return clazz2class.get(clazz);
  }

  public ObjectClass getClassByHandle(ObjectClassHandle handle) {
    return handle2class.get(handle);
  }

  public ObjectClass getObjectClassIfExists(Class clazz) throws ObjectClassNotDefined {
    ObjectClass oc = clazz2class.get(clazz);
    if (oc == null) {
      throw new ObjectClassNotDefined("Unknown class " + clazz.getSimpleName());
    } else {
      return oc;
    }
  }
}
