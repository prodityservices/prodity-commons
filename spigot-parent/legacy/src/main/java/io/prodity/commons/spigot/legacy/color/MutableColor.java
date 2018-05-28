package io.prodity.commons.spigot.legacy.color;

public interface MutableColor extends Color {

    void setRed(int red) throws IllegalArgumentException;

    void setGreen(int green) throws IllegalArgumentException;

    void setBlue(int blue) throws IllegalArgumentException;

}
