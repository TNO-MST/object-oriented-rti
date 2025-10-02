package nl.tno.oorti.test.serializer.hla.rti1516e;

import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleSetFactory;

/**
 *
 * @author bergtwvd
 */
public class MyAttributeHandleSetFactory implements AttributeHandleSetFactory {

    @Override
    public AttributeHandleSet create() {
        return new MyAttributeHandleSet();
    }
    
}
