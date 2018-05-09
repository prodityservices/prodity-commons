package io.prodity.commons.config.load;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import io.prodity.commons.config.annotate.inject.Config;
import io.prodity.commons.config.annotate.inject.Configured;
import io.prodity.commons.config.load.element.ConfigFieldElement;
import io.prodity.commons.config.load.method.InjectableConfigMethod;
import io.prodity.commons.config.load.method.ListenerConfigMethod;
import io.prodity.commons.config.load.method.ListenerType;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

public class ConfigClass<T> implements Configured {

    public static final class Builder<T> {

        private final Class<T> typeClass;
        private final Config annotation;
        private ImmutableList<ConfigFieldElement> injectableFields;
        private ImmutableList<ListenerConfigMethod> listenerMethods;
        private ImmutableList<InjectableConfigMethod> injectableMethods;

        private Builder(@Nonnull Class<T> typeClass) {
            Preconditions.checkNotNull(typeClass, "typeClass");
            Preconditions.checkState(typeClass.isAnnotationPresent(Config.class),
                "class=" + typeClass.getName() + " does not have the config @Config");

            this.typeClass = typeClass;
            this.annotation = typeClass.getAnnotation(Config.class);
        }

        @Nonnull
        public ConfigClass.Builder setInjectableFields(@Nonnull Iterable<ConfigFieldElement> injectableFields) {
            Preconditions.checkNotNull(injectableFields, "injectableFields");
            this.injectableFields = ImmutableList.copyOf(injectableFields);
            return this;
        }

        @Nonnull
        public ConfigClass.Builder setListenerMethods(@Nonnull Iterable<ListenerConfigMethod> listenerMethods) {
            Preconditions.checkNotNull(listenerMethods, "listenerMethods");
            this.listenerMethods = ImmutableList.copyOf(listenerMethods);
            return this;
        }

        @Nonnull
        public ConfigClass.Builder setInjectableMethods(@Nonnull Iterable<InjectableConfigMethod> injectableMethods) {
            Preconditions.checkNotNull(injectableMethods, "injectableMethods");
            this.injectableMethods = ImmutableList.copyOf(injectableMethods);
            return this;
        }

        private void verify() {
            Preconditions.checkNotNull(this.injectableFields, "injectableFields");
            Preconditions.checkNotNull(this.listenerMethods, "listenerMethods");
            Preconditions.checkNotNull(this.injectableMethods, "injectableMethods");
        }

        @Nonnull
        public ConfigClass<T> build() {
            this.verify();
            return new ConfigClass<>(this.typeClass, this.annotation, this.injectableFields, this.listenerMethods, this.injectableMethods);
        }

    }

    @Nonnull
    public static <T> ConfigClass.Builder<T> builder(@Nonnull Class<T> typeClass) {
        return new ConfigClass.Builder<>(typeClass);
    }

    private final Class<T> typeClass;
    private final Config config;
    private final ImmutableList<ConfigFieldElement> injectableFields;
    private final ImmutableList<ListenerConfigMethod> listenerMethods;
    private final ImmutableList<InjectableConfigMethod> injectableMethods;

    private ConfigClass(Class<T> typeClass, Config annotation, ImmutableList<ConfigFieldElement> injectableFields,
        ImmutableList<ListenerConfigMethod> listenerMethods, ImmutableList<InjectableConfigMethod> injectableMethods) {
        this.typeClass = typeClass;
        this.config = annotation;
        this.injectableFields = injectableFields;
        this.listenerMethods = listenerMethods;
        this.injectableMethods = injectableMethods;
    }

    @Nonnull
    public Class<T> getTypeClass() {
        return this.typeClass;
    }

    @Override
    public Config getConfig() {
        return this.config;
    }

    @Nonnull
    public ImmutableList<ConfigFieldElement> getInjectableFields() {
        return this.injectableFields;
    }

    @Nonnull
    public ImmutableList<ListenerConfigMethod> getListenerMethods() {
        return this.listenerMethods;
    }

    @Nonnull
    public List<ListenerConfigMethod> getListenerMethods(ListenerType listenerType) {
        return this.listenerMethods.stream()
            .filter((method) -> method.hasType(listenerType))
            .collect(Collectors.toList());
    }

    public void invokeListeners(Object object, ListenerType listenerType) throws InvocationTargetException, IllegalAccessException {
        for (ListenerConfigMethod method : this.getListenerMethods(listenerType)) {
            method.invoke(object);
        }
    }

    @Nonnull
    public ImmutableList<InjectableConfigMethod> getInjectableMethods() {
        return this.injectableMethods;
    }

}