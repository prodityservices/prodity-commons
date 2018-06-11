package io.prodity.commons.spigot.legacy.location.reference.simple;

import com.google.common.base.Objects;
import io.prodity.commons.spigot.legacy.location.reference.AbstractLocationStore;
import io.prodity.commons.spigot.legacy.location.reference.MutableLocationStore;
import io.prodity.commons.spigot.legacy.world.reference.MutableWorldReference;
import lombok.Getter;
import lombok.Setter;

public class SimpleMutableLocationStore extends AbstractLocationStore implements MutableLocationStore, MutableWorldReference.Delegate {

    @Getter
    private final MutableWorldReference<?> worldReference;

    @Getter
    @Setter
    private double x;

    @Getter
    @Setter
    private double z;

    @Getter
    @Setter
    private double y;

    @Getter
    @Setter
    private float yaw;

    @Getter
    @Setter
    private float pitch;

    public SimpleMutableLocationStore(MutableWorldReference<?> worldReference, double x, double y, double z, float yaw, float pitch) {
        this.worldReference = worldReference;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void setChunkX(int x) {
        this.x *= 16.0;
    }

    @Override
    public void setChunkZ(int z) {
        this.z *= 16.0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.worldReference, this.x, this.z, this.y, this.yaw, this.pitch);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (!(object instanceof SimpleMutableLocationStore)) {
            return false;
        }

        final SimpleMutableLocationStore that = (SimpleMutableLocationStore) object;

        return Objects.equal(this.worldReference, that.worldReference) &&
            Objects.equal(this.x, that.x) &&
            Objects.equal(this.y, that.y) &&
            Objects.equal(this.z, that.z) &&
            Objects.equal(this.yaw, that.yaw) &&
            Objects.equal(this.pitch, that.pitch);
    }

}