package io.prodity.commons.spigot.legacy.config.yaml;

import com.google.common.collect.Maps;
import io.prodity.commons.spigot.legacy.config.yaml.types.bukkit.YamlSection;
import io.prodity.commons.spigot.legacy.config.yaml.types.enumeration.YamlEnum;
import io.prodity.commons.spigot.legacy.config.yaml.types.enumeration.YamlEnumList;
import io.prodity.commons.spigot.legacy.plugin.Loadable;
import io.prodity.commons.spigot.legacy.tryto.Try;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class YamlConfig implements Loadable {

    public static String getFullPath(ConfigurationSection section, String path) {
        final StringBuilder fullPath = new StringBuilder(section.getCurrentPath());
        if (fullPath.length() > 0) {
            fullPath.append(".");
        }
        fullPath.append(path);
        return fullPath.toString();
    }

    private final Plugin plugin;
    private final File file;
    private FileConfiguration config;

    public YamlConfig(Plugin plugin, String fileName) {
        this(plugin, plugin.getDataFolder(), fileName);
    }

    public YamlConfig(Plugin plugin, File directory, String fileName) {
        this.plugin = plugin;
        this.file = new File(directory, fileName);
    }

    public YamlConfig(File file) {
        this.file = file;
        this.plugin = null;
    }

    @Override
    public void load() {
        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();
            Try.run(this.file::createNewFile);

            if (this.plugin != null) {
                try (
                    InputStream input = this.plugin.getResource(this.file.getName())
                ) {
                    Files.copy(input, this.file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException exception) {
                    throw new RuntimeException(exception);
                }
            }
        }

        this.config = YamlConfiguration.loadConfiguration(this.file);
        if (!this.config.isConfigurationSection("variables")) {
            return;
        }

        final ConfigurationSection variablesSection = this.config.getConfigurationSection("variables");
        String configAsString = this.config.saveToString();

        for (String key : variablesSection.getKeys(false)) {
            final Object value = variablesSection.get(key);
            final String valueAsString = value == null ? null : value.toString();
            configAsString = configAsString.replace(key, valueAsString);
        }

        this.config = new YamlConfiguration();
        Try.to(this.config::loadFromString).accept(configAsString);
    }

    public FileConfiguration get() {
        return this.config;
    }

    public ConfigurationSection get(String path) throws YamlException {
        return this.load(path, YamlSection.get());
    }

    public <T> T load(String path, YamlType<T> type) throws YamlException {
        return this.load(this.get(), path, type);
    }

    public <T> T load(ConfigurationSection section, String path, YamlType<T> type) throws YamlException {
        return type.load(section, path);
    }

    public <T> Optional<T> loadOptional(String path, YamlType<T> type) throws YamlException {
        return this.loadOptional(this.get(), path, type);
    }

    public <T> Optional<T> loadOptional(ConfigurationSection section, String path, YamlType<T> type) throws YamlException {
        return type.loadOptional(section, path);
    }

    public <E extends Enum<E>> E loadEnum(String path, Class<E> enumClazz) throws YamlException {
        return this.loadEnum(this.get(), path, enumClazz);
    }

    public <E extends Enum<E>> E loadEnum(ConfigurationSection section, String path, Class<E> enumClazz) throws YamlException {
        return YamlEnum.get(enumClazz).load(section, path);
    }

    public <E extends Enum<E>> Optional<E> loadEnumOptional(String path, Class<E> enumClazz) throws YamlException {
        return this.loadEnumOptional(this.get(), path, enumClazz);
    }

    public <E extends Enum<E>> Optional<E> loadEnumOptional(ConfigurationSection section, String path, Class<E> enumClazz)
        throws YamlException {
        return YamlEnum.get(enumClazz).loadOptional(section, path);
    }

    public <E extends Enum<E>> List<E> loadEnumList(String path, Class<E> enumClazz) throws YamlException {
        return this.loadEnumList(this.get(), path, enumClazz);
    }

    public <E extends Enum<E>> List<E> loadEnumList(ConfigurationSection section, String path, Class<E> enumClazz)
        throws YamlException {
        return YamlEnumList.get(enumClazz).load(section, path);
    }

    public <T> T loadOrDefault(String path, YamlType<T> type, T defaultValue) throws YamlException {
        return this.loadOrDefault(this.get(), path, type, defaultValue);
    }

    public <T> T loadOrDefault(ConfigurationSection section, String path, YamlType<T> type, T defaultValue) throws YamlException {
        return type.loadOrDefault(section, path, defaultValue);
    }

    public <E extends Enum<E>> E loadEnumOrDefault(String path, Class<E> enumClazz, E defaultValue) {
        return this.loadEnumOrDefault(this.get(), path, enumClazz, defaultValue);
    }

    public <E extends Enum<E>> E loadEnumOrDefault(ConfigurationSection section, String path, Class<E> enumClazz, E defaultValue) {
        return YamlEnum.get(enumClazz).loadOrDefault(section, path, defaultValue);
    }

    public <E extends Enum<E>> List<E> loadEnumListOrDefault(String path, Class<E> enumClazz, List<E> defaultValue) {
        return this.loadEnumListOrDefault(this.get(), path, enumClazz, defaultValue);
    }

    public <E extends Enum<E>> List<E> loadEnumListOrDefault(ConfigurationSection section, String path, Class<E> enumClazz,
        List<E> defaultValue) {
        return YamlEnumList.get(enumClazz).loadOrDefault(section, path, defaultValue);
    }

    public <T> Map<String, T> loadFromSectionKeys(ConfigurationSection section, String path, YamlType<T> type)
        throws YamlException {
        section = this.load(section, path, YamlSection.get());
        return this.loadFromSectionKeys(section, type);
    }

    public <T> Map<String, T> loadFromSectionKeys(String path, YamlType<T> type) throws YamlException {
        return this.loadFromSectionKeys(this.get(path), type);
    }

    public <T> Map<String, T> loadFromSectionKeys(ConfigurationSection section, YamlType<T> type) throws YamlException {
        final Map<String, T> map = Maps.newHashMap();
        for (String key : section.getKeys(false)) {
            final T value = this.load(section, key, type);
            map.put(key, value);
        }
        return map;
    }

}