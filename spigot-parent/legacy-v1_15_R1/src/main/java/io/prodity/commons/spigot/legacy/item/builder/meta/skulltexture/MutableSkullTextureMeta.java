package io.prodity.commons.spigot.legacy.item.builder.meta.skulltexture;

import io.prodity.commons.spigot.legacy.item.builder.meta.MutableItemBuilderMeta;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public class MutableSkullTextureMeta extends SkullTextureMeta implements MutableItemBuilderMeta<String> {

    @Getter
    @Setter
    @Accessors(chain = true)
    private String value;

    public MutableSkullTextureMeta(String value) {
        this.value = value;
    }

}