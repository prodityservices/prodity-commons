package io.prodity.commons.spigot.legacy.location.reference;

import io.prodity.commons.spigot.legacy.chunk.reference.AbstractChunkLocation;
import org.bukkit.Location;

public abstract class AbstractLocationStore extends AbstractChunkLocation implements LocationStore {

    @Override
    public Location getBukkitLocation() {
        return new Location(this.getBukkitWorld(), this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
    }

    @Override
    public int getChunkX() {
        return (int) Math.floor(this.getX() / 16.0);
    }

    @Override
    public int getChunkZ() {
        return (int) Math.floor(this.getZ() / 16.0);
    }

}