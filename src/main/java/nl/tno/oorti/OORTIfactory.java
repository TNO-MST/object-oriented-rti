package nl.tno.oorti;

import nl.tno.oorti.impl.OORTIambassadorImpl;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.exceptions.RTIinternalError;

/**
 * Factory class for creating an OORTI ambassador.
 *
 * @author bergtwvd
 */
public class OORTIfactory {

  private final OOproperties properties;

  public OORTIfactory(OOproperties properties) {
    this.properties = properties;
  }

  public OORTIfactory() {
    this(new OOproperties());
  }

  public OORTIambassador getRtiAmbassador() throws RTIinternalError {
    return new OORTIambassadorImpl(
        RtiFactoryFactory.getRtiFactory().getRtiAmbassador(),
        new DefaultOOobjectFactory(),
        properties);
  }

  public OORTIambassador getRtiAmbassador(RTIambassador rtiamb) throws RTIinternalError {
    return new OORTIambassadorImpl(rtiamb, new DefaultOOobjectFactory(), properties);
  }

  public OORTIambassador getRtiAmbassador(RTIambassador rtiamb, OOobjectFactory objectFactory)
      throws RTIinternalError {
    return new OORTIambassadorImpl(rtiamb, objectFactory, properties);
  }
}
