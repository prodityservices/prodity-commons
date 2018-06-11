package io.prodity.commons.spigot.legacy.item.builder.meta.skulltexture;

import io.prodity.commons.spigot.legacy.item.builder.meta.ImmutableItemBuilderMeta;
import lombok.Getter;

public class ImmutableSkullTextureMeta extends SkullTextureMeta implements ImmutableItemBuilderMeta<String> {

    @Getter
    private final String value;

    protected ImmutableSkullTextureMeta(String value) {
        this.value = value;
    }

}
