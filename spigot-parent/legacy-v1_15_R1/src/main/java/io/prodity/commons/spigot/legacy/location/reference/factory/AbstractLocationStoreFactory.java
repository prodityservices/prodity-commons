package io.prodity.commons.spigot.legacy.location.reference.factory;

import io.prodity.commons.spigot.legacy.location.reference.LocationStore;
import io.prodity.commons.spigot.legacy.world.reference.WorldReference;
import io.prodity.commons.spigot.legacy.world.reference.factory.WorldReferenceFactory;
import lombok.Getter;

public abstract class AbstractLocationStoreFactory<W extends WorldReference, L extends LocationStore> implements
    LocationStoreFactory<W, L> {

    @Getter
    private final WorldReferenceFactory<? extends W, ? extends W> worldReferenceFactory;

    protected AbstractLocationStoreFactory(WorldReferenceFactory<? extends W, ? extends W> worldReferenceFactory) {
        this.worldReferenceFactory = worldReferenceFactory;
    }

}