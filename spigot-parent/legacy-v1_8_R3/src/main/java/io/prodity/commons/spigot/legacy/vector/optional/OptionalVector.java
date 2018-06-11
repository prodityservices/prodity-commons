package io.prodity.commons.spigot.legacy.vector.optional;

import java.util.Optional;
import org.bukkit.util.Vector;

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