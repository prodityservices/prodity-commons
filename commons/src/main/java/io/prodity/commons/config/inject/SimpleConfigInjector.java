package io.prodity.commons.config.inject;

import com.google.common.base.Preconditions;
import io.prodity.commons.config.annotate.inject.Config;
import io.prodity.commons.config.inject.deserialize.ElementResolver;
import io.prodity.commons.config.inject.except.ConfigInjectException;
import io.prodity.commons.except.tryto.Try;
import io.prodity.commons.inject.Export;
import io.prodity.commons.plugin.ProdityPlugin;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.jvnet.hk2.annotations.Service;
import org.yaml.snakeyaml.DumperOptions;

@Service
@Export
public class SimpleConfigInjector implements ConfigInjector {

    public static final String VARIABLES_KEY = "variables";

    @Inject
    private ProdityPlugin plugin;

    @Inject
    private ElementResolver elementResolver;

    @Override
    public <T> T inject(Class<T> configClass) throws ConfigInjectException {
        Preconditions.checkNotNull(configClass, "configClass");

        if (!configClass.isAnnotationPresent(Config.class)) {
            throw new IllegalArgumentException("class=" + configClass.getName() + " does not have the @Config annotation");
        }

        final Config config = configClass.getAnnotation(Config.class);
        return Try.mapExceptionTo(() -> this.injectChecked(configClass, config), ConfigInjectException.newMapper(config)).get();
    }

    private <T> T injectChecked(Class<T> configClass, Config config) throws Throwable {
        Preconditions.checkNotNull(configClass, "configClass");

        final ConfigFile configFile = this.createConfigFile(config);
        final boolean created = this.createFileIfNotExists(configFile.getFile());

        if (created) {
            this.saveDefaultConfig(configFile);
        }

        final ConfigurationNode masterNode = this.loadAndReplaceVariables(configFile);

        final ConfigObject<T> configObject = ConfigObject.of(configClass);

        configObject.inject(this.elementResolver, masterNode);

        final T instance = configObject.getObjectInstance();
        if (instance == null) {
            throw new IllegalStateException(
                "configObject=" + configObject + "'s internal object instance is somehow null... this isn't right! O_o");
        }

        return instance;
    }

    private ConfigurationNode loadAndReplaceVariables(ConfigFile configFile) throws IOException, ConfigInjectException {
        Preconditions.checkNotNull(configFile, "configFile");

        ConfigurationNode masterNode = this.loadFromPath(configFile.getFile().toPath());

        final ConfigurationNode variablesNode = masterNode.getNode(SimpleConfigInjector.VARIABLES_KEY);
        if (variablesNode.isVirtual()) {
            return masterNode;
        }

        if (!variablesNode.hasMapChildren()) {
            throw new ConfigInjectException(configFile, "variables node is not in the form of a Map");
        }

        masterNode.removeChild(variablesNode.getKey());
        String contents = masterNode.toString();

        for (Map.Entry<Object, ? extends ConfigurationNode> entry : variablesNode.getChildrenMap().entrySet()) {
            final String key = entry.getKey().toString();
            final Object value = entry.getValue().getValue();
            if (value == null) {
                throw new ConfigInjectException(configFile, "variable key=" + key + " does not have a valid deserialize assigned");
            }

            contents = contents.replace(key, value.toString());
        }

        return this.loadFromString(contents);
    }

    private ConfigurationNode loadFromPath(Path path) throws IOException {
        final YAMLConfigurationLoader loader = this.createConfigLoader(() -> Files.newBufferedReader(path, StandardCharsets.UTF_8));
        return loader.load();
    }

    private ConfigurationNode loadFromString(String string) throws IOException {
        final YAMLConfigurationLoader loader = this.createConfigLoader(() -> {
            final StringReader stringReader = new StringReader(string);
            return new BufferedReader(stringReader);
        });
        return loader.load();
    }

    private YAMLConfigurationLoader createConfigLoader(Callable<BufferedReader> readerSupplier) {
        return YAMLConfigurationLoader.builder()
            .setFlowStyle(DumperOptions.FlowStyle.BLOCK)
            .setIndent(2)
            .setSource(readerSupplier)
            .build();
    }

    private ConfigFile createConfigFile(Config config) {
        Preconditions.checkNotNull(config, "config");

        final File file = this.resolveFile(config);
        final String internalPath = this.resolveInternalPath(config);

        final ConfigFile configFile = new ConfigFile(config, file, internalPath);
        return configFile;
    }

    private File resolveFile(Config annotation) {
        Preconditions.checkNotNull(annotation, "annotation");

        final String filePath = annotation.fileDirectory();
        final File directory;
        if (filePath.isEmpty()) {
            directory = this.plugin.getDataFolder();
        } else {
            directory = new File(this.plugin.getDataFolder(), filePath);
        }

        final String fileName = annotation.fileName();
        final File file = new File(directory, fileName);
        return file;
    }

    private String resolveInternalPath(Config annotation) {
        Preconditions.checkNotNull(annotation, "annotation");

        final String internalPath = annotation.internalPath();
        final String fileName = annotation.fileName();
        if (internalPath.isEmpty()) {
            return fileName;
        } else if (internalPath.endsWith("/") || internalPath.endsWith(File.separator)) {
            return internalPath + fileName;
        } else {
            return internalPath + File.separator + fileName;
        }
    }

    private boolean createFileIfNotExists(File file) throws IOException {
        Preconditions.checkNotNull(file, "file");
        if (file.exists()) {
            return false;
        }
        file.getParentFile().mkdirs();
        return file.createNewFile();
    }

    private void saveDefaultConfig(ConfigFile configFile) throws IOException {
        Preconditions.checkNotNull(configFile, "configFile");

        final String internalPath = configFile.getInternalPath();
        final InputStream inputStream = this.plugin.getResource(internalPath);

        if (inputStream == null) {
            throw new NullPointerException("no internal plugin file found at internalPath=" + internalPath);
        }

        final Path filePath = configFile.getFile().toPath();
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    }

}