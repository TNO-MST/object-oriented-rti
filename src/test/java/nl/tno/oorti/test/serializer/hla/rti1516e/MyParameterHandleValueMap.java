package nl.tno.oorti.test.serializer.hla.rti1516e;

import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.encoding.ByteWrapper;
import java.util.HashMap;

/**
 *
 * @author bergtwvd
 */
public class MyParameterHandleValueMap extends HashMap<ParameterHandle, byte[]> implements ParameterHandleValueMap {

    MyParameterHandleValueMap(int capacity) {
        super(capacity);
    }

    @Override
    public ByteWrapper getValueReference(ParameterHandle key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ByteWrapper getValueReference(ParameterHandle key, ByteWrapper byteWrapper) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
