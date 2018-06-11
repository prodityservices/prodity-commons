package io.prodity.commons.spigot.legacy.chunk.reference;

import io.prodity.commons.spigot.legacy.world.reference.WorldReference;
import org.bukkit.Chunk;

public interface ChunkReference extends WorldReference {

    WorldReference<?> getWorldReference();

    Chunk getBukkitChunk();

}