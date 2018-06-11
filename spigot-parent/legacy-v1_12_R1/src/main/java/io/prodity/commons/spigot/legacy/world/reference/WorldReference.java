package io.prodity.commons.spigot.legacy.world.reference;

import org.bukkit.World;

public interface WorldReference<T> {

    T getWorldReferent();

    World getBukkitWorld();

}