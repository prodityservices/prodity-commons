package io.prodity.commons.spigot.legacy.item.builder.meta.spawntype;

import io.prodity.commons.spigot.legacy.item.builder.meta.ImmutableItemBuilderMeta;
import lombok.Getter;
import org.bukkit.entity.EntityType;

public class ImmutableSpawnTypeMeta extends SpawnTypeMeta implements ImmutableItemBuilderMeta<EntityType> {

    @Getter
    private final EntityType value;

    protected ImmutableSpawnTypeMeta(EntityType value) {
        this.value = value;
    }

}
