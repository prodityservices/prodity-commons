package io.prodity.commons.spigot.legacy.particle.effect.data.item;

import io.prodity.commons.spigot.legacy.particle.effect.data.ImmutableParticleData;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ImmutableItemParticleData extends ImmutableParticleData<ItemStack> implements ItemParticleData {

    public ImmutableItemParticleData(Material material, byte data) throws IllegalArgumentException {
        super(material, data);
    }

    @Override
    public ImmutableItemParticleData toImmutable() {
        return this;
    }

    @Override
    public MutableItemParticleData toMutable() {
        return new MutableItemParticleData(this.getMaterial(), this.getData());
    }

}
