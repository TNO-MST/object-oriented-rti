package nl.tno.oorti;

import hla.rti1516e.exceptions.InteractionClassNotDefined;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import hla.rti1516e.exceptions.RTIinternalError;

/**
 * The OOobjectFactory interface is used by the OORTI to create an object (HLA object instance or
 * HLA interaction) given its type (class), and to get the type of an object that was created
 * earlier by the factory.
 *
 * <p>An object factory may create an object o that is an instance of a sub class T of the requested
 * class C. The type that must be returned for the object o must be C.
 *
 * @author bergtwvd
 */
public interface OOobjectFactory {

  <C extends Object, T extends C> T createObject(Class<C> clazz)
      throws RTIinternalError, ObjectClassNotDefined;

  <C extends Object, T extends C> T createInteraction(Class<C> clazz)
      throws RTIinternalError, InteractionClassNotDefined;

  <C extends Object, T extends C> Class<C> getObjectClass(T object)
      throws RTIinternalError, ObjectInstanceNotKnown;

  <C extends Object, T extends C> Class<C> getInteractionClass(T object)
      throws RTIinternalError, InteractionClassNotDefined;
}
