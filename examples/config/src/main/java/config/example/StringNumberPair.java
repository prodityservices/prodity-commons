package config.example;

import io.prodity.commons.config.annotate.deserialize.ConfigDeserializable;
import io.prodity.commons.config.annotate.inject.ConfigNodeKey;
import io.prodity.commons.config.annotate.inject.ConfigNodeValue;

@ConfigDeserializable
public class StringNumberPair {

    // @ConfigNodeKey can be applied to elements inside of an object annotated with @ConfigDeserializable
    // It declares that the injected value should be the key of the node that is used when resolving the object (in this case being StringNumberPair)
    @ConfigNodeKey
    private int key;

    // @ConfigNodeValue has the same properties as @ConfigNodeKey, however instead of the key being injected, it is the value of the node.
    @ConfigNodeValue
    private String value;

    public int getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.key + ": " + this.value;
    }

}