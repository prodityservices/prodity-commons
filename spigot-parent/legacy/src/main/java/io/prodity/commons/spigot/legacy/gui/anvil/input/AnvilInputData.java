package io.prodity.commons.spigot.legacy.gui.anvil.input;

import com.google.common.base.Preconditions;
import io.prodity.commons.spigot.legacy.message.replace.Replacer;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.annotation.Nullable;

public class AnvilInputData<T> {

    public static final class Builder<T> {

        private BiConsumer<AnvilInputGui<T>, String> failureCallback;
        private BiConsumer<AnvilInputGui<T>, T> successCallback;
        private Runnable inputCallback;
        private Function<String, AnvilInputResult<T>> resultParser;
        private BiConsumer<Replacer, AnvilInputResult<T>> replacerModifier;
        private String inputItemKey;
        private String outputValidItemKey;

        private Builder() {

        }

        public Builder<T> setFailureCallback(@Nullable BiConsumer<AnvilInputGui<T>, String> failureCallback) {
            this.failureCallback = failureCallback;
            return this;
        }

        public Builder<T> setSuccessCallback(@Nullable BiConsumer<AnvilInputGui<T>, T> successCallback) {
            this.successCallback = successCallback;
            return this;
        }

        public Builder<T> setInputCallback(@Nullable Runnable inputCallback) {
            this.inputCallback = inputCallback;
            return this;
        }

        public Builder<T> setResultParser(@Nullable Function<String, AnvilInputResult<T>> resultParser) {
            this.resultParser = resultParser;
            return this;
        }

        public Builder<T> setReplacerModifier(@Nullable BiConsumer<Replacer, AnvilInputResult<T>> replacerModifier) {
            this.replacerModifier = replacerModifier;
            return this;
        }

        public Builder<T> setInputItemKey(@Nullable String inputItemKey) {
            this.inputItemKey = inputItemKey;
            return this;
        }

        public Builder<T> setOutputValidItemKey(@Nullable String outputValidItemKey) {
            this.outputValidItemKey = outputValidItemKey;
            return this;
        }

        private void verify() {
            Preconditions.checkNotNull(this.resultParser, "resultParser");
            Preconditions.checkNotNull(this.inputItemKey, "inputItemKey");
            Preconditions.checkNotNull(this.outputValidItemKey, "outputValidItemKey");
        }

        public AnvilInputData<T> build() {
            this.verify();
            return new AnvilInputData<>(this.failureCallback, this.successCallback, this.inputCallback, this.resultParser,
                this.replacerModifier, this.inputItemKey, this.outputValidItemKey);
        }

    }

    public static final <T> Builder<T> builder() {
        return new Builder<>();
    }

    private final BiConsumer<AnvilInputGui<T>, String> failureCallback;
    private final BiConsumer<AnvilInputGui<T>, T> successCallback;
    private final Runnable inputCallback;
    private final Function<String, AnvilInputResult<T>> resultParser;
    private final BiConsumer<Replacer, AnvilInputResult<T>> replacerModifier;
    private final String inputItemKey;
    private final String outputValidItemKey;

    public AnvilInputData(
        @Nullable BiConsumer<AnvilInputGui<T>, String> failureCallback,
        @Nullable BiConsumer<AnvilInputGui<T>, T> successCallback,
        @Nullable Runnable inputCallback,
        Function<String, AnvilInputResult<T>> resultParser,
        @Nullable BiConsumer<Replacer, AnvilInputResult<T>> replacerModifier,
        String inputItemKey,
        String outputValidItemKey
    ) {
        Preconditions.checkNotNull(resultParser, "resultParser");
        Preconditions.checkNotNull(inputItemKey, "inputItemKey");
        Preconditions.checkNotNull(outputValidItemKey, "outputValidItemKey");
        this.failureCallback = failureCallback;
        this.successCallback = successCallback;
        this.inputCallback = inputCallback;
        this.resultParser = resultParser;
        this.replacerModifier = replacerModifier;
        this.inputItemKey = inputItemKey;
        this.outputValidItemKey = outputValidItemKey;
    }

    public void callFailureCallback(AnvilInputGui<T> gui, String errorKey) {
        Preconditions.checkNotNull(gui, "gui");
        Preconditions.checkNotNull(errorKey, "errorKey");
        if (this.failureCallback != null) {
            this.failureCallback.accept(gui, errorKey);
        }
    }

    public void callSuccessCallback(AnvilInputGui<T> gui, T value) {
        Preconditions.checkNotNull(gui, "gui");
        Preconditions.checkNotNull(value, "value");
        if (this.successCallback != null) {
            this.successCallback.accept(gui, value);
        }
    }

    public void callInputCallback() {
        if (this.inputCallback != null) {
            this.inputCallback.run();
        }
    }

    public AnvilInputResult<T> parseResult(String value) {
        Preconditions.checkNotNull(value, "value");
        return this.resultParser.apply(value);
    }

    public void modifyReplacer(Replacer replacer, AnvilInputResult<T> result) {
        Preconditions.checkNotNull(replacer, "replacer");
        Preconditions.checkNotNull(result, "result");
        if (this.replacerModifier != null) {
            this.replacerModifier.accept(replacer, result);
        }
    }

    public String getInputItemKey() {
        return this.inputItemKey;
    }

    public String getOutputValidItemKey() {
        return this.outputValidItemKey;
    }

}