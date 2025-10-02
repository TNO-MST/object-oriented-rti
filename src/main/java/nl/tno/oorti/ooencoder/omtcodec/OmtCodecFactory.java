package nl.tno.oorti.ooencoder.omtcodec;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import nl.tno.omt.ArrayDataTypesType.ArrayData;
import nl.tno.omt.BasicDataRepresentationsType.BasicData;
import nl.tno.omt.EnumeratedDataTypesType.EnumeratedData;
import nl.tno.omt.FixedRecordDataTypesType.FixedRecordData;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.SimpleDataTypesType.SimpleData;
import nl.tno.omt.VariantRecordDataTypesType.VariantRecordData;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.omt.helpers.OmtMimConstants;
import nl.tno.oorti.AccessorType;
import nl.tno.oorti.accessor.AccessorFactory;
import nl.tno.oorti.accessor.AccessorFactoryFactory;
import nl.tno.oorti.ooencoder.exceptions.InvalidClassStructure;
import nl.tno.oorti.ooencoder.exceptions.InvalidType;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * Thread-safe factory for creating thread safe OMT codecs.
 *
 * @author bergtwvd
 */
public class OmtCodecFactory {

  ////////////////////////////////////////////////////////////////////////////
  // Properties
  ////////////////////////////////////////////////////////////////////////////
  private final AccessorFactory accessorFactory;
  private final ObjectModelType[] modules;
  private final Set<String> paddingDataTypes;
  private final Map<String, Class<? extends OmtBasicDatatypeCodec>> basicDatatypeMap;
  private final Map<String, Class<? extends OmtDatatypeCodec>> datatypeMap;
  private final Map<String, Class<? extends OmtDatatypeCodec>> encodingMap;
  private final Map<Entry<Type, String>, OmtDatatypeCodec> datatypeCodecMap;
  private final Map<Entry<Type, String>, OmtBasicDatatypeCodec> basicDatatypeCodecMap;

  ////////////////////////////////////////////////////////////////////////////
  // Constructors
  ////////////////////////////////////////////////////////////////////////////
  /**
   * This constructor creates a factory for creating OMT codecs.
   *
   * @param accessorFactory: Accessor factory for accessing class properties
   * @param modules: OMT modules
   */
  public OmtCodecFactory(AccessorFactory accessorFactory, ObjectModelType[] modules) {
    this.accessorFactory = accessorFactory;
    this.modules = modules;

    this.paddingDataTypes = new HashSet<>();
    this.basicDatatypeMap = new HashMap<>();
    this.datatypeMap = new HashMap<>();
    this.encodingMap = new HashMap<>();

    this.basicDatatypeCodecMap = new ConcurrentHashMap<>();
    this.datatypeCodecMap = new ConcurrentHashMap<>();

    // Register the standard codecs
    this.register();
  }

  public OmtCodecFactory(ObjectModelType[] modules) {
    this(AccessorFactoryFactory.getAccessorFactory(AccessorType.LAMBDA), modules);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Public factory methods
  ////////////////////////////////////////////////////////////////////////////
  public OmtDatatypeCodec createDatatype(Type type) throws OOcodecException {
    return createDatatype(type, null);
  }

  public OmtDatatypeCodec createDatatype(Type type, String name) throws OOcodecException {
    if (type == null) {
      throw new InvalidClassStructure("Illegal null value for class type");
    }

    Entry<Type, String> entry = new SimpleEntry<>(type, name);

    OmtDatatypeCodec codec = datatypeCodecMap.get(entry);
    if (codec == null) {
      codec = name == null ? createDatatypeCodec(type) : createDataTypeCodec(type, name);
      datatypeCodecMap.put(entry, codec);
    }

    return codec;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Package-level methods called from codec classes
  ////////////////////////////////////////////////////////////////////////////
  OmtBasicDatatypeCodec createBasicDataTypeCodec(Type type, String name) throws OOcodecException {
    if (type == null) {
      throw new InvalidClassStructure("Illegal null value for class type");
    }

    if (name == null) {
      throw new InvalidType("Illegal null value for representation name");
    }

    Entry<Type, String> entry = new SimpleEntry<>(type, name);

    OmtBasicDatatypeCodec codec = basicDatatypeCodecMap.get(entry);
    if (codec == null) {
      BasicData dt = OmtFunctions.getBasicDataByName(modules, name);
      if (dt == null) {
        throw new InvalidType(
            "Unknown basic datatype " + name + " specified for Type " + type.getTypeName());
      }

      codec = createCodecForBasicDataType(type, dt);
      basicDatatypeCodecMap.put(entry, codec);
    }

    return codec;
  }

  AccessorFactory getAccessorFactory() {
    return this.accessorFactory;
  }

  boolean hasPaddingEncoding(String datatype) {
    return this.paddingDataTypes.contains(datatype);
  }

  ObjectModelType[] getModules() {
    return this.modules;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Protected methods called from a subclass
  ////////////////////////////////////////////////////////////////////////////
  protected final void registerBasicDatatype(
      String representation, Class<? extends OmtBasicDatatypeCodec> clazz) {
    this.basicDatatypeMap.put(representation, clazz);
  }

  protected final void registerDatatype(String datatype, Class<? extends OmtDatatypeCodec> clazz) {
    this.datatypeMap.put(datatype, clazz);
  }

  protected final void registerEncoding(String encoding, Class<? extends OmtDatatypeCodec> clazz) {
    this.registerEncoding(encoding, clazz, false);
  }

  protected final void registerEncoding(
      String encoding, Class<? extends OmtDatatypeCodec> clazz, boolean isPadding) {
    this.encodingMap.put(encoding, clazz);

    if (isPadding) {
      // add the array datatypes with this encoding
      Set<String> datatypes = OmtFunctions.getArrayDataTypesWithEncoding(modules, Set.of(encoding));
      this.paddingDataTypes.addAll(datatypes);
    }
  }

  ////////////////////////////////////////////////////////////////////////////
  // Private factory methods called from the main factory methods
  ////////////////////////////////////////////////////////////////////////////
  private void register() {
    // basic datatypes
    this.registerBasicDatatype(OmtMimConstants.HLAFLOAT32BE, HLAfloat32BECodec.class);
    this.registerBasicDatatype(OmtMimConstants.HLAFLOAT64BE, HLAfloat64BECodec.class);
    this.registerBasicDatatype(OmtMimConstants.HLAINTEGER16BE, HLAinteger16BECodec.class);
    this.registerBasicDatatype(OmtMimConstants.HLAINTEGER32BE, HLAinteger32BECodec.class);
    this.registerBasicDatatype(OmtMimConstants.HLAINTEGER64BE, HLAinteger64BECodec.class);
    this.registerBasicDatatype(
        OmtMimConstants.HLAUNSIGNEDINTEGER16BE, HLAunsignedInteger16BECodec.class);
    this.registerBasicDatatype(
        OmtMimConstants.HLAUNSIGNEDINTEGER32BE, HLAunsignedInteger32BECodec.class);
    this.registerBasicDatatype(
        OmtMimConstants.HLAUNSIGNEDINTEGER64BE, HLAunsignedInteger64BECodec.class);
    this.registerBasicDatatype(OmtMimConstants.HLAOCTET, HLAoctetCodec.class);
    this.registerBasicDatatype(OmtMimConstants.HLAOCTETPAIRBE, HLAoctetPairBECodec.class);

    // encodings
    this.registerEncoding(
        OmtMimConstants.HLAEXTENDABLEVARIANTRECORD, HLAextendableVariantRecordCodec.class);
    this.registerEncoding(OmtMimConstants.HLAFIXEDARRAY, HLAfixedArrayCodec.class);
    this.registerEncoding(OmtMimConstants.HLAFIXEDRECORD, HLAfixedRecordCodec.class);
    this.registerEncoding(OmtMimConstants.HLAVARIABLEARRAY, HLAvariableArrayCodec.class);
    this.registerEncoding(OmtMimConstants.HLAVARIANTRECORD, HLAvariantRecordCodec.class);
  }

  private OmtDatatypeCodec createDatatypeCodec(Type type) throws OOcodecException {
    if (type == null) {
      throw new InvalidClassStructure("Illegal null value for class type");
    }

    if (type instanceof ParameterizedType) {
      // we assume that the type concerns an array datatype
      return this.createCodecForEncoding(type, OmtMimConstants.HLAVARIABLEARRAY);
    } else {
      if (((Class) type).isArray() || type.equals(String.class)) {
        return this.createCodecForEncoding(type, OmtMimConstants.HLAVARIABLEARRAY);
      } else if (type.equals(UUID.class)) {
        return this.createCodecForEncoding(type, OmtMimConstants.HLAFIXEDARRAY);
      } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
        return this.createDataTypeCodec(type, OmtMimConstants.HLABOOLEAN);
      } else if (type.equals(Short.class) || type.equals(short.class)) {
        return this.createBasicDataTypeCodec(type, OmtMimConstants.HLAINTEGER16BE);
      } else if (type.equals(Integer.class) || type.equals(int.class)) {
        return this.createBasicDataTypeCodec(type, OmtMimConstants.HLAINTEGER32BE);
      } else if (type.equals(Long.class) || type.equals(long.class)) {
        return this.createBasicDataTypeCodec(type, OmtMimConstants.HLAINTEGER64BE);
      } else if (type.equals(Float.class) || type.equals(float.class)) {
        return this.createBasicDataTypeCodec(type, OmtMimConstants.HLAFLOAT32BE);
      } else if (type.equals(Double.class) || type.equals(double.class)) {
        return this.createBasicDataTypeCodec(type, OmtMimConstants.HLAFLOAT64BE);
      } else if (type.equals(Byte.class) || type.equals(byte.class)) {
        return this.createBasicDataTypeCodec(type, OmtMimConstants.HLAOCTET);
      } else if (type.equals(Character.class) || type.equals(char.class)) {
        return this.createBasicDataTypeCodec(type, OmtMimConstants.HLAOCTETPAIRBE);
      } else {
        // this must be: a fixed record type, a variant record type, or an enum type
        return this.createDataTypeCodec(type, ((Class) type).getSimpleName());
      }
    }
  }

  private OmtDatatypeCodec createDataTypeCodec(Type type, String name) throws OOcodecException {
    if (type == null) {
      throw new InvalidClassStructure("Illegal null value for class type");
    }

    if (name == null) {
      throw new InvalidType("Illegal null value for datatype name");
    }

    if (type instanceof ParameterizedType) {
      // we assume that the type concerns an array datatype
      ArrayData arraydt = OmtFunctions.getArrayDataByName(modules, name);
      if (arraydt != null) {
        return createCodecForDatatypeOrEncoding(
            type, arraydt, arraydt.getName().getValue(), arraydt.getEncoding().getValue());
      } else {
        throw new InvalidType(
            "Unknown array datatype " + name + " specified for Type " + type.getTypeName());
      }
    }

    SimpleData simpledt = OmtFunctions.getSimpleDataByName(this.modules, name);
    if (simpledt != null) {
      return createCodecForSimpleDatatype(type, simpledt);
    }

    EnumeratedData enumdt = OmtFunctions.getEnumeratedDataByName(this.modules, name);
    if (enumdt != null) {
      return new HLAenumCodec(this, type, enumdt);
    }

    ArrayData arraydt = OmtFunctions.getArrayDataByName(this.modules, name);
    if (arraydt != null) {
      return createCodecForDatatypeOrEncoding(
          type, arraydt, arraydt.getName().getValue(), arraydt.getEncoding().getValue());
    }

    FixedRecordData fixedrecdt = OmtFunctions.getFixedRecordDataByName(this.modules, name);
    if (fixedrecdt != null) {
      return createCodecForDatatypeOrEncoding(
          type, fixedrecdt, fixedrecdt.getName().getValue(), fixedrecdt.getEncoding().getValue());
    }

    VariantRecordData variantrecdt = OmtFunctions.getVariantRecordDataByName(this.modules, name);
    if (variantrecdt != null) {
      return createCodecForDatatypeOrEncoding(
          type,
          variantrecdt,
          variantrecdt.getName().getValue(),
          variantrecdt.getEncoding().getValue());
    }

    throw new InvalidType("Unknown datatype " + name + " specified for Type " + type.getTypeName());
  }

  private <T extends OmtDatatypeCodec> T createInstance(Class<T> codecClass, Type type, Object dt)
      throws OOcodecException {
    try {
      Constructor<T> ctor =
          codecClass.getDeclaredConstructor(OmtCodecFactory.class, Type.class, dt.getClass());

      try {
        return ctor.newInstance(this, type, dt);
      } catch (InstantiationException
          | IllegalAccessException
          | IllegalArgumentException
          | InvocationTargetException ex) {
        if (ex.getCause() instanceof OOcodecException ooException) {
          throw ooException;
        } else
          throw new InvalidClassStructure(
              "Error in construction of codec class "
                  + ctor.getName()
                  + " for type="
                  + type.getTypeName()
                  + " dt="
                  + dt.getClass().getSimpleName(),
              ex);
      }
    } catch (NoSuchMethodException | SecurityException | IllegalArgumentException ex) {
      throw new InvalidClassStructure(
          "No constructor found for type="
              + type.getTypeName()
              + " dt="
              + dt.getClass().getSimpleName());
    }
  }

  private <T extends OmtDatatypeCodec> T createInstance(Class<T> codecClass, Type type)
      throws OOcodecException {
    try {
      Constructor<T> ctor = codecClass.getDeclaredConstructor(OmtCodecFactory.class, Type.class);
      try {
        return ctor.newInstance(this, type);
      } catch (InstantiationException
          | IllegalAccessException
          | IllegalArgumentException
          | InvocationTargetException ex) {
        if (ex.getCause() instanceof OOcodecException ooException) {
          throw ooException;
        } else
          throw new InvalidClassStructure(
              "Error in construction of codec class "
                  + ctor.getName()
                  + " for type="
                  + type.getTypeName(),
              ex);
      }
    } catch (NoSuchMethodException | SecurityException | IllegalArgumentException ex) {
      throw new InvalidClassStructure("No constructor found for type=" + type.getTypeName());
    }
  }

  private OmtBasicDatatypeCodec createCodecForBasicDataType(Type type, BasicData dt)
      throws OOcodecException {

    Class<? extends OmtBasicDatatypeCodec> codecClass =
        this.basicDatatypeMap.get(dt.getName().getValue());
    if (codecClass != null) {
      return this.createInstance(codecClass, type, dt);
    }

    throw new InvalidType(
        "No codec class found for type=" + type.getTypeName() + " dt=" + dt.getName().getValue());
  }

  private OmtDatatypeCodec createCodecForSimpleDatatype(Type type, SimpleData dt)
      throws OOcodecException {

    Class<? extends OmtDatatypeCodec> codecClass = this.datatypeMap.get(dt.getName().getValue());
    if (codecClass != null) {
      return createInstance(codecClass, type, dt);
    } else {
      // use the representation instead
      return createBasicDataTypeCodec(type, dt.getRepresentation().getValue());
    }
  }

  private OmtDatatypeCodec createCodecForEncoding(Type type, String encoding)
      throws OOcodecException {
    Class<? extends OmtDatatypeCodec> codecClass = this.encodingMap.get(encoding);
    if (codecClass != null) {
      return createInstance(codecClass, type);
    }

    throw new InvalidType(
        "No codec class found for type=" + type.getTypeName() + " encoding=" + encoding);
  }

  private OmtDatatypeCodec createCodecForDatatypeOrEncoding(
      Type type, Object dt, String datatype, String encoding) throws OOcodecException {

    Class<? extends OmtDatatypeCodec> codecClass = this.datatypeMap.get(datatype);
    if (codecClass != null) {
      return createInstance(codecClass, type, dt);
    }

    codecClass = this.encodingMap.get(encoding);
    if (codecClass != null) {
      return createInstance(codecClass, type, dt);
    }

    throw new InvalidType(
        "No codec class found for type="
            + type.getTypeName()
            + " dt="
            + dt.getClass().getSimpleName()
            + " datatype="
            + datatype
            + " encoding="
            + encoding);
  }
}
