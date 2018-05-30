package io.prodity.commons.inject.impl;

import java.util.concurrent.Executor;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface Platform {

    boolean isListener(Object instance);

    void registerListener(Object instance);

    void unregisterListener(Object instance);

    boolean hasPlugin(String name);

    boolean isEnabled();

    Executor getAsyncExecutor();
}