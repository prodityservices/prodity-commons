package io.prodity.commons.spigot.legacy.chunk.reference.factory;

import io.prodity.commons.spigot.legacy.chunk.reference.MutableChunkLocation;
import io.prodity.commons.spigot.legacy.chunk.reference.simple.SimpleMutableChunkLocation;
import io.prodity.commons.spigot.legacy.world.reference.MutableWorldReference;
import io.prodity.commons.spigot.legacy.world.reference.WorldReference;
import io.prodity.commons.spigot.legacy.world.reference.factory.MutableWorldReferenceFactory;

public class MutableChunkLocationFactory extends AbstractChunkLocationFactory<MutableWorldReference, MutableChunkLocation> {

    public MutableChunkLocationFactory(MutableWorldReferenceFactory worldReferenceFactory) {
        super(worldReferenceFactory);
    }

    @Override
    public MutableChunkLocation of(WorldReference<?> worldReference, int chunkX, int chunkZ) {
        final MutableWorldReference<?> mutableWorldReference;
        if (worldReference instanceof MutableWorldReference) {
            mutableWorldReference = (MutableWorldReference<?>) worldReference;
        } else {
            mutableWorldReference = this.getWorldReferenceFactory().byNameFromExisting(worldReference);
        }

        return new SimpleMutableChunkLocation(mutableWorldReference, chunkX, chunkZ);
    }

}