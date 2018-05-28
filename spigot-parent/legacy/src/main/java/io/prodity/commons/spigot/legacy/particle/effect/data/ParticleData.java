package io.prodity.commons.spigot.legacy.particle.effect.data;

import io.prodity.commons.spigot.legacy.particle.effect.data.block.BlockParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.data.item.ItemParticleData;
import org.bukkit.Material;

public interface ParticleData<T> {

    Material getMaterial();

    byte getData();

    T getSpawnData();

    T createSpawnData();

    default void verify(Material material, byte data) throws IllegalArgumentException {

    }

    ImmutableParticleData<T> toImmutable();

    MutableParticleData<T> toMutable();

    BlockParticleData toBlockData();

    ItemParticleData toItemData();

}