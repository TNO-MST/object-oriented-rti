package nl.tno.oorti.exceptions;

import hla.rti1516e.exceptions.RTIexception;
import nl.tno.oorti.OOattribute;

/**
 * @author bergtwvd
 */
public class ObjectEncodingError extends RTIexception {

  final Object theObject;
  final OOattribute attribute;

  public ObjectEncodingError(String msg, Object theObject, OOattribute attribute) {
    super(msg);
    this.theObject = theObject;
    this.attribute = attribute;
  }

  public ObjectEncodingError(
      String msg, Throwable cause, Object theObject, OOattribute attribute) {
    super(msg, cause);
    this.theObject = theObject;
    this.attribute = attribute;
  }

  public Object getTheObject() {
    return theObject;
  }

  public OOattribute getAttribute() {
    return attribute;
  }
}
