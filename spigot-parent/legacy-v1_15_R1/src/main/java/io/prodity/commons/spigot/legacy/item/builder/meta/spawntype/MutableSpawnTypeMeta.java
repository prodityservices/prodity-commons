package io.prodity.commons.spigot.legacy.item.builder.meta.spawntype;

import io.prodity.commons.spigot.legacy.item.builder.meta.MutableItemBuilderMeta;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.entity.EntityType;

public class MutableSpawnTypeMeta extends SpawnTypeMeta implements MutableItemBuilderMeta<EntityType> {

    @Getter
    @Setter
    @Accessors(chain = true)
    private EntityType value;

    public MutableSpawnTypeMeta(EntityType value) {
        this.value = value;
    }

}
