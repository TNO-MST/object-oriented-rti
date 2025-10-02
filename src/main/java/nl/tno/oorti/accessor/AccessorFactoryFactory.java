package nl.tno.oorti.accessor;

import java.lang.invoke.MethodHandles.Lookup;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.tno.oorti.AccessorType;

/**
 * @author bergtwvd
 */
public class AccessorFactoryFactory {

  /**
   * Get the LAMBDA (default) AccessorFactory.
   *
   * @return
   */
  public static AccessorFactory getAccessorFactory() {
    return getAccessorFactory(AccessorType.LAMBDA, null);
  }

  /**
   * Get the AccessorFactory of the given type.
   *
   * @param type
   * @return
   */
  public static AccessorFactory getAccessorFactory(AccessorType type) {
    return getAccessorFactory(type, null);
  }

  /**
   * Get the AccessorFactory of the given type, providing a lookup context for the LAMBDA and
   * METHODHANDLE AccessorFactory type.
   *
   * @param type
   * @param lookup
   * @return
   */
  public static AccessorFactory getAccessorFactory(AccessorType type, Lookup lookup) {
    Logger.getLogger(AccessorFactoryFactory.class.getName())
        .log(Level.FINE, "getAccesorFactory for type={0}", type == null ? "null" : type.name());

    if (type == null) {
      return new LambdaAccessorFactory(lookup);
    } else {
      return switch (type) {
        case LAMBDA -> new LambdaAccessorFactory(lookup);
        case METHOD -> new MethodAccessorFactory();
        case FIELD -> new FieldAccessorFactory();
        case METHODHANDLE -> new MethodHandleAccessorFactory(lookup);
      };
    }
  }
}
