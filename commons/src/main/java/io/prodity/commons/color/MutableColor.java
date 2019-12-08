package io.prodity.commons.color;

import com.google.common.base.MoreObjects;
import io.prodity.commons.config.annotate.deserialize.ConfigDeserializable;
import io.prodity.commons.config.annotate.inject.ConfigInject;
import io.prodity.commons.config.annotate.inject.Required;
import java.util.Objects;

@ConfigDeserializable
public class MutableColor implements Color {

    private int red;
    private int green;
    private int blue;

    @ConfigInject
    public MutableColor(@Required int red, @Required int green, @Required int blue) {
        Colors.verifyComponent("red", red);
        Colors.verifyComponent("green", green);
        Colors.verifyComponent("blue", blue);
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public int getRed() {
        return this.red;
    }

    public void setRed(int red) throws IllegalStateException {
        Colors.verifyComponent("red", red);
        this.red = red;
    }

    @Override
    public int getGreen() {
        return this.green;
    }

    public void setGreen(int green) throws IllegalStateException {
        Colors.verifyComponent("green", green);
        this.green = green;
    }

    @Override
    public int getBlue() {
        return this.blue;
    }

    public void setBlue(int blue) throws IllegalStateException {
        Colors.verifyComponent("blue", blue);
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