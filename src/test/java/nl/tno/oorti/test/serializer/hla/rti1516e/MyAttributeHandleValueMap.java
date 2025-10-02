package nl.tno.oorti.test.serializer.hla.rti1516e;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.encoding.ByteWrapper;
import java.util.HashMap;

/**
 *
 * @author bergtwvd
 */
public class MyAttributeHandleValueMap extends HashMap<AttributeHandle, byte[]> implements AttributeHandleValueMap  {

    MyAttributeHandleValueMap(int capacity) {
        super(capacity);
    }

    @Override
    public ByteWrapper getValueReference(AttributeHandle key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ByteWrapper getValueReference(AttributeHandle key, ByteWrapper byteWrapper) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
