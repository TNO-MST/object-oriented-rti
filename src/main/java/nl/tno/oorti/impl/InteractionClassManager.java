package nl.tno.oorti.impl;

import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMapFactory;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InteractionClassNotDefined;
import hla.rti1516e.exceptions.InteractionParameterNotDefined;
import hla.rti1516e.exceptions.InvalidInteractionClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
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
 * Thread-safe class to manage InteractionClasses. The collection is accessed by both the federate
 * and RTI ambassador threads.
 *
 * @author bergtwvd
 */
public class InteractionClassManager {

  // static properties
  private final RTIambassador rtiamb;
  private final AccessorFactory accessorFactory;
  private final OOencoderFactory encoderFactory;
  private final ObjectModelType[] modules;
  private final ParameterHandleValueMapFactory paramHVMFactory;

  // dynamic properties
  private final Map<Class, InteractionClass> clazz2class = new ConcurrentHashMap<>();
  private final Map<InteractionClassHandle, InteractionClass> handle2class =
      new ConcurrentHashMap<>();

  public InteractionClassManager(
      RTIambassador rtiamb,
      AccessorFactory accessorFactory,
      OOencoderFactory encoderFactory,
      ObjectModelType[] modules)
      throws FederateNotExecutionMember, NotConnected {

    this.rtiamb = rtiamb;
    this.accessorFactory = accessorFactory;
    this.encoderFactory = encoderFactory;
    this.modules = modules;
    this.paramHVMFactory = rtiamb.getParameterHandleValueMapFactory();
  }

  public InteractionClass create(Class clazz)
      throws FederateNotExecutionMember,
          NotConnected,
          RTIinternalError,
          InteractionClassNotDefined,
          InteractionParameterNotDefined {

    InteractionClass ic = this.clazz2class.get(clazz);
    if (ic != null) return ic;

    try {
      String fqClassName = Helpers.getFullyQualifiedInteractionClassName(clazz);
      InteractionClassHandle classHandle = rtiamb.getInteractionClassHandle(fqClassName);
      ic = new InteractionClass(clazz, fqClassName, classHandle, this.paramHVMFactory);

      this.createParameterSet(ic);

      clazz2class.put(ic.getClazz(), ic);
      handle2class.put(ic.getClassHandle(), ic);

      return ic;
    } catch (NameNotFound ex) {
      throw new InteractionClassNotDefined(ex.getMessage(), ex);
    }
  }

  /** This method returns the OMT parameter, given its name, or null when none found. */
  private nl.tno.omt.Parameter getOmtParameterByName(
      Set<nl.tno.omt.Parameter> parameters, String name) {
    for (nl.tno.omt.Parameter parameter : parameters) {
      if (parameter.getName().getValue().equals(name)) {
        return parameter;
      }
    }
    return null;
  }

  private void createParameterSet(InteractionClass ic)
      throws InteractionClassNotDefined,
          RTIinternalError,
          FederateNotExecutionMember,
          NotConnected,
          InteractionParameterNotDefined {

    // get the parameters as defined in the FOM
    Set<nl.tno.omt.Parameter> omtParameters =
        OmtFunctions.getInteractionClassParameters(modules, ic.getName());
    if (omtParameters == null) {
      // something went wrong
      throw new InteractionClassNotDefined("Cannot get parameters of Class " + ic.getName());
    }

    // create a parameter for each Java class field
    Collection<Field> fields = ClassUtils.getFields(ic.getClazz());

    for (Field field : fields) {
      try {
        String fieldName = field.getName();
        String parameterName = OmtJavaMapping.toOmtName(fieldName);

        // check if the field exists in the FOM
        nl.tno.omt.Parameter omtParameter =
            this.getOmtParameterByName(omtParameters, parameterName);
        if (omtParameter == null) {
          throw new InteractionParameterNotDefined(
              "Java Class attribute " + field.getName() + " not defined in FOM");
        }

        // create accessor for the Java Class field
        Accessor accessor = accessorFactory.createAccessor(field);

        // create codec for the Java Class field
        OOencoder codec =
            encoderFactory.createOOencoder(
                field.getGenericType(), omtParameter.getDataType().getValue());

        ParameterHandle parameterHandle =
            rtiamb.getParameterHandle(ic.getClassHandle(), parameterName);

        Parameter parameter = new Parameter(ic, parameterName, parameterHandle, accessor, codec);

        ic.addParameter(parameter);
      } catch (OOcodecException | ReflectiveOperationException ex) {
        throw new RTIinternalError(ex.getMessage(), ex);
      } catch (InvalidInteractionClassHandle ex) {
        throw new InteractionClassNotDefined(ex.getMessage(), ex);
      } catch (NameNotFound ex) {
        throw new InteractionParameterNotDefined(ex.getMessage(), ex);
      }
    }
  }

  public InteractionClass getClassByClazz(Class clazz) {
    return clazz2class.get(clazz);
  }

  public InteractionClass getClassByHandle(InteractionClassHandle classHandle) {
    return handle2class.get(classHandle);
  }

  public InteractionClass getInteractionClassIfExists(Class clazz)
      throws InteractionClassNotDefined {
    InteractionClass ic = clazz2class.get(clazz);
    if (ic == null) {
      throw new InteractionClassNotDefined("Unknown class " + clazz.getSimpleName());
    } else return ic;
  }
}
