package io.prodity.commons.spigot.legacy.location.reference;

import io.prodity.commons.spigot.legacy.chunk.reference.MutableChunkLocation;
import io.prodity.commons.spigot.legacy.world.reference.MutableWorldReference;

public interface MutableLocationStore extends LocationStore, MutableChunkLocation {

    @Override
    MutableWorldReference<?> getWorldReference();

    void setX(double x);

    void setY(double y);

    void setZ(double z);

    void setYaw(float yaw);

    void setPitch(float pitch);

}