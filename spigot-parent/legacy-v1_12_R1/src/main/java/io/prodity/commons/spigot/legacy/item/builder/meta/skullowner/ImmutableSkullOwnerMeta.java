package io.prodity.commons.spigot.legacy.item.builder.meta.skullowner;

import io.prodity.commons.spigot.legacy.item.builder.meta.ImmutableItemBuilderMeta;
import lombok.Getter;
import org.bukkit.OfflinePlayer;

public class ImmutableSkullOwnerMeta extends SkullOwnerMeta implements ImmutableItemBuilderMeta<OfflinePlayer> {

    @Getter
    private final OfflinePlayer value;

    protected ImmutableSkullOwnerMeta(OfflinePlayer value) {
        this.value = value;
    }

}
