package io.prodity.commons.spigot.legacy.item.builder.meta.skullowner;

import io.prodity.commons.spigot.legacy.item.builder.meta.ImmutableItemBuilderMeta;
import lombok.Getter;
import org.bukkit.OfflinePlayer;

public class ImmutableSkullOwnerMeta extends SkullOwnerMeta implements ImmutableItemBuilderMeta<String> {

    @Getter
    private final String value;

    protected ImmutableSkullOwnerMeta(String value) {
        this.value = value;
    }

}
