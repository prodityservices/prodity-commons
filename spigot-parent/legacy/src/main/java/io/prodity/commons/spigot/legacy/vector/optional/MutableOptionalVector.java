package io.prodity.commons.spigot.legacy.vector.optional;

import java.util.Optional;
import lombok.Getter;
import org.bukkit.util.Vector;

public class MutableOptionalVector extends AbstractOptionalVector {

    public static MutableOptionalVector empty() {
        return new MutableOptionalVector(null, null, null);
    }

    public static MutableOptionalVector fromVector(Vector vector) {
        return vector == null ? MutableOptionalVector.empty() : MutableOptionalVector.of(vector.getX(), vector.getY(), vector.getZ());
    }

    public static MutableOptionalVector of(Optional<Double> x, Optional<Double> y, Optional<Double> z) {
        return new MutableOptionalVector(x, y, z);
    }

    public static MutableOptionalVector of(Double x, Double y, Double z) {
        return new MutableOptionalVector(Optional.ofNullable(x), Optional.ofNullable(y), Optional.ofNullable(z));
    }

    public static MutableOptionalVector ofX(Double x) {
        return MutableOptionalVector.of(x, null, null);
    }

    public static MutableOptionalVector ofXY(Double x, Double y) {
        return MutableOptionalVector.of(x, null, y);
    }

    public static MutableOptionalVector ofXZ(Double x, Double z) {
        return MutableOptionalVector.of(x, null, z);
    }

    public static MutableOptionalVector ofY(Double y) {
        return MutableOptionalVector.of(null, y, null);
    }

    public static MutableOptionalVector ofZ(Double z) {
        return MutableOptionalVector.of(null, null, z);
    }

    public static MutableOptionalVector ofYZ(Double y, Double z) {
        return MutableOptionalVector.of(null, y, z);
    }

    @Getter
    private Optional<Double> x;
    @Getter
    private Optional<Double> y;
    @Getter
    private Optional<Double> z;

    public MutableOptionalVector(Optional<Double> x, Optional<Double> y, Optional<Double> z) {
        this.x = x == null ? Optional.empty() : x;
        this.y = y == null ? Optional.empty() : y;
        this.z = z == null ? Optional.empty() : z;
    }

    public void setX(Optional<Double> x) {
        this.x = x == null ? Optional.empty() : x;
    }

    public void setX(Double x) {
        this.x = Optional.ofNullable(x);
    }

    public void setY(Optional<Double> y) {
        this.y = y == null ? Optional.empty() : y;
    }

    public void setY(Double y) {
        this.y = Optional.ofNullable(y);
    }

    public void setZ(Optional<Double> z) {
        this.z = z == null ? Optional.empty() : z;
    }

    public void setZ(Double z) {
        this.z = Optional.ofNullable(z);
    }

    @Override
    public ImmutableOptionalVector toImmutable() {
        return new ImmutableOptionalVector(this.x, this.y, this.z);
    }

    @Override
    public MutableOptionalVector toMutable() {
        return new MutableOptionalVector(this.x, this.y, this.z);
    }

}