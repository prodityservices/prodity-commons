package io.prodity.commons.spigot.legacy.vector.optional;

import org.bukkit.util.Vector;

import java.util.Optional;

public interface OptionalVector {

    Optional<Double> getX();

    Optional<Double> getY();

    Optional<Double> getZ();

    Vector getBukkitVector();

    default boolean isAllPresent() {
        return this.getX().isPresent() && this.getY().isPresent() && this.getZ().isPresent();
    }

    default boolean isAnyPresent() {
        return this.getX().isPresent() || this.getY().isPresent() || this.getZ().isPresent();
    }

    MutableOptionalVector toMutable();

    ImmutableOptionalVector toImmutable();

}