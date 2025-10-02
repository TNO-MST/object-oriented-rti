package nl.tno.oorti;

import nl.tno.omt.helpers.OmtMimConstants;

/**
 * Utility functions to work with enumerators.
 *
 * @author bergtwvd
 */
public class EnumFunctions {

  /**
   * Return enumerator object, given its name.
   *
   * @param <T> type of enumerator object
   * @param clazz: enumerator class
   * @param name: name of enumerator
   * @return enumerator
   */
  public static <T> T valueOf(Class clazz, String name) {
    if (clazz == null) {
      throw new IllegalArgumentException("Invalid null value for enum class");
    }

    if (name == null) {
      throw new IllegalArgumentException("Invalid null value for name");
    }

    if (clazz.isEnum()) {
      return (T) Enum.valueOf(clazz, name);
    } else {
      if (clazz == boolean.class || clazz == Boolean.class) {
        switch (name) {
          case OmtMimConstants.HLAFALSE -> {
            return (T) Boolean.FALSE;
          }
          case OmtMimConstants.HLATRUE -> {
            return (T) Boolean.TRUE;
          }
          default ->
              throw new IllegalArgumentException(
                  clazz.getCanonicalName() + "." + name + " is not a boolean value");
        }
      } else {
        throw new IllegalArgumentException(
            clazz.getCanonicalName() + "." + name + " is not an enum");
      }
    }
  }

  /**
   * Return the name of an enumerator object.
   *
   * @param object: enumerator
   * @return name of the enumerator
   */
  public static String name(Object object) {
    if (object == null) {
      throw new IllegalArgumentException("Invalid null value for enum constant");
    }

    Class clazz = object.getClass();
    if (clazz.isEnum()) {
      return ((Enum) object).name();
    } else {
      if (Boolean.class.isAssignableFrom(clazz)) {
        return (Boolean) object ? OmtMimConstants.HLATRUE : OmtMimConstants.HLAFALSE;
      } else {
        throw new IllegalArgumentException(
            "No enum constant " + clazz.getCanonicalName() + "." + object.toString());
      }
    }
  }
}
