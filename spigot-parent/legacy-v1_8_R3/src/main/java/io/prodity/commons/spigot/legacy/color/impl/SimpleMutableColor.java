package io.prodity.commons.spigot.legacy.color.impl;

import com.google.common.base.Objects;
import io.prodity.commons.spigot.legacy.color.Colors;
import io.prodity.commons.spigot.legacy.color.ImmutableColor;
import io.prodity.commons.spigot.legacy.color.MutableColor;
import lombok.Getter;

public class SimpleMutableColor implements MutableColor {

    @Getter
    private int red;

    @Getter
    private int green;

    @Getter
    private int blue;

    public SimpleMutableColor(int red, int green, int blue) {
        Colors.verifyComponent(red, "red");
        Colors.verifyComponent(green, "green");
        Colors.verifyComponent(blue, "blue");
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public void setRed(int red) throws IllegalArgumentException {
        Colors.verifyComponent(red, "red");
        this.red = red;
    }

    @Override
    public void setGreen(int green) throws IllegalArgumentException {
        Colors.verifyComponent(green, "green");
        this.green = green;
    }

    @Override
    public void setBlue(int blue) throws IllegalArgumentException {
        Colors.verifyComponent(blue, "blue");
        this.blue = blue;
    }

    @Override
    public ImmutableColor toImmutable() {
        return new SimpleImmutableColor(this.red, this.green, this.blue);
    }

    @Override
    public MutableColor toMutable() {
        return new SimpleMutableColor(this.red, this.green, this.blue);
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
        if (!(obj instanceof SimpleMutableColor)) {
            return false;
        }
        final SimpleMutableColor that = (SimpleMutableColor) obj;
        return Objects.equal(this.red, that.red) &&
            Objects.equal(this.green, that.green) &&
            Objects.equal(this.blue, that.blue);
    }

}