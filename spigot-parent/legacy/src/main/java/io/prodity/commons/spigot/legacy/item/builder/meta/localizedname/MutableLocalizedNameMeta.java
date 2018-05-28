package io.prodity.commons.spigot.legacy.item.builder.meta.localizedname;

import io.prodity.commons.spigot.legacy.item.builder.meta.MutableItemBuilderMeta;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public class MutableLocalizedNameMeta extends LocalizedNameMeta implements MutableItemBuilderMeta<String> {

    @Getter
    @Setter
    @Accessors(chain = true)
    private String value;

    public MutableLocalizedNameMeta(String value) {
        this.value = value;
    }

}
