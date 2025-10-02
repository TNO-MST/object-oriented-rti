package nl.tno.oorti.test.serializer.hla.rti1516e;

import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.ParameterHandleValueMapFactory;

/**
 *
 * @author bergtwvd
 */
public class MyParameterHandleValueMapFactory implements ParameterHandleValueMapFactory {

    @Override
    public ParameterHandleValueMap create(int capacity) {

        return new MyParameterHandleValueMap(capacity);
    }

}
