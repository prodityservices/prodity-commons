package io.prodity.commons.spigot.legacy.chunk.reference.factory;

import io.prodity.commons.spigot.legacy.chunk.reference.ChunkLocation;
import io.prodity.commons.spigot.legacy.world.reference.WorldReference;
import io.prodity.commons.spigot.legacy.world.reference.factory.WorldReferenceFactory;
import lombok.Getter;

public abstract class AbstractChunkLocationFactory<W extends WorldReference, C extends ChunkLocation> implements
    ChunkLocationFactory<W, C> {

    @Getter
    private final WorldReferenceFactory<? extends W, ? extends W> worldReferenceFactory;

    protected AbstractChunkLocationFactory(WorldReferenceFactory<? extends W, ? extends W> worldReferenceFactory) {
        this.worldReferenceFactory = worldReferenceFactory;
    }
}
