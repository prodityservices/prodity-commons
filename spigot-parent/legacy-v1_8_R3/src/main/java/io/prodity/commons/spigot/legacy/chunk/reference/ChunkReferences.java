package io.prodity.commons.spigot.legacy.chunk.reference;

import io.prodity.commons.spigot.legacy.chunk.reference.factory.ImmutableChunkLocationFactory;
import io.prodity.commons.spigot.legacy.chunk.reference.factory.MutableChunkLocationFactory;
import io.prodity.commons.spigot.legacy.world.reference.WorldReferences;

public class ChunkReferences {

    private static ChunkReferences instance;

    private static ChunkReferences getInstance() {
        if (ChunkReferences.instance == null) {
            ChunkReferences.instance = new ChunkReferences();
        }
        return ChunkReferences.instance;
    }

    public static MutableChunkLocationFactory mutableFactory() {
        return ChunkReferences.getInstance().mutableReferenceFactory;
    }

    public static ImmutableChunkLocationFactory immutableFactory() {
        return ChunkReferences.getInstance().immutableReferenceFactory;
    }

    private final MutableChunkLocationFactory mutableReferenceFactory;
    private final ImmutableChunkLocationFactory immutableReferenceFactory;

    private ChunkReferences() {
        this.mutableReferenceFactory = new MutableChunkLocationFactory(WorldReferences.mutableFactory());
        this.immutableReferenceFactory = new ImmutableChunkLocationFactory(WorldReferences.immutableFactory());
    }

}