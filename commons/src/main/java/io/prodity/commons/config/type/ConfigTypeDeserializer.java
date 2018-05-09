package io.prodity.commons.config.type;

@FunctionalInterface
public interface ConfigTypeDeserializer<T> {

    //TODO add proper arguments
    T deserialize() throws Exception;

}