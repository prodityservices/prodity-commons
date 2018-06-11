package io.prodity.commons.spigot.legacy.world.reference;

import org.bukkit.World;

public interface DelegateWorldReference<T extends WorldReference<R>, R> extends WorldReference<R> {

    T getWorldReference();

    @Override
    default R getWorldReferent() {
        return this.getWorldReference().getWorldReferent();
    }

    @Override
    default World getBukkitWorld() {
        return this.getWorldReference().getBukkitWorld();
    }

}