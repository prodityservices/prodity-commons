package io.prodity.commons.spigot.legacy.config.yaml;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import org.bukkit.configuration.ConfigurationSection;

public interface YamlType<T> {

    T load(ConfigurationSection section, String path) throws YamlException;

    void loadIfPresent(ConfigurationSection section, String path, Consumer<T> ifPresent) throws YamlException;

    T loadOrDefault(ConfigurationSection section, String path, T defaultValue) throws YamlException;

    Optional<T> loadOptional(ConfigurationSection section, String path) throws YamlException;

    Collection<T> loadFromSectionKeys(ConfigurationSection section, String path) throws YamlException;

    Class<T> getType();

}