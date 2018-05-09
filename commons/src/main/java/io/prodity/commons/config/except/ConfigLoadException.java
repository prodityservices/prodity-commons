package io.prodity.commons.config.except;

import com.google.common.base.Preconditions;
import io.prodity.commons.config.annotate.inject.Config;
import io.prodity.commons.config.annotate.inject.Configured;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ConfigLoadException extends Exception {

    @Nonnull
    public static Function<Throwable, ConfigLoadException> newMapper(Config config) {
        return (throwable) -> new ConfigLoadException(config, throwable);
    }

    @Nonnull
    public static Function<Throwable, ConfigLoadException> newMapper(Config config, String message) {
        return (throwable) -> new ConfigLoadException(config, message, throwable);
    }

    @Nonnull
    public static Function<Throwable, ConfigLoadException> newMapper(Configured configured) {
        return (throwable) -> new ConfigLoadException(configured, throwable);
    }

    @Nonnull
    public static Function<Throwable, ConfigLoadException> newMapper(Configured configured, String message) {
        return (throwable) -> new ConfigLoadException(configured, message, throwable);
    }

    @Nonnull
    private static String createMessage(@Nonnull Config config, @Nullable String message) {
        Preconditions.checkNotNull(config, "config");

        final StringBuilder messageBuilder = new StringBuilder("Exception occurred while loading config=")
            .append(config.fileName());

        if (message != null) {
            messageBuilder.append(": ").append(message);
        }

        return messageBuilder.toString();
    }

    private final Config config;

    public ConfigLoadException(@Nonnull Config config, @Nullable String message) {
        super(ConfigLoadException.createMessage(config, message));
        this.config = config;
    }

    public ConfigLoadException(@Nonnull Config config, @Nullable String message, @Nullable Throwable cause) {
        super(ConfigLoadException.createMessage(config, message), cause);
        this.config = config;
    }

    public ConfigLoadException(@Nonnull Config config, @Nullable Throwable cause) {
        super(ConfigLoadException.createMessage(config, null), cause);
        this.config = config;
    }

    public ConfigLoadException(@Nonnull Configured configured, @Nullable String message) {
        this(configured.getConfig(), message);
    }

    public ConfigLoadException(@Nonnull Configured configured, @Nullable String message, @Nullable Throwable cause) {
        this(configured.getConfig(), message, cause);
    }

    public ConfigLoadException(@Nonnull Configured configured, @Nullable Throwable cause) {
        this(configured.getConfig(), cause);
    }

    @Nonnull
    public Config getConfig() {
        return this.config;
    }

}