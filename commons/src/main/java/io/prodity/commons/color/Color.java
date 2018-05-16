package io.prodity.commons.color;

import java.util.Comparator;

/**
 * Represents an RGB color.<br>Test
 */
public interface Color extends Comparable<Color>, Cloneable {

    Comparator<Color> COMPARATOR = Comparator.comparing(Color::getRed)
        .thenComparing(Color::getBlue)
        .thenComparing(Color::getGreen);

    int getRed();

    int getGreen();

    int getBlue();

    Color clone();

    @Override
    default int compareTo(Color color) {
        return Color.COMPARATOR.compare(this, color);
    }

}