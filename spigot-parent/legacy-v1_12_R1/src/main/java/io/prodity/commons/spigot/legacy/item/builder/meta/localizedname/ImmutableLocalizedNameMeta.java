package io.prodity.commons.spigot.legacy.item.builder.meta.localizedname;

import io.prodity.commons.spigot.legacy.item.builder.meta.ImmutableItemBuilderMeta;
import lombok.Getter;

public class ImmutableLocalizedNameMeta extends LocalizedNameMeta implements ImmutableItemBuilderMeta<String> {

    @Getter
    private final String value;

    protected ImmutableLocalizedNameMeta(String value) {
        this.value = value;
    }

    @Override
    public MutableLocalizedNameMeta toMutable() {
        return LocalizedNameMeta.mutable(this.value);
    }

}
