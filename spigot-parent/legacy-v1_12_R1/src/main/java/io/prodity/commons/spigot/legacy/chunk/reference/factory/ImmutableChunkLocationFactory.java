package io.prodity.commons.spigot.legacy.chunk.reference.factory;

import io.prodity.commons.spigot.legacy.chunk.reference.ImmutableChunkLocation;
import io.prodity.commons.spigot.legacy.chunk.reference.simple.SimpleImmutableChunkLocation;
import io.prodity.commons.spigot.legacy.world.reference.ImmutableWorldReference;
import io.prodity.commons.spigot.legacy.world.reference.WorldReference;
import io.prodity.commons.spigot.legacy.world.reference.factory.ImmutableWorldReferenceFactory;

public class ImmutableChunkLocationFactory extends AbstractChunkLocationFactory<ImmutableWorldReference, ImmutableChunkLocation> {

    public ImmutableChunkLocationFactory(ImmutableWorldReferenceFactory worldReferenceFactory) {
        super(worldReferenceFactory);
    }

    @Override
    public ImmutableChunkLocation of(WorldReference<?> worldReference, int chunkX, int chunkZ) {
        final ImmutableWorldReference<?> immutableWorldReference;
        if (worldReference instanceof ImmutableWorldReference) {
            immutableWorldReference = (ImmutableWorldReference<?>) worldReference;
        } else {
            immutableWorldReference = this.getWorldReferenceFactory().byNameFromExisting(worldReference);
        }
        return new SimpleImmutableChunkLocation(immutableWorldReference, chunkX, chunkZ);
    }

}