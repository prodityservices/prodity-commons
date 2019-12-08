package io.prodity.commons.spigot.legacy.item.builder.meta.displayname;

import io.prodity.commons.spigot.legacy.item.builder.meta.ImmutableItemBuilderMeta;
import lombok.Getter;

public class ImmutableDisplayNameMeta extends DisplayNameMeta implements ImmutableItemBuilderMeta<String> {

    @Getter
    private final String value;

    protected ImmutableDisplayNameMeta(String value) {
        this.value = value;
    }

}