package nl.tno.oorti.accessor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bergtwvd
 */
public class ClassUtils {

  // lookup map of classes and their fields
  private static final Map<Class, Map<String, Field>> class2FieldMap = new ConcurrentHashMap<>();

  // this method creates a lookup map for the fields of a class, including any inherited fields
  private static Map<String, Field> createFieldMap(Class clazz) {
    Map<String, Field> fieldMap = new ConcurrentHashMap<>();
    while (clazz != Object.class) {
      // get ALL fields of THIS class
      for (Field f : clazz.getDeclaredFields()) {
        int modifiers = f.getModifiers();
        // skip transient fields
        if (!Modifier.isTransient(modifiers)) {
          fieldMap.put(f.getName(), f);
        }
      }
      clazz = clazz.getSuperclass();
    }
    return fieldMap;
  }

  public static Collection<Field> getFields(Class clazz) {
    Map<String, Field> fieldMap = class2FieldMap.get(clazz);
    if (fieldMap == null) {
      fieldMap = createFieldMap(clazz);
      class2FieldMap.put(clazz, fieldMap);
    }
    return fieldMap.values();
  }

  public static Field getField(Class clazz, String name) {
    Map<String, Field> fieldMap = class2FieldMap.get(clazz);
    if (fieldMap == null) {
      fieldMap = createFieldMap(clazz);
      class2FieldMap.put(clazz, fieldMap);
    }
    return fieldMap.get(name);
  }
}
