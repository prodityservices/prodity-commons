package io.prodity.commons.spigot.legacy.chunk.reference.simple;

import com.google.common.base.Objects;
import io.prodity.commons.spigot.legacy.chunk.reference.AbstractChunkLocation;
import io.prodity.commons.spigot.legacy.chunk.reference.ImmutableChunkLocation;
import io.prodity.commons.spigot.legacy.world.reference.ImmutableWorldReference;
import lombok.Getter;

public class SimpleImmutableChunkLocation extends AbstractChunkLocation implements ImmutableChunkLocation,
    ImmutableWorldReference.Delegate {

    @Getter
    private final ImmutableWorldReference<?> worldReference;

    @Getter
    private final int chunkX;

    @Getter
    private final int chunkZ;

    public SimpleImmutableChunkLocation(ImmutableWorldReference<?> worldReference, int chunkX, int chunkZ) {
        this.worldReference = worldReference;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.worldReference, this.chunkX, this.chunkZ);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (!(object instanceof SimpleImmutableChunkLocation)) {
            return false;
        }

        final SimpleImmutableChunkLocation that = (SimpleImmutableChunkLocation) object;

        return Objects.equal(this.worldReference, that.worldReference) &&
            Objects.equal(this.chunkX, that.chunkX) &&
            Objects.equal(this.chunkZ, that.chunkZ);
    }

}