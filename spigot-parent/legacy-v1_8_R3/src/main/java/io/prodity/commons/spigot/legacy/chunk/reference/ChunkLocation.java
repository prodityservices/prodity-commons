package io.prodity.commons.spigot.legacy.chunk.reference;

import io.prodity.commons.spigot.legacy.world.reference.WorldReference;

public interface ChunkLocation extends ChunkReference {

    int getChunkX();

    int getChunkZ();

    @Override
    WorldReference<?> getWorldReference();

}