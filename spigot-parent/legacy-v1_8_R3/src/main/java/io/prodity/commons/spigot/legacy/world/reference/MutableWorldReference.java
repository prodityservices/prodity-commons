package io.prodity.commons.spigot.legacy.world.reference;

import org.bukkit.World;

public interface MutableWorldReference<T> extends WorldReference<T> {

    void setWorldReferent(T value);

    void setWorldReferentFrom(World world);

    interface Delegate<T> extends MutableWorldReference<T>, DelegateWorldReference<MutableWorldReference<T>, T> {

        @Override
        default void setWorldReferentFrom(World world) {
            this.getWorldReference().setWorldReferentFrom(world);
        }

        @Override
        default void setWorldReferent(T value) {
            this.getWorldReference().setWorldReferent(value);
        }

    }

}