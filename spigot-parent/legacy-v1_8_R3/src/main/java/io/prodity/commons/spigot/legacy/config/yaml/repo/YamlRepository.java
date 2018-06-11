package io.prodity.commons.spigot.legacy.config.yaml.repo;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import io.prodity.commons.spigot.legacy.config.yaml.YamlConfig;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.plugin.Loadable;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public abstract class YamlRepository<T> implements Loadable {

    private final YamlConfig yamlConfig;
    @Getter
    private final Multimap<String, Function<T, T>> loadModifiers;
    @Getter
    private ImmutableMap<String, T> artifacts;

    protected YamlRepository(Plugin plugin, String fileName) {
        this(new YamlConfig(plugin, fileName));
    }

    protected YamlRepository(File file) {
        this(new YamlConfig(file));
    }

    protected YamlRepository(YamlConfig yamlConfig) {
        this.yamlConfig = yamlConfig;
        this.artifacts = ImmutableMap.of();
        this.loadModifiers = MultimapBuilder.hashKeys().arrayListValues().build();
    }

    @Override
    public void load() throws Throwable {
        this.yamlConfig.load();
        final ConfigurationSection section = this.yamlConfig.get();
        final ImmutableMap.Builder<String, T> mapBuilder = ImmutableMap.builder();

        this.loadSection(section, mapBuilder);
        this.artifacts = mapBuilder.build();
    }

    public YamlRepository<T> addLoadModifier(String key, Function<T, T> modifier) {
        this.loadModifiers.put(key, modifier);
        return this;
    }

    protected abstract boolean shouldLoad(ConfigurationSection section, String path);

    protected abstract T load(ConfigurationSection section, String path) throws YamlException;

    protected void loadSection(ConfigurationSection section, ImmutableMap.Builder<String, T> mapBuilder)
        throws YamlException {
        for (String key : section.getKeys(false)) {
            final String keyPath = this.appendPath(section.getCurrentPath(), key);

            if (this.shouldLoad(section, key)) {
                T value = this.load(section, key);
                final Collection<Function<T, T>> modifiers = this.loadModifiers.get(keyPath);
                if (!modifiers.isEmpty()) {
                    for (Function<T, T> modifier : modifiers) {
                        value = modifier.apply(value);
                    }
                }

                mapBuilder.put(keyPath, value);
                continue;
            }

            if (section.isConfigurationSection(key)) {
                this.loadSection(section.getConfigurationSection(key), mapBuilder);
                continue;
            }

            throw new YamlException(section, key, "path is not a section & couldn't be loaded");
        }
    }

    protected String appendPath(String basePath, String currentPath) {
        return basePath.equalsIgnoreCase("") ? currentPath : basePath + "." + currentPath;
    }

    public boolean contains(String key) {
        return this.artifacts.containsKey(key);
    }

    public T get(String key) {
        return this.artifacts.get(key);
    }

    public T getOrDefault(String key, T defaultValue) {
        return this.artifacts.getOrDefault(key, defaultValue);
    }

    public List<T> getList(String... keys) {
        final List<String> keyList = Lists.newArrayList(keys);
        return this.getList(keyList);
    }

    public List<T> getList(Iterable<String> keys) {
        final List<T> list = Lists.newArrayList();
        for (String key : keys) {
            final T value = this.get(key);
            if (value != null) {
                list.add(value);
            }
        }
        return list;
    }

    public Optional<T> getSafely(String key) {
        return Optional.ofNullable(this.artifacts.get(key));
    }

}