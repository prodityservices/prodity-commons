package io.prodity.commons.spigot.legacy.config.yaml;

import com.google.common.collect.Lists;
import com.google.gson.internal.Primitives;
import io.prodity.commons.spigot.legacy.config.yaml.types.bukkit.YamlSection;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.configuration.ConfigurationSection;

public abstract class AbstractYamlType<T> implements YamlType<T> {

    @Getter
    private final Class<T> type;

    @Getter
    @Accessors(fluent = true)
    private final boolean requiresVerification;

    public AbstractYamlType(Class<T> type, boolean requiresVerification) {
        this.type = Primitives.wrap(type);
        this.requiresVerification = requiresVerification;
    }

    public void verify(ConfigurationSection section, String path) throws YamlException {

    }

    protected abstract T loadInternally(ConfigurationSection section, String path) throws YamlException;

    @Override
    public final T load(ConfigurationSection section, String path) throws YamlException {
        if (this.requiresVerification) {
            this.verify(section, path);
        }
        return this.loadInternally(section, path);
    }

    @Override
    public void loadIfPresent(ConfigurationSection section, String path, Consumer<T> ifPresent) throws YamlException {
        this.loadOptional(section, path).ifPresent(ifPresent);
    }

    @Override
    public Optional<T> loadOptional(ConfigurationSection section, String path) throws YamlException {
        if (!section.contains(path)) {
            return Optional.empty();
        }
        final T value = this.load(section, path);
        return Optional.of(value);
    }

    @Override
    public T loadOrDefault(ConfigurationSection section, String path, T defaultValue) {
        if (!section.contains(path)) {
            return defaultValue;
        }

        try {
            return this.load(section, path);
        } catch (YamlException exception) {
            exception.printStackTrace();
            return defaultValue;
        }
    }

    @Override
    public Collection<T> loadFromSectionKeys(ConfigurationSection section, String path) throws YamlException {
        section = YamlSection.get().load(section, path);
        final List<T> list = Lists.newArrayList();

        for (String key : section.getKeys(false)) {
            final T value = this.load(section, key);
            list.add(value);
        }

        return list;
    }

}