package nl.tno.oorti;

import hla.rti1516e.exceptions.RTIinternalError;
import java.lang.reflect.InvocationTargetException;

/**
 * Default object factory for creating HLA object class instances and HLA interactions.
 *
 * @author bergtwvd
 */
public class DefaultOOobjectFactory implements OOobjectFactory {

  @Override
  public <T extends Object> T createObject(Class<T> clazz) throws RTIinternalError {
    try {
      return clazz.getConstructor().newInstance();
    } catch (NoSuchMethodException
        | SecurityException
        | InstantiationException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException ex) {
      throw new RTIinternalError(ex.getMessage(), ex);
    }
  }

  @Override
  public <T extends Object> T createInteraction(Class<T> clazz) throws RTIinternalError {
    try {
      return clazz.getConstructor().newInstance();
    } catch (NoSuchMethodException
        | SecurityException
        | InstantiationException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException ex) {
      throw new RTIinternalError(ex.getMessage(), ex);
    }
  }

  @Override
  public <T extends Object> Class<T> getObjectClass(T object) {
    return (Class<T>) object.getClass();
  }

  @Override
  public <T extends Object> Class<T> getInteractionClass(T object) {
    return (Class<T>) object.getClass();
  }
}
