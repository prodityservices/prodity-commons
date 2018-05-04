package io.prodity.commons.config.load;

import com.google.common.base.Preconditions;
import io.prodity.commons.config.annotate.Config;
import io.prodity.commons.config.annotate.ConfigInjectable;
import io.prodity.commons.config.annotate.ConfigValue;
import io.prodity.commons.config.except.ConfigLoadException;
import io.prodity.commons.file.FileUtil;
import io.prodity.commons.plugin.ProdityPlugin;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import org.jvnet.hk2.annotations.Service;
import org.yaml.snakeyaml.Yaml;

@Service
public class ConfigLoader {

    private static final String LITERAL_PERIOD_REGEX = Pattern.quote(".");

    private static void verifyAnnotationPresent(@Nonnull Class<?> clazz) throws IllegalArgumentException {
        if (!clazz.isAnnotationPresent(Config.class)) {
            throw new IllegalArgumentException("@Config anotation is not present on class " + clazz.getName());
        }
    }

    private static void verifyAnnotationValues(@Nonnull Config annotation) {
        final String fileName = annotation.fileName();
        Preconditions.checkNotNull(fileName, "annotation.fileName()");
        Preconditions.checkState(!fileName.isEmpty(), "annotation.fileName() is empty");
    }

    private final Yaml yaml = new Yaml();

    @Inject
    private ProdityPlugin plugin;

    @Nonnull
    public <T> T load(@Nonnull Class<T> configClass) throws Exception {
        Preconditions.checkNotNull(configClass, "configClass");
        ConfigLoader.verifyAnnotationPresent(configClass);

        final Config configAnnotation = configClass.getAnnotation(Config.class);
        ConfigLoader.verifyAnnotationValues(configAnnotation);

        final ConfigLoadData<T> data = new ConfigLoadData<>(configClass, configAnnotation);

        this.resolveFileFromAnnotation(data);

        final File file = data.getFile();
        Preconditions.checkNotNull(file, "data.getFile()");

        final boolean created = this.createFileIfNotExists(file);
        if (created) {
            this.saveDefaultConfig(data);
        }

        final String fileContents = FileUtil.readFileContents(file);

        final Map<?, ?> rawValues = this.yaml.load(fileContents);
        data.setRawValues(rawValues);

        final T object = configClass.newInstance();
        data.setConfigObject(object);

        this.resolveApplicableFields(data);
        this.resolveApplicableMethods(data);

        return null;//TODO
    }

    private void resolveFileFromAnnotation(@Nonnull ConfigLoadData<?> data) {
        Preconditions.checkNotNull(data, "data");

        final Config annotation = data.getAnnotation();
        final String filePath = annotation.fileDirectory();
        final File directory;
        if (filePath.isEmpty()) {
            directory = this.plugin.getDataFolder();
        } else {
            directory = new File(this.plugin.getDataFolder(), filePath);
        }

        final String fileName = annotation.fileName();
        final File file = new File(directory, fileName);
        data.setFile(file);
    }

    private boolean createFileIfNotExists(@Nonnull File file) throws IOException {
        Preconditions.checkNotNull(file, "file");
        if (file.exists()) {
            return false;
        }
        file.getParentFile().mkdirs();
        return file.createNewFile();
    }

    private void saveDefaultConfig(@Nonnull ConfigLoadData<?> data) throws IOException {
        Preconditions.checkNotNull(data, "data");
        Preconditions.checkNotNull(data.getFile(), "data.getFile()");

        this.resolveInternalPath(data);
        final String internalPath = data.getInternalPath();
        final InputStream inputStream = this.plugin.getResource(internalPath);

        if (inputStream == null) {
            throw new NullPointerException("no internal plugin file found at internalPath=" + internalPath);
        }

        final Path filePath = data.getFile().toPath();
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    private void resolveInternalPath(@Nonnull ConfigLoadData<?> data) {
        Preconditions.checkNotNull(data, "data");

        final Config annotation = data.getAnnotation();
        final String internalPath = annotation.internalPath();
        final String fileName = annotation.fileName();
        if (internalPath.isEmpty()) {
            data.setInternalPath(fileName);
        } else if (internalPath.endsWith("/") || internalPath.endsWith(File.separator)) {
            data.setInternalPath(internalPath + fileName);
        } else {
            data.setInternalPath(internalPath + File.separator + fileName);
        }
    }

    private void resolveApplicableFields(@Nonnull ConfigLoadData<?> data) {
        final Class<?> clazz = data.getClass();
        final List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
            .filter((field) -> field.isAnnotationPresent(ConfigInjectable.class))
            .collect(Collectors.toList());
        data.setApplicableFields(fields);
    }

    private void resolveApplicableMethods(@Nonnull ConfigLoadData<?> data) {
        final Class<?> clazz = data.getClass();
        final List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
            .filter((method) -> method.isAnnotationPresent(ConfigInjectable.class))
            .collect(Collectors.toList());
        data.setApplicableMethods(methods);
    }

    private void loadFields(@Nonnull ConfigLoadData<?> data) {
        Preconditions.checkNotNull(data, "data");
        Preconditions.checkNotNull(data.getApplicableFields(), "data.getApplicableFields()");

        for (Field field : data.getApplicableFields()) {
            //TODO
            //this.loadField(data.getRawValues(), field);
        }
    }

    private void loadField(@Nonnull Map<?, ?> rawValues, Field field) throws ConfigLoadException {
        if (!field.isAnnotationPresent(ConfigValue.class)) {
            throw new ConfigLoadException("");
        }
    }

    private Optional<Object> getMapValueFromPath(@Nonnull Map<?, ?> rawValues, String path) throws ConfigLoadException {
        final String[] keys = path.split(ConfigLoader.LITERAL_PERIOD_REGEX);

        Map<?, ?> currentMap = rawValues;
        for (int i = 0; i < keys.length; i++) {
            final String key = keys[i];
            if (!currentMap.containsKey(key)) {
                return Optional.empty();
            }
            final Object value = currentMap.get(key);
            if (i == keys.length - 1 || !(value instanceof Map)) {
                break;
            }
            currentMap = (Map<?, ?>) value;
        }

        return Optional.empty();
    }

}