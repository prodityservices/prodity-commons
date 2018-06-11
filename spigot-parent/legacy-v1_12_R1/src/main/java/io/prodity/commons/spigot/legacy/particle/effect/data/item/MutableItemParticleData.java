package io.prodity.commons.spigot.legacy.particle.effect.data.item;

import io.prodity.commons.spigot.legacy.particle.effect.data.MutableParticleData;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MutableItemParticleData extends MutableParticleData<ItemStack> implements ItemParticleData {

    public MutableItemParticleData(Material material, byte data) throws IllegalArgumentException {
        super(material, data);
    }

    @Override
    public ImmutableItemParticleData toImmutable() {
        return new ImmutableItemParticleData(this.getMaterial(), this.getData());
    }

    @Override
    public MutableItemParticleData toMutable() {
        return new MutableItemParticleData(this.getMaterial(), this.getData());
    }

}