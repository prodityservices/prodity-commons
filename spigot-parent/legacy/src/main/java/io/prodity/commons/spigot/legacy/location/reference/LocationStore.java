package io.prodity.commons.spigot.legacy.location.reference;

public interface LocationStore extends LocationReference {

    double getX();

    double getY();

    double getZ();

    float getYaw();

    float getPitch();

}