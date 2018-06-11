package io.prodity.commons.spigot.legacy.particle.effect.data.block;

import io.prodity.commons.spigot.legacy.particle.effect.data.ImmutableParticleData;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public class ImmutableBlockParticleData extends ImmutableParticleData<MaterialData> implements BlockParticleData {

    public ImmutableBlockParticleData(Material material, byte data) {
        super(material, data);
    }

    @Override
    public ImmutableBlockParticleData toImmutable() {
        return this;
    }

    @Override
    public MutableBlockParticleData toMutable() {
        return new MutableBlockParticleData(this.getMaterial(), this.getData());
    }

}