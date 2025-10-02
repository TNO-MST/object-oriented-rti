package nl.tno.oorti;

/**
 * The AccessorType enumeration defines the available access methods for getting and setting a Java
 * Bean property.
 *
 * <p>The selected access method has an impact on the performance of the get-set operation.
 * Generally, the Lambda call site access provides the best performance.
 *
 * @author bergtwvd
 */
public enum AccessorType {
  LAMBDA, // Java Lambda callsite
  METHOD, // Java method reflection
  FIELD, // Java field reflection
  METHODHANDLE; // Java method handles
}
