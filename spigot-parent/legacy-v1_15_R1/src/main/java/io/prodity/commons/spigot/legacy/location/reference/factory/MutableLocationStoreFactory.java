package io.prodity.commons.spigot.legacy.location.reference.factory;

import io.prodity.commons.spigot.legacy.location.reference.MutableLocationStore;
import io.prodity.commons.spigot.legacy.location.reference.simple.SimpleMutableLocationStore;
import io.prodity.commons.spigot.legacy.world.reference.MutableWorldReference;
import io.prodity.commons.spigot.legacy.world.reference.WorldReference;
import io.prodity.commons.spigot.legacy.world.reference.factory.MutableWorldReferenceFactory;

public class MutableLocationStoreFactory extends AbstractLocationStoreFactory<MutableWorldReference, MutableLocationStore> {

    public MutableLocationStoreFactory(MutableWorldReferenceFactory worldReferenceFactory) {
        super(worldReferenceFactory);
    }

    @Override
    public MutableLocationStore of(WorldReference<?> worldReference, double x, double y, double z, float yaw, float pitch) {
        final MutableWorldReference<?> mutableWorldReference;
        if (worldReference instanceof MutableWorldReference) {
            mutableWorldReference = (MutableWorldReference<?>) worldReference;
        } else {
            mutableWorldReference = this.getWorldReferenceFactory().byNameFromExisting(worldReference);
        }
        return new SimpleMutableLocationStore(mutableWorldReference, x, y, z, yaw, pitch);
    }

}