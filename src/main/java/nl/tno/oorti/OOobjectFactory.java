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
 * <p>An object factory may create an object y that is an instance of a sub class Y of the requested
 * class X. The type that must be returned for object y must be X.
 *
 * @author bergtwvd
 */
public interface OOobjectFactory {

  <T extends Object> T createObject(Class<T> clazz) throws RTIinternalError, ObjectClassNotDefined;

  <T extends Object> T createInteraction(Class<T> clazz)
      throws RTIinternalError, InteractionClassNotDefined;

  <T extends Object> Class<T> getObjectClass(T object)
      throws RTIinternalError, ObjectInstanceNotKnown;

  <T extends Object> Class<T> getInteractionClass(T object)
      throws RTIinternalError, InteractionClassNotDefined;
}
