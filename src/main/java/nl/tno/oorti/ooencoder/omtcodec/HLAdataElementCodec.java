package nl.tno.oorti.ooencoder.omtcodec;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import nl.tno.oorti.ooencoder.exceptions.InvalidClassStructure;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;

/**
 * Abstract super class with support methods for Codec classes.
 *
 * @author bergtwvd
 */
public abstract class HLAdataElementCodec {

  /**
   * Returns the padding size.
   *
   * @param dividend number of bytes used
   * @param divisor octet boundary, must a a power of 2 (i.e. 1, 2, 4, 8 ...).
   * @return padding size to get to octet boundary
   */
  protected int paddingSize(int dividend, int divisor) {
    return (divisor - dividend) & (divisor - 1);
  }

  /**
   * Returns a new object, optionally providing an outer object that the new object is part of.
   *
   * @param clazz Class of the new object
   * @param outer optional outer object (null for none)
   * @param enclosingCtor optional constructor of outer class (if an outer object is provided)
   * @return new object
   * @throws OOcodecException
   */
  protected Object createNewObect(Class clazz, Object outer, Constructor enclosingCtor)
      throws OOcodecException {
    if (enclosingCtor == null) {
      try {
        return clazz.getConstructor().newInstance();
      } catch (NoSuchMethodException
          | SecurityException
          | InstantiationException
          | IllegalAccessException
          | IllegalArgumentException
          | InvocationTargetException ex) {
        throw new InvalidClassStructure(ex.getMessage(), ex);
      }
    } else {
      if (outer != null) {
        try {
          return enclosingCtor.newInstance(outer);
        } catch (InstantiationException
            | IllegalAccessException
            | IllegalArgumentException
            | InvocationTargetException ex) {
          throw new InvalidClassStructure(ex.getMessage(), ex);
        }
      } else {
        throw new InvalidClassStructure(
            "No outer object instance provided for outer class "
                + clazz.getEnclosingClass().getName());
      }
    }
  }
}
