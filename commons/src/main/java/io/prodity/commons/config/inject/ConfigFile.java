package io.prodity.commons.config.inject;

import com.google.common.base.Preconditions;
import io.prodity.commons.config.annotate.inject.Config;
import io.prodity.commons.config.annotate.inject.Configured;
import java.io.File;

public class ConfigFile implements Configured {

    private final Config config;
    private final File file;
    private final String internalPath;

    public ConfigFile(Config annotation, File file, String internalPath) {
        Preconditions.checkNotNull(annotation, "config");
        Preconditions.checkNotNull(file, "file");
        Preconditions.checkNotNull(internalPath, "internalPath");

        this.config = annotation;
        this.file = file;
        this.internalPath = internalPath;
    }

    public String getInternalPath() {
        return this.internalPath;
    }

    public File getFile() {
        return this.file;
    }

    @Override
    public Config getConfig() {
        return this.config;
    }

}