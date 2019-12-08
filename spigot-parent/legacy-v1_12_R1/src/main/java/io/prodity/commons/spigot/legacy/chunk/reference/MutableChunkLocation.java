package io.prodity.commons.spigot.legacy.chunk.reference;

import io.prodity.commons.spigot.legacy.world.reference.MutableWorldReference;

public interface MutableChunkLocation extends ChunkLocation, MutableWorldReference {

    void setChunkX(int x);

    void setChunkZ(int z);

}
