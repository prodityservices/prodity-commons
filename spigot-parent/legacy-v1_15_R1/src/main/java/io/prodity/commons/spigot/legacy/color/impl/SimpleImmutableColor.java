package io.prodity.commons.spigot.legacy.color.impl;

import com.google.common.base.Objects;
import io.prodity.commons.spigot.legacy.color.Colors;
import io.prodity.commons.spigot.legacy.color.ImmutableColor;
import io.prodity.commons.spigot.legacy.color.MutableColor;
import lombok.Getter;

public class SimpleImmutableColor implements ImmutableColor {

    @Getter
    private final int red;

    @Getter
    private final int green;

    @Getter
    private final int blue;

    public SimpleImmutableColor(int red, int green, int blue) {
        Colors.verifyComponent(red, "red");
        Colors.verifyComponent(green, "green");
        Colors.verifyComponent(blue, "blue");
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public MutableColor toMutable() {
        return new SimpleMutableColor(this.red, this.green, this.blue);
    }

    @Override
    public ImmutableColor toImmutable() {
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.red, this.green, this.blue);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SimpleImmutableColor)) {
            return false;
        }
        final SimpleImmutableColor that = (SimpleImmutableColor) obj;
        return Objects.equal(this.red, that.red) &&
            Objects.equal(this.green, that.green) &&
            Objects.equal(this.blue, that.blue);
    }

}