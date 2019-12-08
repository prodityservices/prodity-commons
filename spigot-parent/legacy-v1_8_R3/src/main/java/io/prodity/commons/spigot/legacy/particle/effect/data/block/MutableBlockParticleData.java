package io.prodity.commons.spigot.legacy.particle.effect.data.block;

import io.prodity.commons.spigot.legacy.particle.effect.data.MutableParticleData;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public class MutableBlockParticleData extends MutableParticleData<MaterialData> implements BlockParticleData {

    public MutableBlockParticleData(Material material, byte data) {
        super(material, data);
    }

    @Override
    public ImmutableBlockParticleData toImmutable() {
        return new ImmutableBlockParticleData(this.getMaterial(), this.getData());
    }

    @Override
    public MutableBlockParticleData toMutable() {
        return new MutableBlockParticleData(this.getMaterial(), this.getData());
    }

}