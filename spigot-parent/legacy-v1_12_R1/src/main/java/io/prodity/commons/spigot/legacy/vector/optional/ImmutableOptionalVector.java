package io.prodity.commons.spigot.legacy.vector.optional;

import java.util.Optional;
import lombok.Getter;
import org.bukkit.util.Vector;

public class ImmutableOptionalVector extends AbstractOptionalVector {

    public static ImmutableOptionalVector empty() {
        return new ImmutableOptionalVector(null, null, null);
    }

    public static ImmutableOptionalVector fromVector(Vector vector) {
        return vector == null ? ImmutableOptionalVector.empty() : ImmutableOptionalVector.of(vector.getX(), vector.getY(), vector.getZ());
    }

    public static ImmutableOptionalVector of(Optional<Double> x, Optional<Double> y, Optional<Double> z) {
        return new ImmutableOptionalVector(x, y, z);
    }

    public static ImmutableOptionalVector of(Double x, Double y, Double z) {
        return new ImmutableOptionalVector(Optional.ofNullable(x), Optional.ofNullable(y), Optional.ofNullable(z));
    }

    public static ImmutableOptionalVector ofX(Double x) {
        return ImmutableOptionalVector.of(x, null, null);
    }

    public static ImmutableOptionalVector ofXY(Double x, Double y) {
        return ImmutableOptionalVector.of(x, null, y);
    }

    public static ImmutableOptionalVector ofXZ(Double x, Double z) {
        return ImmutableOptionalVector.of(x, null, z);
    }

    public static ImmutableOptionalVector ofY(Double y) {
        return ImmutableOptionalVector.of(null, y, null);
    }

    public static ImmutableOptionalVector ofZ(Double z) {
        return ImmutableOptionalVector.of(null, null, z);
    }

    public static ImmutableOptionalVector ofYZ(Double y, Double z) {
        return ImmutableOptionalVector.of(null, y, z);
    }

    @Getter
    private final Optional<Double> x;
    @Getter
    private final Optional<Double> y;
    @Getter
    private final Optional<Double> z;

    public ImmutableOptionalVector(Optional<Double> x, Optional<Double> y, Optional<Double> z) {
        this.x = x == null ? Optional.empty() : x;
        this.y = y == null ? Optional.empty() : y;
        this.z = z == null ? Optional.empty() : z;
    }

    @Override
    public MutableOptionalVector toMutable() {
        return new MutableOptionalVector(this.x, this.y, this.z);
    }

    @Override
    public ImmutableOptionalVector toImmutable() {
        return this;
    }

}