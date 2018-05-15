package io.prodity.commons.color;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.util.Objects;

public class MutableColor implements Color {

    private int red;
    private int green;
    private int blue;

    public MutableColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public int getRed() {
        return this.red;
    }

    public void setRed(int red) throws IllegalStateException {
        Preconditions.checkState(red >= 0 && red <= 255, "specified RGB component red=" + red + " but must be 0-255 inclusive");
        this.red = red;
    }

    @Override
    public int getGreen() {
        return this.green;
    }

    public void setGreen(int green) throws IllegalStateException {
        Preconditions.checkState(green >= 0 && green <= 255, "specified RGB component green=" + green + " but must be 0-255 inclusive");
        this.green = green;
    }

    @Override
    public int getBlue() {
        return this.blue;
    }

    public void setBlue(int blue) throws IllegalStateException {
        Preconditions.checkState(blue >= 0 && blue <= 255, "specified RGB component blue=" + blue + " but must be 0-255 inclusive");
        this.blue = blue;
    }

    @Override
    public Color clone() {
        return new MutableColor(this.red, this.green, this.blue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.red, this.green, this.blue);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MutableColor)) {
            return false;
        }
        final MutableColor that = (MutableColor) object;
        return Objects.equals(this.red, that.red)
            && Objects.equals(this.green, that.green)
            && Objects.equals(this.blue, that.blue);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("red", this.red)
            .add("green", this.green)
            .add("blue", this.blue)
            .toString();
    }

}