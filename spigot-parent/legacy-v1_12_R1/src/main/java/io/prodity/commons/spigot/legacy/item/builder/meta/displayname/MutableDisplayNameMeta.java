package io.prodity.commons.spigot.legacy.item.builder.meta.displayname;

import io.prodity.commons.spigot.legacy.item.builder.meta.MutableItemBuilderMeta;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public class MutableDisplayNameMeta extends DisplayNameMeta implements MutableItemBuilderMeta<String> {

    @Getter
    @Setter
    @Accessors(chain = true)
    private String value;

    public MutableDisplayNameMeta(String value) {
        this.value = value;
    }

}
