package nl.tno.oorti.exceptions;

import hla.rti1516e.exceptions.RTIexception;
import nl.tno.oorti.OOparameter;

/**
 * @author bergtwvd
 */
public class InteractionEncodingError extends RTIexception {

  final Object theInteraction;
  final OOparameter parameter;

  public InteractionEncodingError(String msg, Object theInteraction, OOparameter parameter) {
    super(msg);
    this.theInteraction = theInteraction;
    this.parameter = parameter;
  }

  public InteractionEncodingError(
      String msg, Throwable cause, Object theInteraction, OOparameter parameter) {
    super(msg, cause);
    this.theInteraction = theInteraction;
    this.parameter = parameter;
  }

  public Object getTheInteraction() {
    return theInteraction;
  }

  public OOparameter getParameter() {
    return parameter;
  }
}
