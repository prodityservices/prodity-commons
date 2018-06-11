package io.prodity.commons.spigot.legacy.location.reference;

import io.prodity.commons.spigot.legacy.world.reference.WorldReference;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public interface DelegateLocationReference extends LocationReference {

    LocationReference getLocationReference();

    @Override
    default WorldReference<?> getWorldReference() {
        return this.getLocationReference().getWorldReference();
    }

    @Override
    default Location getBukkitLocation() {
        return this.getLocationReference().getBukkitLocation();
    }

    @Override
    default World getBukkitWorld() {
        return this.getLocationReference().getBukkitWorld();
    }

    @Override
    default Chunk getBukkitChunk() {
        return this.getLocationReference().getBukkitChunk();
    }

    @Override
    default Object getWorldReferent() {
        return this.getLocationReference().getWorldReferent();
    }

}