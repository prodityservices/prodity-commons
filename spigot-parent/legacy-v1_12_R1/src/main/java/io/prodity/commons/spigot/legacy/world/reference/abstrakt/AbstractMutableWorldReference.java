package io.prodity.commons.spigot.legacy.world.reference.abstrakt;

import io.prodity.commons.spigot.legacy.world.reference.MutableWorldReference;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.World;

public abstract class AbstractMutableWorldReference<T> extends AbstractWorldReference<T> implements MutableWorldReference<T> {

    @Getter
    @Setter
    @NonNull
    protected T worldReferent;

    protected AbstractMutableWorldReference(@NonNull T worldReferent) {
        this.worldReferent = worldReferent;
    }

    @Override
    public void setWorldReferentFrom(World world) {
        this.worldReferent = this.worldToReference(world);
    }

}
