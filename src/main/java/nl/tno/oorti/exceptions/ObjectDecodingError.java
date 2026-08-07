package nl.tno.oorti.exceptions;

import hla.rti1516e.exceptions.RTIexception;
import nl.tno.oorti.OOattribute;

/**
 * @author bergtwvd
 */
public class ObjectDecodingError extends RTIexception {

  final Object theObject;
  final OOattribute attribute;
  final String value; // byte string representation of attribute value

  public ObjectDecodingError(String msg, Object theObject, OOattribute attribute, String value) {
    super(msg);
    this.theObject = theObject;
    this.attribute = attribute;
    this.value = value;
  }

  public ObjectDecodingError(
      String msg, Throwable cause, Object theObject, OOattribute attribute, String value) {
    super(msg, cause);
    this.theObject = theObject;
    this.attribute = attribute;
    this.value = value;
  }

  public Object getTheObject() {
    return theObject;
  }

  public OOattribute getAttribute() {
    return attribute;
  }

  public String getValue() {
    return value;
  }
}
