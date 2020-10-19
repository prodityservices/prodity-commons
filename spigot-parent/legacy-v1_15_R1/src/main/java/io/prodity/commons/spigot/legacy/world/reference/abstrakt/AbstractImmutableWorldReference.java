package io.prodity.commons.spigot.legacy.world.reference.abstrakt;

import io.prodity.commons.spigot.legacy.world.reference.ImmutableWorldReference;
import lombok.Getter;
import lombok.NonNull;

public abstract class AbstractImmutableWorldReference<T> extends AbstractWorldReference<T> implements ImmutableWorldReference<T> {

    @Getter
    @NonNull
    protected final T worldReferent;

    protected AbstractImmutableWorldReference(@NonNull T worldReferent) {
        this.worldReferent = worldReferent;
    }

}
