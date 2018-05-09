package io.prodity.commons.config.load;

import com.google.common.base.Preconditions;
import io.prodity.commons.config.annotate.inject.Config;
import io.prodity.commons.config.annotate.inject.Configured;
import java.io.File;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import ninja.leaping.configurate.ConfigurationNode;

public class ConfigLoadContext<T> implements Configured {

    public static final class Builder<T> {

        private ConfigClass<T> configType;
        private String internalPath;
        private File file;
        private T configObject;

        private Builder() {

        }

        @Nonnull
        public ConfigLoadContext.Builder<T> setConfigType(@Nonnull ConfigClass<T> configType) {
            Preconditions.checkNotNull(configType, "configType");
            this.configType = configType;
            return this;
        }

        @Nonnull
        public ConfigLoadContext.Builder<T> setInternalPath(String internalPath) {
            Preconditions.checkNotNull(internalPath, "internalPath");
            this.internalPath = internalPath;
            return this;
        }

        @Nonnull
        public ConfigLoadContext.Builder<T> setFile(File file) {
            Preconditions.checkNotNull(file, "file");
            this.file = file;
            return this;
        }

        @Nonnull
        public ConfigLoadContext.Builder<T> setConfigObject(T configObject) {
            Preconditions.checkNotNull(configObject, "configObject");
            this.configObject = configObject;
            return this;
        }

        private void verify() {
            Preconditions.checkNotNull(this.configType, "configType");
            Preconditions.checkNotNull(this.internalPath, "internalPath");
            Preconditions.checkNotNull(this.file, "file");
            Preconditions.checkNotNull(this.configObject, "configObject");
        }

        @Nonnull
        public ConfigLoadContext<T> build() {
            this.verify();
            return new ConfigLoadContext<>(this.configType, this.internalPath, this.file, this.configObject);
        }

    }

    @Nonnull
    public static <T> ConfigLoadContext.Builder<T> builder() {
        return new ConfigLoadContext.Builder<>();
    }

    private final ConfigClass<T> configType;
    private final String internalPath;
    private final File file;

    private final T configObject;
    private ConfigurationNode masterNode;

    private ConfigLoadContext(ConfigClass<T> configType, String internalPath, File file, T configObject) {
        this.configType = configType;
        this.internalPath = internalPath;
        this.file = file;
        this.configObject = configObject;
    }

    @Nonnull
    public ConfigClass<T> getConfigType() {
        return this.configType;
    }

    @Override
    public Config getConfig() {
        return this.configType.getConfig();
    }

    @Nonnull
    public String getInternalPath() {
        return this.internalPath;
    }

    @Nonnull
    public File getFile() {
        return this.file;
    }

    @Nonnull
    public T getConfigObject() {
        return this.configObject;
    }

    public void setMasterNode(@Nonnull ConfigurationNode masterNode) {
        Preconditions.checkNotNull(masterNode, "masterNode");
        this.masterNode = masterNode;
    }

    @Nullable
    public ConfigurationNode getMasterNode() {
        return this.masterNode;
    }

}