package io.prodity.commons.spigot.legacy.particle.effect.data.item;

import io.prodity.commons.spigot.legacy.particle.effect.data.ParticleData;
import org.bukkit.inventory.ItemStack;

public interface ItemParticleData extends ParticleData<ItemStack> {

    @Override
    default ItemStack createSpawnData() {
        return new ItemStack(this.getMaterial(), 1, (short) 0, this.getData());
    }

}