package io.prodity.commons.spigot.legacy.location.reference;

import io.prodity.commons.spigot.legacy.chunk.reference.ImmutableChunkLocation;
import io.prodity.commons.spigot.legacy.world.reference.ImmutableWorldReference;

public interface ImmutableLocationStore extends LocationStore, ImmutableChunkLocation {

    @Override
    ImmutableWorldReference<?> getWorldReference();

}