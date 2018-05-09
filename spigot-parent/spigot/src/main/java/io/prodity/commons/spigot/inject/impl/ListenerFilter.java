package io.prodity.commons.spigot.inject.impl;

import org.jvnet.hk2.annotations.Contract;

@Contract
public interface ListenerFilter {

    boolean shouldRegister(Object object);
}
