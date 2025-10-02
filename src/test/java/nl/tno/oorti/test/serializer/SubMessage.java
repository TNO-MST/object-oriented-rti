package nl.tno.oorti.test.serializer;

/**
 *
 * @author bergtwvd
 */
public class SubMessage extends Message {
    
    private int someAdditionalProperty;

    public int getSomeAdditionalProperty() {
        return someAdditionalProperty;
    }

    public void setSomeAdditionalProperty(int someAdditionalProperty) {
        this.someAdditionalProperty = someAdditionalProperty;
    }
    
}
