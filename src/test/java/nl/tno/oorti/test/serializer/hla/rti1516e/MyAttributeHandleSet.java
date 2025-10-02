package nl.tno.oorti.test.serializer.hla.rti1516e;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import java.util.HashSet;

/**
 *
 * @author bergtwvd
 */
public class MyAttributeHandleSet extends HashSet<AttributeHandle> implements AttributeHandleSet {

    @Override
    public AttributeHandleSet clone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}
