package nl.tno.oorti.impl;

import nl.tno.omt.helpers.OmtMimConstants;

/**
 * @author bergtwvd
 */
class Helpers {

  private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

  /**
   * This method returns a character string representation for the given byte array.
   *
   * @param bytes
   * @return Character string
   */
  static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = HEX_ARRAY[v >>> 4];
      hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
    }
    return new String(hexChars);
  }

  /**
   * This method returns a fully qualified OMT class name for the provided Java class. Two variants
   * for class naming are handled:
   *
   * <p>(1) The Java class name and that of its supers corresponds with the FQ OMT class name, but
   * where each '.' is replaced by a '_'.
   *
   * <p>(2) The Java class name and that of its supers corresponds with the simple OMT class name.
   *
   * @param clazz
   * @param rootName of root class
   * @return FQ OMT class name
   */
  static String getFullyQualifiedClassName(Class clazz, String rootName) {
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

  static String getFullyQualifiedObjectClassName(Class clazz) {
    return getFullyQualifiedClassName(clazz, OmtMimConstants.HLAOBJECTROOT);
  }

  static String getFullyQualifiedInteractionClassName(Class clazz) {
    return getFullyQualifiedClassName(clazz, OmtMimConstants.HLAINTERACTIONROOT);
  }
}
