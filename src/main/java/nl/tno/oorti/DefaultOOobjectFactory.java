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
  public <C extends Object, T extends C> T createObject(Class<C> clazz) throws RTIinternalError {
    try {
      return  (T) clazz.getConstructor().newInstance();
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
  public <C extends Object, T extends C> T createInteraction(Class<C> clazz) throws RTIinternalError {
    try {
      return (T) clazz.getConstructor().newInstance();
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
  public <C extends Object, T extends C> Class<C> getObjectClass(T object) {
    return (Class<C>) object.getClass();
  }

  @Override
  public <C extends Object, T extends C> Class<C> getInteractionClass(T object) {
    return (Class<C>) object.getClass();
  }
}
