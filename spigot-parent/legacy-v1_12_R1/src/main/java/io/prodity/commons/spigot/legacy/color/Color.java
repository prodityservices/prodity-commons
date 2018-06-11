package io.prodity.commons.spigot.legacy.color;

public interface Color {

    int getRed();

    int getGreen();

    int getBlue();

    default org.bukkit.Color toBukkitColor() {
        return org.bukkit.Color.fromRGB(this.getRed(), this.getGreen(), this.getBlue());
    }

    MutableColor toMutable();

    ImmutableColor toImmutable();

}
