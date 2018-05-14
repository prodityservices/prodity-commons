package io.prodity.commons.config.inject;

import io.prodity.commons.config.inject.deserialize.ConfigValueResolver;
import io.prodity.commons.config.inject.except.ConfigInjectException;
import ninja.leaping.configurate.ConfigurationNode;

@FunctionalInterface
public interface ConfigInjectable {

    void inject(ConfigFile configFile, ConfigValueResolver valueResolver, ConfigurationNode node) throws ConfigInjectException;

}