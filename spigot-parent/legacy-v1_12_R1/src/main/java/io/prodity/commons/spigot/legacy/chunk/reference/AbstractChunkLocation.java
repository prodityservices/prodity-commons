package io.prodity.commons.spigot.legacy.chunk.reference;

import org.bukkit.Chunk;

public abstract class AbstractChunkLocation implements ChunkLocation {

    @Override
    public Chunk getBukkitChunk() {
        return this.getBukkitWorld().getChunkAt(this.getChunkX(), this.getChunkZ());
    }

}