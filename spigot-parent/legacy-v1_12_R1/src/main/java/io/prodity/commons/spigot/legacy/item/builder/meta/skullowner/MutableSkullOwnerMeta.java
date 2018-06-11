package io.prodity.commons.spigot.legacy.item.builder.meta.skullowner;

import io.prodity.commons.spigot.legacy.item.builder.meta.MutableItemBuilderMeta;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.OfflinePlayer;

public class MutableSkullOwnerMeta extends SkullOwnerMeta implements MutableItemBuilderMeta<OfflinePlayer> {

    @Getter
    @Setter
    @Accessors(chain = true)
    private OfflinePlayer value;

    public MutableSkullOwnerMeta(OfflinePlayer value) {
        this.value = value;
    }

}