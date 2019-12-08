package io.prodity.commons.spigot.legacy.chunk.reference.factory;

import io.prodity.commons.spigot.legacy.chunk.reference.ChunkLocation;
import io.prodity.commons.spigot.legacy.chunk.reference.ChunkReference;
import io.prodity.commons.spigot.legacy.world.reference.WorldReference;
import io.prodity.commons.spigot.legacy.world.reference.factory.WorldReferenceFactory;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public interface ChunkLocationFactory<W extends WorldReference, C extends ChunkLocation> {

    WorldReferenceFactory<? extends W, ? extends W> getWorldReferenceFactory();

    C of(WorldReference<?> worldReference, int chunkX, int chunkZ);

    default C from(ChunkLocation chunkLocation) {
        return this.of(chunkLocation.getWorldReference(), chunkLocation.getChunkX(), chunkLocation.getChunkZ());
    }

    default C of(World world, int chunkX, int chunkZ) {
        W worldReference = this.getWorldReferenceFactory().byNameFromWorld(world);
        return this.of(worldReference, chunkX, chunkZ);
    }

    default C fromExisting(ChunkReference chunkReference) {
        if (chunkReference instanceof ChunkLocation) {
            final ChunkLocation chunkLocation = (ChunkLocation) chunkReference;
            return this.of(chunkLocation.getWorldReference(), chunkLocation.getChunkX(), chunkLocation.getChunkZ());
        } else {
            final Chunk chunk = chunkReference.getBukkitChunk();
            return this.fromChunk(chunk);
        }
    }

    default C fromChunk(Chunk chunk) {
        return this.of(chunk.getWorld(), chunk.getX(), chunk.getZ());
    }

    default C fromLocation(Location location) {
        return this.fromChunk(location.getChunk());
    }

    default C fromBlock(Block block) {
        return this.fromChunk(block.getChunk());
    }

}