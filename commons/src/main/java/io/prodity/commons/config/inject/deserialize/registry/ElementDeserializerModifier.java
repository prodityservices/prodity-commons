package io.prodity.commons.config.inject.deserialize.registry;

import org.jvnet.hk2.annotations.Contract;

@Contract
public interface ElementDeserializerModifier {

    void modify(ElementDeserializerRegistry registry);

}