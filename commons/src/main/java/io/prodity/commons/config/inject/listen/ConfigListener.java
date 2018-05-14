package io.prodity.commons.config.inject.listen;

import java.lang.reflect.InvocationTargetException;

@FunctionalInterface
public interface ConfigListener {

    void callListeners(ListenerType type) throws InvocationTargetException, IllegalAccessException;

}