package nl.tno.oorti.impl.serializer;

import java.util.Set;
import nl.tno.oorti.OOparameter;

/**
 * @author bergtwvd
 */
public class DeserializedInteractionData {

  private Object theInteraction;
  private Set<OOparameter> parameterSet;

  public DeserializedInteractionData() {}

  public DeserializedInteractionData(Object theInteraction, Set<OOparameter> parameterSet) {
    this.theInteraction = theInteraction;
    this.parameterSet = parameterSet;
  }

  public void set(Object theInteraction, Set<OOparameter> parameterSet) {
    this.theInteraction = theInteraction;
    this.parameterSet = parameterSet;
  }

  public Object getTheInteraction() {
    return theInteraction;
  }

  public Set<OOparameter> getParameterSet() {
    return parameterSet;
  }
}
