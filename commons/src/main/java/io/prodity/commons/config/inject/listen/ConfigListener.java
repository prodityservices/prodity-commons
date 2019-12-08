package io.prodity.commons.config.inject.listen;

import io.prodity.commons.config.inject.ConfigInjectionContext;
import java.lang.reflect.InvocationTargetException;

@FunctionalInterface
public interface ConfigListener {

    void callListeners(ListenerType type, ConfigInjectionContext context) throws InvocationTargetException, IllegalAccessException;

}