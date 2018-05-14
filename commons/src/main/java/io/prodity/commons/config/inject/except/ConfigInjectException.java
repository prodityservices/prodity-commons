package io.prodity.commons.config.inject.except;

import com.google.common.base.Preconditions;
import io.prodity.commons.config.annotate.inject.Config;
import io.prodity.commons.config.annotate.inject.Configured;
import java.util.function.Function;
import javax.annotation.Nullable;

public class ConfigInjectException extends Exception {

    public static Function<Throwable, ConfigInjectException> newMapper(Config config) {
        return (throwable) -> new ConfigInjectException(config, throwable);
    }

    public static Function<Throwable, ConfigInjectException> newMapper(Config config, String message) {
        return (throwable) -> new ConfigInjectException(config, message, throwable);
    }

    public static Function<Throwable, ConfigInjectException> newMapper(Configured configured) {
        return (throwable) -> new ConfigInjectException(configured, throwable);
    }

    public static Function<Throwable, ConfigInjectException> newMapper(Configured configured, String message) {
        return (throwable) -> new ConfigInjectException(configured, message, throwable);
    }

    private static String createMessage(Config config, @Nullable String message) {
        Preconditions.checkNotNull(config, "config");

        final StringBuilder messageBuilder = new StringBuilder("Exception occurred while loading config=")
            .append(config.fileName());

        if (message != null) {
            messageBuilder.append(": ").append(message);
        }

        return messageBuilder.toString();
    }

    private final Config config;

    public ConfigInjectException(Config config, @Nullable String message) {
        super(ConfigInjectException.createMessage(config, message));
        this.config = config;
    }

    public ConfigInjectException(Config config, @Nullable String message, @Nullable Throwable cause) {
        super(ConfigInjectException.createMessage(config, message), cause);
        this.config = config;
    }

    public ConfigInjectException(Config config, @Nullable Throwable cause) {
        super(ConfigInjectException.createMessage(config, null), cause);
        this.config = config;
    }

    public ConfigInjectException(Configured configured, @Nullable String message) {
        this(configured.getConfig(), message);
    }

    public ConfigInjectException(Configured configured, @Nullable String message, @Nullable Throwable cause) {
        this(configured.getConfig(), message, cause);
    }

    public ConfigInjectException(Configured configured, @Nullable Throwable cause) {
        this(configured.getConfig(), cause);
    }

    public Config getConfig() {
        return this.config;
    }

}