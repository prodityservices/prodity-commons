package io.prodity.commons.spigot.legacy.chunk.reference.simple;

import com.google.common.base.Objects;
import io.prodity.commons.spigot.legacy.chunk.reference.AbstractChunkLocation;
import io.prodity.commons.spigot.legacy.chunk.reference.MutableChunkLocation;
import io.prodity.commons.spigot.legacy.world.reference.MutableWorldReference;
import lombok.Getter;
import lombok.Setter;

public class SimpleMutableChunkLocation extends AbstractChunkLocation implements MutableChunkLocation, MutableWorldReference.Delegate {

    @Getter
    private final MutableWorldReference<?> worldReference;

    @Getter
    @Setter
    private int chunkX;

    @Getter
    @Setter
    private int chunkZ;

    public SimpleMutableChunkLocation(MutableWorldReference<?> worldReference, int chunkX, int chunkZ) {
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

        if (!(object instanceof SimpleMutableChunkLocation)) {
            return false;
        }

        final SimpleMutableChunkLocation that = (SimpleMutableChunkLocation) object;

        return Objects.equal(this.worldReference, that.worldReference) &&
            Objects.equal(this.chunkX, that.chunkX) &&
            Objects.equal(this.chunkZ, that.chunkZ);
    }

}