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
 * Thread-safe class to manage InteractionClasses.
 *
 * @author bergtwvd
 */
public class InteractionClassManager {

  // immutable properties
  private final RTIambassador rtiamb;
  private final AccessorFactory accessorFactory;
  private final OOencoderFactory encoderFactory;
  private final ObjectModelType[] modules;
  private final ParameterManager pam;
  private final ParameterHandleValueMapFactory paramHVMFactory;

  // mutable properties
  private final Map<Class, InteractionClass> clazz2class = new ConcurrentHashMap<>();
  private final Map<InteractionClassHandle, InteractionClass> handle2class =
      new ConcurrentHashMap<>();

  public InteractionClassManager(
      RTIambassador rtiamb,
      AccessorFactory accessorFactory,
      OOencoderFactory encoderFactory,
      ObjectModelType[] modules)
      throws FederateNotExecutionMember, NotConnected {

    this.pam = new ParameterManager();
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
      String className = HelperFunctions.getFullyQualifiedInteractionClassName(clazz);
      InteractionClassHandle classHandle = this.rtiamb.getInteractionClassHandle(className);
      Set<Parameter> parameterSet = this.createParameterSet(clazz, className, classHandle);

      ic = new InteractionClass(clazz, className, classHandle, parameterSet, this.paramHVMFactory);

      this.clazz2class.put(ic.getClazz(), ic);
      this.handle2class.put(ic.getClassHandle(), ic);

      return ic;
    } catch (NameNotFound ex) {
      throw new InteractionClassNotDefined(ex.getMessage(), ex);
    }
  }

  /** This method returns the OMT parameter, given its name, or null when none found. */
  private nl.tno.omt.Parameter getOmtParameterByName(
      Set<nl.tno.omt.Parameter> parameters, String name) throws InteractionParameterNotDefined {
    for (nl.tno.omt.Parameter parameter : parameters) {
      if (parameter.getName().getValue().equals(name)) {
        return parameter;
      }
    }
    return null;
  }

  private Set<Parameter> createParameterSet(
      Class clazz, String className, InteractionClassHandle classHandle)
      throws InteractionClassNotDefined,
          RTIinternalError,
          FederateNotExecutionMember,
          NotConnected,
          InteractionParameterNotDefined {

    // get the parameters as defined in the FOM
    Set<nl.tno.omt.Parameter> omtParameters =
        OmtFunctions.getInteractionClassParameters(this.modules, className);
    if (omtParameters == null) {
      // something went wrong
      throw new InteractionClassNotDefined("Cannot get parameters of Class " + className);
    }

    Set<Parameter> parameterSet = new HashSet<>();

    // create a parameter for each Java class property
    for (Field field : ClassUtils.getFields(clazz)) {
      try {
        String parameterName = OmtJavaMapping.toOmtName(field.getName());

        nl.tno.omt.Parameter omtParameter =
            this.getOmtParameterByName(omtParameters, parameterName);
        if (omtParameter == null) {
          // Java property is not in the FOM, skip
          continue;
        }

        ParameterHandle parameterHandle =
            this.rtiamb.getParameterHandle(classHandle, parameterName);

        Parameter parameter = this.pam.getParameterByHandle(parameterHandle);
        if (parameter == null) {
          // create accessor for the Java Class field
          Accessor accessor = this.accessorFactory.createAccessor(field);

          // create codec for the Java Class field
          OOencoder codec =
              this.encoderFactory.createOOencoder(
                  field.getGenericType(), omtParameter.getDataType().getValue());

          parameter = this.pam.create(parameterName, parameterHandle, accessor, codec);
        }

        parameterSet.add(parameter);
      } catch (OOcodecException | ReflectiveOperationException ex) {
        throw new RTIinternalError(ex.getMessage(), ex);
      } catch (InvalidInteractionClassHandle ex) {
        throw new InteractionClassNotDefined(ex.getMessage(), ex);
      } catch (NameNotFound ex) {
        throw new InteractionParameterNotDefined(ex.getMessage(), ex);
      }
    }

    return parameterSet;
  }

  public InteractionClass getInteractionClassByHandle(InteractionClassHandle classHandle) {
    return this.handle2class.get(classHandle);
  }

  public InteractionClass getInteractionClassIfExists(Class clazz)
      throws InteractionClassNotDefined {
    InteractionClass ic = this.clazz2class.get(clazz);
    if (ic == null) {
      throw new InteractionClassNotDefined("Unknown class " + clazz.getSimpleName());
    } else return ic;
  }
}
