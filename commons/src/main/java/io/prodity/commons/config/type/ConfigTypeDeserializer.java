package io.prodity.commons.config.type;

@FunctionalInterface
public interface ConfigTypeDeserializer<T> {

    T deserialize() throws Exception;

}