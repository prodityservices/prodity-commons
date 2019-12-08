package io.prodity.commons.effect.message.color;

import org.jvnet.hk2.annotations.Contract;

@FunctionalInterface
@Contract
public interface Colorizer {

    String translateColors(String string);

}