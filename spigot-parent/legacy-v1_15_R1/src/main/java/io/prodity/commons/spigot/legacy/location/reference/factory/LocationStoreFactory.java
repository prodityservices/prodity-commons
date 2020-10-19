package io.prodity.commons.spigot.legacy.location.reference.factory;

import io.prodity.commons.spigot.legacy.location.reference.LocationReference;
import io.prodity.commons.spigot.legacy.location.reference.LocationStore;
import io.prodity.commons.spigot.legacy.world.reference.WorldReference;
import io.prodity.commons.spigot.legacy.world.reference.factory.WorldReferenceFactory;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public interface LocationStoreFactory<W extends WorldReference, L extends LocationStore> {

    WorldReferenceFactory<? extends W, ? extends W> getWorldReferenceFactory();

    L of(WorldReference<?> worldReference, double x, double y, double z, float yaw, float pitch);

    default L of(WorldReference<?> worldReference, double x, double y, double z) {
        return this.of(worldReference, x, y, z, 0F, 0F);
    }

    default L of(World world, double x, double y, double z, float yaw, float pitch) {
        final W worldReference = this.getWorldReferenceFactory().byNameFromWorld(world);
        return this.of(worldReference, x, y, z, yaw, pitch);
    }

    default L of(World world, double x, double y, double z) {
        return this.of(world, x, y, z, 0F, 0F);
    }

    default L fromLocation(Location location) {
        final W worldReference = this.getWorldReferenceFactory().byNameFromLocation(location);
        return this.of(worldReference, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    default L fromBlock(Block block) {
        return this.fromLocation(block.getLocation());
    }

    default L fromExisting(LocationReference locationReference) {
        if (locationReference instanceof LocationStore) {
            final WorldReference<?> worldReference = locationReference.getWorldReference();
            final LocationStore store = (LocationStore) locationReference;
            return this.of(worldReference, store.getX(), store.getY(), store.getZ(), store.getYaw(), store.getPitch());
        } else {
            final Location bukkitLocation = locationReference.getBukkitLocation();
            return this.fromLocation(bukkitLocation);
        }
    }

    default L fromEntity(Entity entity) {
        return this.fromLocation(entity.getLocation());
    }

}