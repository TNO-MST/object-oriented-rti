package nl.tno.oorti.impl.serializer;

import nl.tno.oorti.accessor.Accessor;
import nl.tno.oorti.accessor.AccessorFactory;
import nl.tno.oorti.accessor.ClassUtils;
import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSetFactory;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.AttributeHandleValueMapFactory;
import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.ParameterHandleValueMapFactory;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InteractionClassNotDefined;
import hla.rti1516e.exceptions.InteractionParameterNotDefined;
import hla.rti1516e.exceptions.InvalidInteractionClassHandle;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import hla.rti1516e.exceptions.RTIinternalError;
import jakarta.json.bind.JsonbBuilder;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashSet;
import java.util.Set;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.helpers.OmtJavaMapping;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.omt.helpers.OmtMimConstants;
import nl.tno.oorti.OOattribute;
import nl.tno.oorti.OOobjectFactory;
import nl.tno.oorti.OOparameter;
import nl.tno.oorti.OOproperties;
import nl.tno.oorti.accessor.AccessorFactoryFactory;
import nl.tno.oorti.ooencoder.OOencoderFactory;
import nl.tno.oorti.ooencoder.OOencoder;
import nl.tno.oorti.ooencoder.OOencoderFactoryFactory;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * @author bergtwvd
 */
public class Serializer {

  private static final Logger LOGGER = Logger.getLogger(Serializer.class.getName());

  // log all messagaes at this level
  private final Level LOGLEVEL = Level.FINER;

  // the RTI Ambassador
  final RTIambassador rtiamb;

  // factories
  private final AccessorFactory accessorFactory;
  private final OOencoderFactory encoderFactory;
  private final OOobjectFactory objectFactory;
  private final AttributeHandleValueMapFactory attribHVMFactory;
  private final ParameterHandleValueMapFactory paramHVMFactory;
  private final AttributeHandleSetFactory attribHandleFactory;

  // the FOM modules
  private final ObjectModelType[] modules;

  // collections
  private final ObjectClassCollection pubObjectClasses = new ObjectClassCollection();
  private final ObjectClassCollection subObjectClasses = new ObjectClassCollection();
  private final InteractionClassCollection pubInteractionClasses = new InteractionClassCollection();
  private final InteractionClassCollection subInteractionClasses = new InteractionClassCollection();
  private final ObjectInstanceCollection objectInstances = new ObjectInstanceCollection();

  // settings
  private final OOproperties properties;

  /**
   * Serializer for the encoding and decoding of RTI data.
   *
   * @param rtiamb
   * @param modules: FOM modules to use
   * @param objectFactory: factory for creating Bean instances
   * @param properties: properties
   * @throws hla.rti1516e.exceptions.FederateNotExecutionMember
   * @throws hla.rti1516e.exceptions.NotConnected
   */
  public Serializer(
      RTIambassador rtiamb,
      ObjectModelType[] modules,
      OOobjectFactory objectFactory,
      OOproperties properties)
      throws FederateNotExecutionMember, NotConnected {

    this.rtiamb = rtiamb;
    this.modules = modules;
    this.accessorFactory = AccessorFactoryFactory.getAccessorFactory(properties.getAccessorType());
    this.objectFactory = objectFactory;
    this.properties = properties;
    this.encoderFactory =
        OOencoderFactoryFactory.getOOencoderFactory(
            properties.getEncodingType(), accessorFactory, modules);
    this.attribHVMFactory = rtiamb.getAttributeHandleValueMapFactory();
    this.paramHVMFactory = rtiamb.getParameterHandleValueMapFactory();
    this.attribHandleFactory = rtiamb.getAttributeHandleSetFactory();
  }

  //////////////////////////
  // OBJECT CLASS METHODS //
  //////////////////////////
  public ObjectClass createObjectClass(Class clazz)
      throws RTIinternalError,
          AttributeNotDefined,
          FederateNotExecutionMember,
          NotConnected,
          ObjectClassNotDefined {
    return this.createObjectClass(clazz, null);
  }

  public ObjectClass createObjectClass(Class clazz, Set<? extends Object> cookies)
      throws RTIinternalError,
          AttributeNotDefined,
          FederateNotExecutionMember,
          NotConnected,
          ObjectClassNotDefined {

    // get the FQ name for the Java class
    String fqClassName = this.getFullyQualifiedObjectClassName(clazz);

    ObjectClassHandle classHandle;
    try {
      classHandle = rtiamb.getObjectClassHandle(fqClassName);
    } catch (NameNotFound ex) {
      throw new ObjectClassNotDefined(ex.getMessage(), ex);
    }

    // create Object Class
    ObjectClass objectClass =
        new ObjectClass(clazz, fqClassName, classHandle, this.attribHandleFactory);

    LOGGER.log(LOGLEVEL, "Add Class={0}", new Object[] {objectClass.getName()});

    // get the attributes as defined in the FOM
    Set<nl.tno.omt.Attribute> omtAttributeSet =
        OmtFunctions.getObjectClassAttributes(modules, fqClassName);
    if (omtAttributeSet == null) {
      // something went wrong
      throw new ObjectClassNotDefined("Cannot get attributes of Class " + fqClassName);
    }

    if (cookies != null) {
      // add only the designated attributes
      for (Object cookie : cookies) {
        try {
          // use the toString method to obtain the field name
          String fieldName = cookie.toString();
          Field field = ClassUtils.getField(clazz, fieldName);
          if (field == null) {
            throw new AttributeNotDefined(
                "Java Class attribute "
                    + fieldName
                    + " not defined in Class "
                    + clazz.getSimpleName());
          }

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
              this.encoderFactory.createOOencoder(
                  field.getGenericType(), omtAttribute.getDataType().getValue());

          // create and add attribute
          AttributeHandle attributeHandle = rtiamb.getAttributeHandle(classHandle, fieldName);
          Attribute attrib =
              new Attribute(objectClass, field.getName(), attributeHandle, cookie, accessor, codec);
          objectClass.addAttribute(attrib);

          LOGGER.log(LOGLEVEL, "Add Attribute={0}", new Object[] {field.getName()});

        } catch (OOcodecException | ReflectiveOperationException ex) {
          throw new RTIinternalError(ex.getMessage(), ex);
        } catch (InvalidObjectClassHandle ex) {
          throw new ObjectClassNotDefined(ex.getMessage(), ex);
        } catch (NameNotFound ex) {
          throw new AttributeNotDefined(ex.getMessage(), ex);
        }
      }
    } else {
      // create an attribute for each Java class field
      Collection<Field> fields = ClassUtils.getFields(clazz);

      for (Field field : fields) {
        try {
          String fieldName = field.getName();

          // check if the field exists in the FOM
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
              this.encoderFactory.createOOencoder(
                  field.getGenericType(), omtAttribute.getDataType().getValue());

          // create and add attribute
          AttributeHandle attributeHandle = rtiamb.getAttributeHandle(classHandle, fieldName);
          Attribute attribute =
              new Attribute(objectClass, fieldName, attributeHandle, null, accessor, codec);
          objectClass.addAttribute(attribute);

          LOGGER.log(LOGLEVEL, "Add Attribute={0}", new Object[] {field.getName()});

        } catch (OOcodecException | ReflectiveOperationException ex) {
          throw new RTIinternalError(ex.getMessage(), ex);
        } catch (InvalidObjectClassHandle ex) {
          throw new ObjectClassNotDefined(ex.getMessage(), ex);
        } catch (NameNotFound ex) {
          throw new AttributeNotDefined(ex.getMessage(), ex);
        }
      }
    }

    return objectClass;
  }

  public ObjectClass addObjectClass(boolean pub, ObjectClass objectClass) {
    return pub ? pubObjectClasses.add(objectClass) : subObjectClasses.add(objectClass);
  }

  public ObjectClass removeObjectClass(boolean pub, ObjectClass objectClass) {
    return pub ? pubObjectClasses.remove(objectClass) : subObjectClasses.remove(objectClass);
  }

  /////////////////////////////
  // OBJECT INSTANCE METHODS //
  /////////////////////////////
  public ObjectInstance createObjectInstance(
      ObjectClass objectClass,
      ObjectInstanceHandle instanceHandle,
      Object theObject,
      String theObjectName)
      throws ObjectClassNotDefined, RTIinternalError {
    LOGGER.log(
        LOGLEVEL,
        "createObjectInstance Class={0} Handle={1} Object={2} ObjectName={3}",
        new Object[] {objectClass.getName(), instanceHandle, theObject, theObjectName});

    ObjectInstance objectInstance =
        new ObjectInstance(objectClass, instanceHandle, theObject, theObjectName);
    return this.objectInstances.add(objectInstance);
  }

  public ObjectInstance removeObjectInstance(ObjectInstance objectInstance) {
    LOGGER.log(
        LOGLEVEL,
        "removeLocalObjectInstance Class={0} Handle={1}",
        new Object[] {
          objectInstance.getObjectClass().getName(), objectInstance.getInstanceHandle()
        });

    return this.objectInstances.removeObjectInstance(objectInstance);
  }

  //////////////////////////////////
  // OBJECT SERIALIZATION METHODS //
  //////////////////////////////////
  public SerializedObjectData serializeObject(
      String theObjectName, Object theObject, SerializedObjectData result)
      throws ObjectInstanceNotKnown, RTIinternalError {

    ObjectInstance oi = this.getObjectInstanceIfExists(theObjectName);
    return serializeObject(oi, theObject, oi.getObjectClass().getAttributeSet(), result);
  }

  public SerializedObjectData serializeObject(Object theObject, SerializedObjectData result)
      throws RTIinternalError, ObjectInstanceNotKnown {

    ObjectInstance oi = this.getObjectInstanceIfExists(theObject);
    return serializeObject(oi, theObject, oi.getObjectClass().getAttributeSet(), result);
  }

  public SerializedObjectData serializeObject(
      String theObjectName,
      Object theObject,
      Set<OOattribute> attributeSet,
      SerializedObjectData result)
      throws RTIinternalError, AttributeNotDefined, ObjectInstanceNotKnown {

    ObjectInstance oi = this.getObjectInstanceIfExists(theObjectName);

    // check that the Attributes belong to same object class
    for (OOattribute ooAttribute : attributeSet) {
      Attribute attribute = (Attribute) ooAttribute;
      if (attribute.getObjectClass() != oi.getObjectClass()) {
        throw new AttributeNotDefined(
            "Attribute "
                + attribute.getName()
                + " is not a member of class "
                + oi.getObjectClass().getName());
      }
    }

    return serializeObject(oi, theObject, attributeSet, result);
  }

  public SerializedObjectData serializeObject(
      Object theObject, Set<OOattribute> attributeSet, SerializedObjectData result)
      throws RTIinternalError, AttributeNotDefined, ObjectInstanceNotKnown {

    ObjectInstance oi = this.getObjectInstanceIfExists(theObject);

    // check that the Attributes belong to same object class
    for (OOattribute ooAttribute : attributeSet) {
      Attribute attribute = (Attribute) ooAttribute;
      if (attribute.getObjectClass() != oi.getObjectClass()) {
        throw new AttributeNotDefined(
            "Attribute "
                + attribute.getName()
                + " is not a member of class "
                + oi.getObjectClass().getName());
      }
    }

    return serializeObject(oi, theObject, attributeSet, result);
  }

  private SerializedObjectData serializeObject(
      ObjectInstance objectInstance,
      Object theObject,
      Set<OOattribute> attributeSet,
      SerializedObjectData result)
      throws RTIinternalError {
    try {
      // the resulting value map
      AttributeHandleValueMap attributeValues = this.attribHVMFactory.create(attributeSet.size());

      // serialize designated attributes
      for (OOattribute ooAttribute : attributeSet) {
        Attribute attribute = (Attribute) ooAttribute;

        // get the attribute value
        Object value = attribute.getAccessor().get(theObject);
        if (value == null) {
          // do not serialize null value; skip
          continue;
        }

        try {
          // encode the attribute value to bytes
          byte[] bytes = attribute.getDataElementCodec().encode(value);

          // add to results
          attributeValues.put(attribute.getAttributeHandle(), bytes);
        } catch (OOcodecException ex) {
          LOGGER.log(
              Level.WARNING,
              "Error encoding class={0}, instanceHandle={1}, attribute={2}, codec={3}, value={4}",
              new Object[] {
                objectInstance.getObjectClass().getName(),
                objectInstance.getInstanceHandle().toString(),
                attribute.getName(),
                attribute.getDataElementCodec().toString(),
                JsonbBuilder.create().toJson(value)
              });
          throw new RTIinternalError(ex.getMessage(), ex);
        }
      }

      if (result == null) {
        result = new SerializedObjectData(objectInstance.getInstanceHandle(), attributeValues);
      } else {
        result.set(objectInstance.getInstanceHandle(), attributeValues);
      }
      return result;
    } catch (ReflectiveOperationException ex) {
      throw new RTIinternalError(ex.getMessage(), ex);
    }
  }

  public DeserializedObjectData deserializeObject(
      ObjectInstance objectInstance,
      AttributeHandleValueMap attributeValueMap,
      DeserializedObjectData result)
      throws RTIinternalError {
    try {
      // get the Java object in which to reflect the values
      Object theObject = objectInstance.getObject();

      ObjectClass objectClass = objectInstance.getObjectClass();

      Set<OOattribute> attributeSet = new HashSet<>();

      for (AttributeHandle attributeHandle : attributeValueMap.keySet()) {
        // the bytes to deserialize
        byte[] bytes = attributeValueMap.get(attributeHandle);

        // get the attribute
        Attribute attribute = objectClass.getAttributeByHandle(attributeHandle);
        if (attribute == null) {
          // attribute not in Java Class, so skip
          continue;
        }

        // use current value on in place copy, otherwise create new value
        Object value =
            (this.properties.isUseInPlaceCopy()) ? attribute.getAccessor().get(theObject) : null;

        // decode bytes to an attribute value
        try {
          value = attribute.getDataElementCodec().decode(bytes, value, theObject);
        } catch (OOcodecException ex) {
          LOGGER.log(
              Level.WARNING,
              "Error decoding class={0}, instanceHandle={1}, attribute={2}, codec={3}, len={4}, bytes={5}",
              new Object[] {
                objectInstance.getObjectClass().getName(),
                objectInstance.getInstanceHandle().toString(),
                attribute.getName(),
                attribute.getDataElementCodec().toString(),
                bytes.length,
                bytesToHex(bytes)
              });
          throw new RTIinternalError(ex.getMessage(), ex);
        }

        // set the new attribute value
        attribute.getAccessor().set(theObject, value);

        // add the attrbute to the set of deserialized attributes
        attributeSet.add(attribute);
      }

      if (result == null) {
        result = new DeserializedObjectData(theObject, attributeSet);
      } else {
        result.set(theObject, attributeSet);
      }
      return result;
    } catch (ReflectiveOperationException ex) {
      throw new RTIinternalError(ex.getMessage(), ex);
    }
  }

  ///////////////////////////////
  // INTERACTION CLASS METHODS //
  ///////////////////////////////
  public InteractionClass createInteractionClass(Class clazz)
      throws FederateNotExecutionMember,
          NotConnected,
          RTIinternalError,
          InteractionParameterNotDefined,
          InteractionClassNotDefined {
    return createInteractionClass(clazz, null);
  }

  public InteractionClass createInteractionClass(Class clazz, Set<? extends Object> cookies)
      throws FederateNotExecutionMember,
          NotConnected,
          RTIinternalError,
          InteractionClassNotDefined,
          InteractionParameterNotDefined {

    // get the FQ name for the Java class
    String fqClassName = this.getFullyQualifiedInteractionClassName(clazz);

    InteractionClassHandle classHandle;
    try {
      classHandle = rtiamb.getInteractionClassHandle(fqClassName);
    } catch (NameNotFound ex) {
      throw new InteractionClassNotDefined(ex.getMessage(), ex);
    }

    // create the Interaction Class
    InteractionClass interactionClass = new InteractionClass(clazz, fqClassName, classHandle);

    // get the parameters as defined in the FOM
    Set<nl.tno.omt.Parameter> parameters =
        OmtFunctions.getInteractionClassParameters(modules, fqClassName);
    if (parameters == null) {
      // something went wrong
      throw new InteractionClassNotDefined("Cannot get parameters of Class " + fqClassName);
    }

    LOGGER.log(LOGLEVEL, "Add Class={0}", new Object[] {fqClassName});

    // create a parameter for each Java class field
    Collection<Field> fields = ClassUtils.getFields(clazz);

    for (Field field : fields) {
      try {
        String fieldName = field.getName();

        // check if the field exists in the FOM
        nl.tno.omt.Parameter omtParameter =
            this.getOmtParameterByName(parameters, OmtJavaMapping.toOmtName(fieldName));
        if (omtParameter == null) {
          throw new InteractionParameterNotDefined(
              "Java Class attribute " + field.getName() + " not defined in FOM");
        }

        // create accessor for the Java Class field
        Accessor accessor = accessorFactory.createAccessor(field);

        // create codec for the Java Class field
        OOencoder codec =
            this.encoderFactory.createOOencoder(
                field.getGenericType(), omtParameter.getDataType().getValue());

        // create parameter and add to class
        ParameterHandle parmHandle = rtiamb.getParameterHandle(classHandle, fieldName);

        // check if there is a cookie for this parameter
        Object cookie = null;
        if (cookies != null) {
          for (Object c : cookies) {
            if (c.toString().equals(fieldName)) {
              cookie = c;
              break;
            }
          }
        }

        Parameter parameter =
            new Parameter(interactionClass, fieldName, parmHandle, cookie, accessor, codec);
        interactionClass.addParameter(parameter);

        LOGGER.log(LOGLEVEL, "Add Parameter={0}", new Object[] {fieldName});
      } catch (OOcodecException | ReflectiveOperationException ex) {
        throw new RTIinternalError(ex.getMessage(), ex);
      } catch (InvalidInteractionClassHandle ex) {
        throw new InteractionClassNotDefined(ex.getMessage(), ex);
      } catch (NameNotFound ex) {
        throw new InteractionParameterNotDefined(ex.getMessage(), ex);
      }
    }

    return interactionClass;
  }

  public InteractionClass addInteractionClass(boolean pub, InteractionClass interactionClass) {
    return pub
        ? this.pubInteractionClasses.add(interactionClass)
        : this.subInteractionClasses.add(interactionClass);
  }

  public InteractionClass removeInteractionClass(boolean pub, InteractionClass interactionClass) {
    return pub
        ? this.pubInteractionClasses.remove(interactionClass)
        : this.subInteractionClasses.remove(interactionClass);
  }

  ///////////////////////////////////////
  // INTERACTION SERIALIZATION METHODS //
  ///////////////////////////////////////
  public SerializedInteractionData serializeInteraction(
      Object theInteraction, SerializedInteractionData result)
      throws RTIinternalError, InteractionClassNotDefined {

    InteractionClass ic =
        this.getInteractionClassIfExists(
            true, this.objectFactory.getInteractionClass(theInteraction));

    return serializeInteraction(ic, theInteraction, ic.getParameterSet(), result);
  }

  public SerializedInteractionData serializeInteraction(
      Object theInteraction, Set<OOparameter> parameterSet, SerializedInteractionData result)
      throws RTIinternalError, InteractionClassNotDefined {

    InteractionClass ic =
        this.getInteractionClassIfExists(
            true, this.objectFactory.getInteractionClass(theInteraction));

    return serializeInteraction(ic, theInteraction, parameterSet, result);
  }

  private SerializedInteractionData serializeInteraction(
      InteractionClass interactionClass,
      Object theInteraction,
      Set<OOparameter> parameterSet,
      SerializedInteractionData result)
      throws RTIinternalError {
    try {
      // the resulting value map
      ParameterHandleValueMap parameterValueMap = this.paramHVMFactory.create(parameterSet.size());

      for (OOparameter ooParameter : parameterSet) {
        Parameter parameter = (Parameter) ooParameter;

        // get the parameter value
        Object value = parameter.getAccessor().get(theInteraction);
        if (value == null) {
          // do not serialize null value; skip
          continue;
        }

        try {
          // encode the parameter value to bytes
          byte[] bytes = parameter.getDataElementCodec().encode(value);

          // add to results
          parameterValueMap.put(parameter.getParameterHandle(), bytes);
        } catch (OOcodecException ex) {
          LOGGER.log(
              Level.WARNING,
              "Error encoding class={0}, parameter={1}, codec={2}, value={3}",
              new Object[] {
                interactionClass.getName(),
                parameter.getName(),
                parameter.getDataElementCodec().toString(),
                JsonbBuilder.create().toJson(value)
              });
          throw new RTIinternalError(ex.getMessage(), ex);
        }
      }

      if (result == null) {
        result =
            new SerializedInteractionData(interactionClass.getClassHandle(), parameterValueMap);
      } else {
        result.set(interactionClass.getClassHandle(), parameterValueMap);
      }

      return result;
    } catch (ReflectiveOperationException ex) {
      throw new RTIinternalError(ex.getMessage(), ex);
    }
  }

  public DeserializedInteractionData deserializeInteraction(
      InteractionClass interactionClass,
      ParameterHandleValueMap parameterValueMap,
      DeserializedInteractionData result)
      throws RTIinternalError {
    try {
      // create a new object to return the data in
      Object theInteraction = this.objectFactory.createInteraction(interactionClass.getClazz());

      Set<OOparameter> parameterSet = new HashSet<>();

      for (ParameterHandle parameterHandle : parameterValueMap.keySet()) {
        // the bytes to deserialize
        byte[] bytes = parameterValueMap.get(parameterHandle);

        // get the parameter
        Parameter parameter = interactionClass.getParameterByHandle(parameterHandle);
        if (parameter == null) {
          // received parameter not in Java Class, so skip
          continue;
        }

        // decode the bytes to a parameter value
        Object value;
        try {
          value = parameter.getDataElementCodec().decode(bytes, null, theInteraction);
        } catch (OOcodecException ex) {
          LOGGER.log(
              Level.WARNING,
              "Error decoding class={0}, parameter={1}, codec={2}, len={3}, bytes={4}",
              new Object[] {
                interactionClass.getName(),
                parameter.getName(),
                parameter.getDataElementCodec().toString(),
                bytes.length,
                bytesToHex(bytes)
              });
          throw new RTIinternalError(ex.getMessage(), ex);
        }

        // set the new parameter value
        parameter.getAccessor().set(theInteraction, value);

        parameterSet.add(parameter);
      }

      if (result == null) {
        result = new DeserializedInteractionData(theInteraction, parameterSet);
      } else {
        result.set(theInteraction, parameterSet);
      }

      return result;
    } catch (InteractionClassNotDefined | ReflectiveOperationException ex) {
      throw new RTIinternalError(ex.getMessage(), ex);
    }
  }

  /////////////////////
  // SUPPORT METHODS //
  /////////////////////
  /**
   * This method returns the OMT attribute, given its name, or null when none found.
   *
   * @param attributes
   * @param name
   * @return
   */
  private nl.tno.omt.Attribute getOmtAttributeByName(
      Set<nl.tno.omt.Attribute> attributes, String name) {
    for (nl.tno.omt.Attribute attribute : attributes) {
      if (attribute.getName().getValue().equals(name)) {
        return attribute;
      }
    }
    return null;
  }

  /**
   * This method returns the OMT parameter, given its name, or null when none found.
   *
   * @param attributes
   * @param name
   * @return
   */
  private nl.tno.omt.Parameter getOmtParameterByName(
      Set<nl.tno.omt.Parameter> parameters, String name) {
    for (nl.tno.omt.Parameter parameter : parameters) {
      if (parameter.getName().getValue().equals(name)) {
        return parameter;
      }
    }
    return null;
  }

  /**
   * This method returns fully qualified OMT class name for the provided Java class. Two variants
   * for class naming are handled:
   *
   * <p>(1) The Java class name and that of its supers corresponds with the FQ OMT class name, but
   * where each '.' is replaced by a '_'.
   *
   * <p>(2) The Java class name and that of its supers corresponds with the simple OMT class name.
   *
   * @param clazz
   * @param name of root class
   * @return FQ OMT class name
   */
  private String getFullyQualifiedClassName(Class clazz, String rootName) {
    if (clazz == Object.class) {
      return rootName;
    }

    String thisName = clazz.getSimpleName();
    if (thisName.equals(rootName)) {
      return rootName;
    }

    if (clazz.getSuperclass() != Object.class) {
      String superName = clazz.getSuperclass().getSimpleName();
      String lastPartOfThisName = thisName.substring(thisName.lastIndexOf("_") + 1);

      // thisName may be a fully qualified class name with a "_" to seperate the parts.
      // But thisName may be a class with just a "_" in the name, so we check this possibility here.
      // And note that the superName may or may not be fully qualified, hence the endsWith.
      if (thisName.endsWith(superName + "_" + lastPartOfThisName)) {
        return getFullyQualifiedClassName(clazz.getSuperclass(), rootName)
            + "."
            + lastPartOfThisName;
      } else {
        return getFullyQualifiedClassName(clazz.getSuperclass(), rootName) + "." + thisName;
      }
    } else {
      return getFullyQualifiedClassName(clazz.getSuperclass(), rootName) + "." + thisName;
    }
  }

  private String getFullyQualifiedObjectClassName(Class clazz) {
    return getFullyQualifiedClassName(clazz, OmtMimConstants.HLAOBJECTROOT);
  }

  private String getFullyQualifiedInteractionClassName(Class clazz) {
    return getFullyQualifiedClassName(clazz, OmtMimConstants.HLAINTERACTIONROOT);
  }

  //////////////////////////////////////
  // Support methods for ObjectClass
  //////////////////////////////////////
  public ObjectClass getObjectClassIfExists(boolean pub, Class clazz) throws ObjectClassNotDefined {
    ObjectClass oc = getObjectClass(pub, clazz);
    if (oc == null) {
      throw new ObjectClassNotDefined("Unknown class " + clazz.getSimpleName());
    } else {
      return oc;
    }
  }

  public ObjectClass getObjectClass(boolean pub, Class clazz) {
    return pub ? pubObjectClasses.getClassByClazz(clazz) : subObjectClasses.getClassByClazz(clazz);
  }

  public ObjectClass getObjectClass(boolean pub, ObjectClassHandle classHandle) {
    return pub
        ? pubObjectClasses.getClassByHandle(classHandle)
        : subObjectClasses.getClassByHandle(classHandle);
  }

  //////////////////////////////////////
  // Support methods for InteractionClass
  //////////////////////////////////////
  public InteractionClass getInteractionClassIfExists(boolean pub, Class clazz)
      throws InteractionClassNotDefined {
    InteractionClass ic = getInteractionClass(pub, clazz);
    if (ic == null) {
      throw new InteractionClassNotDefined("Unknown class " + clazz.getSimpleName());
    }
    return ic;
  }

  public InteractionClass getInteractionClass(boolean pub, Class clazz) {
    return pub
        ? pubInteractionClasses.getClassByClazz(clazz)
        : subInteractionClasses.getClassByClazz(clazz);
  }

  public InteractionClass getInteractionClass(boolean pub, InteractionClassHandle classHandle) {
    return pub
        ? pubInteractionClasses.getClassByHandle(classHandle)
        : subInteractionClasses.getClassByHandle(classHandle);
  }

  //////////////////////////////////////
  // Support methods for ObjectInstance
  //////////////////////////////////////
  public ObjectInstance getObjectInstance(Object theObject) {
    return objectInstances.getObjectInstanceByObject(theObject);
  }

  public ObjectInstance getObjectInstanceIfExists(Object theObject) throws ObjectInstanceNotKnown {
    ObjectInstance oi = objectInstances.getObjectInstanceByObject(theObject);
    if (oi == null) {
      throw new ObjectInstanceNotKnown(
          "Unknown object of class " + theObject.getClass().getSimpleName());
    } else {
      return oi;
    }
  }

  public ObjectInstance getObjectInstance(String theName) {
    return objectInstances.getObjectInstanceByName(theName);
  }

  public ObjectInstance getObjectInstanceIfExists(String theName) throws ObjectInstanceNotKnown {
    ObjectInstance oi = objectInstances.getObjectInstanceByName(theName);
    if (oi == null) {
      throw new ObjectInstanceNotKnown("Unknown object " + theName);
    } else {
      return oi;
    }
  }

  public ObjectInstance getObjectInstance(ObjectInstanceHandle instanceHandle) {
    return objectInstances.getObjectInstanceByHandle(instanceHandle);
  }

  public Collection<ObjectInstance> getObjectInstances() {
    return this.objectInstances.getObjectInstances();
  }

  //////////////////////////////////////
  // Support methods for hex conversion
  //////////////////////////////////////
  private final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

  private String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = HEX_ARRAY[v >>> 4];
      hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
    }
    return new String(hexChars);
  }
}
