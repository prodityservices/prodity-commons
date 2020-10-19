package io.prodity.commons.spigot.legacy.item.builder.meta.skullowner;

import io.prodity.commons.spigot.legacy.item.builder.meta.MutableItemBuilderMeta;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public class MutableSkullOwnerMeta extends SkullOwnerMeta implements MutableItemBuilderMeta<String> {

    @Getter
    @Setter
    @Accessors(chain = true)
    private String value;

    public MutableSkullOwnerMeta(String value) {
        this.value = value;
    }

}