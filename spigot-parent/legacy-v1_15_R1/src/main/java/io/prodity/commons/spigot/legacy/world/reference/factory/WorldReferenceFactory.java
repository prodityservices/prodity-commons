package io.prodity.commons.spigot.legacy.world.reference.factory;

import io.prodity.commons.spigot.legacy.world.reference.DelegateWorldReference;
import io.prodity.commons.spigot.legacy.world.reference.WorldReference;
import io.prodity.commons.spigot.legacy.world.reference.name.WorldNameReference;
import io.prodity.commons.spigot.legacy.world.reference.uid.WorldUidReference;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.UUID;

public interface WorldReferenceFactory<N extends WorldNameReference, U extends WorldUidReference> {

    N byName(String worldName);

    U byUid(UUID worldUid);

    default N byNameFromWorld(World world) {
        return this.byName(world.getName());
    }

    default U byUidFromWorld(World world) {
        return this.byUid(world.getUID());
    }

    default N byNameFromExisting(WorldReference<?> worldReference) {
        if (worldReference instanceof DelegateWorldReference) {
            worldReference = ((DelegateWorldReference) worldReference).getWorldReference();
        }
        if (worldReference instanceof WorldNameReference) {
            return this.byName(((WorldNameReference) worldReference).getWorldReferent());
        }
        return this.byNameFromWorld(worldReference.getBukkitWorld());
    }

    default U byUidFromExisting(WorldReference<?> worldReference) {
        if (worldReference instanceof DelegateWorldReference) {
            worldReference = ((DelegateWorldReference) worldReference).getWorldReference();
        }
        if (worldReference instanceof WorldUidReference) {
            return this.byUid(((WorldUidReference) worldReference).getWorldReferent());
        }
        return this.byUidFromWorld(worldReference.getBukkitWorld());
    }

    default N byNameFromChunk(Chunk chunk) {
        return this.byNameFromWorld(chunk.getWorld());
    }

    default U byUidFromChunk(Chunk chunk) {
        return this.byUidFromWorld(chunk.getWorld());
    }

    default N byNameFromLocation(Location location) {
        return this.byNameFromWorld(location.getWorld());
    }

    default U byUidFromLocation(Location location) {
        return this.byUidFromWorld(location.getWorld());
    }

    default N byNameFromBlock(Block block) {
        return this.byNameFromWorld(block.getWorld());
    }

    default U byUidFromBlock(Block block) {
        return this.byUidFromWorld(block.getWorld());
    }

    default N byNameFromEntity(Entity entity) {
        return this.byNameFromWorld(entity.getWorld());
    }

    default U byUidFromEntity(Entity entity) {
        return this.byUidFromWorld(entity.getWorld());
    }

}