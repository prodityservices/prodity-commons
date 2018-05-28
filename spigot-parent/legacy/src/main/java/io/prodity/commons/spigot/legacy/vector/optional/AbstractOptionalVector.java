package io.prodity.commons.spigot.legacy.vector.optional;

import org.bukkit.util.Vector;

public abstract class AbstractOptionalVector implements OptionalVector {

    @Override
    public Vector getBukkitVector() {
        final double x = this.getX().orElse(0.0);
        final double y = this.getY().orElse(0.0);
        final double z = this.getZ().orElse(0.0);
        return new Vector(x, y, z);
    }

}