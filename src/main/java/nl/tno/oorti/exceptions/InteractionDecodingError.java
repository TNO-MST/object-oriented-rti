package nl.tno.oorti.exceptions;

import hla.rti1516e.exceptions.RTIexception;
import nl.tno.oorti.OOparameter;

/**
 * @author bergtwvd
 */
public class InteractionDecodingError extends RTIexception {

  final Object theInteraction;
  final OOparameter parameter;
  final String value; // byte string representation of parameter value

  public InteractionDecodingError(String msg, Object theInteraction, OOparameter parameter, String value) {
    super(msg);
    this.theInteraction = theInteraction;
    this.parameter = parameter;
    this.value = value;
  }

  public InteractionDecodingError(
      String msg, Throwable cause, Object theInteraction, OOparameter parameter, String value) {
    super(msg, cause);
    this.theInteraction = theInteraction;
    this.parameter = parameter;
    this.value = value;
  }

  public Object getTheInteraction() {
    return theInteraction;
  }

  public OOparameter getParameter() {
    return parameter;
  }

  public String getValue() {
    return value;
  }
}
