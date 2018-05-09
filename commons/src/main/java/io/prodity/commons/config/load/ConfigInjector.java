package io.prodity.commons.config.load;

import com.google.common.base.Preconditions;
import io.prodity.commons.config.annotate.inject.Config;
import io.prodity.commons.config.except.ConfigLoadException;
import io.prodity.commons.config.load.element.ConfigElementResolver;
import io.prodity.commons.config.load.element.ConfigFieldElement;
import io.prodity.commons.config.load.method.ConfigMethodResolver;
import io.prodity.commons.config.load.method.InjectableConfigMethod;
import io.prodity.commons.config.load.method.ListenerConfigMethod;
import io.prodity.commons.config.load.method.ListenerType;
import io.prodity.commons.except.tryto.Try;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.jvnet.hk2.annotations.Service;
import org.yaml.snakeyaml.DumperOptions;

@Service
public class ConfigInjector {

    public static final String VARIABLES_KEY = "variables";

    @Inject
    private ProdityPlugin plugin;

    @Nonnull
    public <T> T inject(@Nonnull Class<T> configClass) throws ConfigLoadException {
        Preconditions.checkNotNull(configClass, "configClass");

        if (!configClass.isAnnotationPresent(Config.class)) {
            throw new IllegalArgumentException("class=" + configClass.getName() + " does not have the @Config annotation");
        }

        final Config config = configClass.getAnnotation(Config.class);
        return Try.mapExceptionTo(() -> this.injectChecked(configClass), ConfigLoadException.newMapper(config)).get();
    }

    @Nonnull
    private <T> T injectChecked(@Nonnull Class<T> configClass) throws Throwable {
        Preconditions.checkNotNull(configClass, "configClass");

        final ConfigClass<T> configType = this.createConfigType(configClass);
        final ConfigLoadContext<T> context = this.createContext(configType);

        final File file = context.getFile();
        final boolean created = this.createFileIfNotExists(file);

        if (created) {
            this.saveDefaultConfig(context);
        }

        final ConfigurationNode masterNode = this.loadAndReplaceVariables(context);
        context.setMasterNode(masterNode);

        final T object = context.getConfigObject();
        configType.invokeListeners(object, ListenerType.PRE_LOAD);

        //TODO load actual fields / methods and inject.

        configType.invokeListeners(object, ListenerType.POST_LOAD);

        return object;
    }

    @Nonnull
    private ConfigurationNode loadAndReplaceVariables(@Nonnull ConfigLoadContext<?> context) throws IOException, ConfigLoadException {
        Preconditions.checkNotNull(context, "context");

        ConfigurationNode masterNode = this.loadFromPath(context.getFile().toPath());

        final ConfigurationNode variablesNode = masterNode.getNode(ConfigInjector.VARIABLES_KEY);
        if (variablesNode.isVirtual()) {
            return masterNode;
        }

        if (!variablesNode.hasMapChildren()) {
            throw new ConfigLoadException(context, "variables node is not in the form of a Map");
        }

        masterNode.removeChild(variablesNode.getKey());
        String contents = masterNode.toString();

        for (Map.Entry<Object, ? extends ConfigurationNode> entry : variablesNode.getChildrenMap().entrySet()) {
            final String key = entry.getKey().toString();
            final Object value = entry.getValue().getValue();
            if (value == null) {
                throw new ConfigLoadException(context, "variable key=" + key + " does not have a valid value assigned");
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

    @Nonnull
    private <T> ConfigClass<T> createConfigType(@Nonnull Class<T> configClass) {
        Preconditions.checkNotNull(configClass, "configClass");

        final ConfigClass.Builder<T> builder = ConfigClass.builder(configClass);

        final List<InjectableConfigMethod> injectableMethods = ConfigMethodResolver.resolveInjectableMethods(configClass);
        builder.setInjectableMethods(injectableMethods);

        final List<ListenerConfigMethod> listenerMethods = ConfigMethodResolver.resolveListenerMethods(configClass);
        builder.setListenerMethods(listenerMethods);

        final List<ConfigFieldElement> fieldElements = ConfigElementResolver.resolveFieldElements(configClass);
        builder.setInjectableFields(fieldElements);

        return builder.build();
    }

    @Nonnull
    private <T> ConfigLoadContext<T> createContext(@Nonnull ConfigClass<T> type) throws IllegalAccessException, InstantiationException {
        Preconditions.checkNotNull(type, "type");

        final ConfigLoadContext.Builder<T> builder = ConfigLoadContext.builder();
        builder.setConfigType(type);

        final Config annotation = type.getConfig();

        final File file = this.resolveFile(annotation);
        builder.setFile(file);

        final String internalPath = this.resolveInternalPath(annotation);
        builder.setInternalPath(internalPath);

        final T object = type.getTypeClass().newInstance();
        builder.setConfigObject(object);

        return builder.build();
    }

    @Nonnull
    private File resolveFile(@Nonnull Config annotation) {
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

    @Nonnull
    private String resolveInternalPath(@Nonnull Config annotation) {
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

    private boolean createFileIfNotExists(@Nonnull File file) throws IOException {
        Preconditions.checkNotNull(file, "file");
        if (file.exists()) {
            return false;
        }
        file.getParentFile().mkdirs();
        return file.createNewFile();
    }

    private void saveDefaultConfig(@Nonnull ConfigLoadContext<?> data) throws IOException {
        Preconditions.checkNotNull(data, "data");

        final String internalPath = data.getInternalPath();
        final InputStream inputStream = this.plugin.getResource(internalPath);

        if (inputStream == null) {
            throw new NullPointerException("no internal plugin file found at internalPath=" + internalPath);
        }

        final Path filePath = data.getFile().toPath();
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    }

}