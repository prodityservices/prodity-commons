package io.prodity.commons.spigot.legacy.particle.effect.data.block;

import io.prodity.commons.spigot.legacy.particle.effect.data.ParticleData;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public interface BlockParticleData extends ParticleData<MaterialData> {

    @Override
    default void verify(Material material, byte data) throws IllegalArgumentException {
        if (!material.isBlock()) {
            throw new IllegalArgumentException("specified material '" + material.name() + "' is not a block");
        }
    }

    @Override
    default MaterialData createSpawnData() {
        return new MaterialData(this.getMaterial(), this.getData());
    }

}