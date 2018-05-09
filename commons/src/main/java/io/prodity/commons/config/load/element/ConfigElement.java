package io.prodity.commons.config.load.element;

import com.google.common.base.Preconditions;
import io.prodity.commons.config.annotate.inject.ConfigIgnore;
import io.prodity.commons.config.annotate.inject.ConfigPath;
import io.prodity.commons.config.annotate.inject.Required;
import io.prodity.commons.config.annotate.modify.Colorize;
import io.prodity.commons.config.annotate.modify.ConfigDefault;
import io.prodity.commons.config.annotate.modify.LoadFromRepository;
import io.prodity.commons.reflect.NamedAnnotatedElement;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

public class ConfigElement {

    private static String resolveRepositoryName(AnnotatedElement element) {
        return Optional.ofNullable(element.getAnnotation(LoadFromRepository.class))
            .map(LoadFromRepository::value)
            .orElse(null);
    }

    private static Class<? extends Supplier<?>> resolveDefaultSupplier(AnnotatedElement element) {
        return Optional.ofNullable(element.getAnnotation(ConfigDefault.class))
            .map(ConfigDefault::value)
            .orElse(null);
    }

    private static String resolvePath(NamedAnnotatedElement element) {
        return Optional.ofNullable(element.getAnnotation(ConfigPath.class))
            .map(ConfigPath::value)
            .orElseGet(element::getName);
    }

    private static boolean resolveRequired(NamedAnnotatedElement element) {
        return Optional.ofNullable(element.getAnnotation(Required.class))
            .map(Required::value)
            .orElse(Required.Default.DEFAULT_VALUE);
    }

    private static boolean resolveColorized(NamedAnnotatedElement element) {
        return Optional.ofNullable(element.getAnnotation(Colorize.class))
            .map(Colorize::value)
            .orElse(false);
    }

    public static boolean isElement(AnnotatedElement element) {
        return !element.isAnnotationPresent(ConfigIgnore.class);
    }

    private final Class<?> type;
    private final String path;
    private final boolean required;
    private final NamedAnnotatedElement element;
    private final boolean colorized;
    private final String repositoryName;
    private final Class<? extends Supplier<?>> defaultSupplier;

    public ConfigElement(@Nonnull Class<?> type, @Nonnull NamedAnnotatedElement element) {
        Preconditions.checkNotNull(type, "type");
        Preconditions.checkNotNull(element, "element");

        this.type = type;
        this.element = element;
        this.path = ConfigElement.resolvePath(element);
        this.required = ConfigElement.resolveRequired(element);
        this.colorized = ConfigElement.resolveColorized(element);
        this.repositoryName = ConfigElement.resolveRepositoryName(element);
        this.defaultSupplier = ConfigElement.resolveDefaultSupplier(element);
    }

    @Nonnull
    public Class<?> getType() {
        return this.type;
    }

    @Nonnull
    public String getPath() {
        return this.path;
    }

    public boolean isRequired() {
        return this.required;
    }

    @Nonnull
    public NamedAnnotatedElement getElement() {
        return this.element;
    }

    public boolean isColorized() {
        return this.colorized;
    }

    @Nonnull
    public Optional<String> getRepositoryName() {
        return Optional.ofNullable(this.repositoryName);
    }

    @Nonnull
    public Optional<Class<? extends Supplier<?>>> getDefaultSupplier() {
        return Optional.ofNullable(this.defaultSupplier);
    }

    @Nonnull
    public Object loadDefault() throws IllegalStateException, IllegalAccessException, InstantiationException {
        if (this.defaultSupplier == null) {
            throw new IllegalStateException("element=" + this.element.toString() + " does not have the @ConfigDefault annotation");
        }
        final Supplier<?> supplier = this.defaultSupplier.newInstance();
        return supplier.get();
    }

}