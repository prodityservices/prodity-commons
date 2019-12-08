package io.prodity.commons.spigot.legacy.world.reference.abstrakt;

import io.prodity.commons.spigot.legacy.world.reference.WorldReference;
import org.bukkit.World;

public abstract class AbstractWorldReference<T> implements WorldReference<T> {

    protected abstract T worldToReference(World world);

    protected abstract World worldFromReference(T worldReferent);

    @Override
    public World getBukkitWorld() {
        return this.worldFromReference(this.getWorldReferent());
    }

}
