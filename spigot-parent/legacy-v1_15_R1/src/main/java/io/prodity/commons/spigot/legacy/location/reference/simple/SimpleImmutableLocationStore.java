package io.prodity.commons.spigot.legacy.location.reference.simple;

import com.google.common.base.Objects;
import io.prodity.commons.spigot.legacy.location.reference.AbstractLocationStore;
import io.prodity.commons.spigot.legacy.location.reference.ImmutableLocationStore;
import io.prodity.commons.spigot.legacy.world.reference.ImmutableWorldReference;
import lombok.Getter;

public class SimpleImmutableLocationStore extends AbstractLocationStore implements ImmutableLocationStore,
    ImmutableWorldReference.Delegate {

    @Getter
    private final ImmutableWorldReference<?> worldReference;

    @Getter
    private final double x;

    @Getter
    private final double z;

    @Getter
    private final double y;

    @Getter
    private final float yaw;

    @Getter
    private final float pitch;

    public SimpleImmutableLocationStore(ImmutableWorldReference<?> worldReference, double x, double y, double z, float yaw, float pitch) {
        this.worldReference = worldReference;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
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

        if (!(object instanceof SimpleImmutableLocationStore)) {
            return false;
        }

        final SimpleImmutableLocationStore that = (SimpleImmutableLocationStore) object;

        return Objects.equal(this.worldReference, that.worldReference) &&
            Objects.equal(this.x, that.x) &&
            Objects.equal(this.y, that.y) &&
            Objects.equal(this.z, that.z) &&
            Objects.equal(this.yaw, that.yaw) &&
            Objects.equal(this.pitch, that.pitch);
    }

}