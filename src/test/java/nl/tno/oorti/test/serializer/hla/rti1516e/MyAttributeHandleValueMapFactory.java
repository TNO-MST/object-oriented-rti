package nl.tno.oorti.test.serializer.hla.rti1516e;

import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.AttributeHandleValueMapFactory;

/**
 *
 * @author bergtwvd
 */
public class MyAttributeHandleValueMapFactory implements AttributeHandleValueMapFactory {

    @Override
    public AttributeHandleValueMap create(int capacity) {
        return new MyAttributeHandleValueMap(capacity);
    }
    
}
