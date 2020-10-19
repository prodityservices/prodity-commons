package io.prodity.commons.spigot.legacy.location.reference.factory;

import io.prodity.commons.spigot.legacy.location.reference.ImmutableLocationStore;
import io.prodity.commons.spigot.legacy.location.reference.simple.SimpleImmutableLocationStore;
import io.prodity.commons.spigot.legacy.world.reference.ImmutableWorldReference;
import io.prodity.commons.spigot.legacy.world.reference.WorldReference;
import io.prodity.commons.spigot.legacy.world.reference.factory.ImmutableWorldReferenceFactory;

public class ImmutableLocationStoreFactory extends AbstractLocationStoreFactory<ImmutableWorldReference, ImmutableLocationStore> {

    public ImmutableLocationStoreFactory(ImmutableWorldReferenceFactory worldReferenceFactory) {
        super(worldReferenceFactory);
    }

    @Override
    public ImmutableLocationStore of(WorldReference<?> worldReference, double x, double y, double z, float yaw, float pitch) {
        final ImmutableWorldReference<?> immutableWorldReference;
        if (worldReference instanceof ImmutableWorldReference) {
            immutableWorldReference = (ImmutableWorldReference<?>) worldReference;
        } else {
            immutableWorldReference = this.getWorldReferenceFactory().byNameFromExisting(worldReference);
        }
        return new SimpleImmutableLocationStore(immutableWorldReference, x, y, z, yaw, pitch);
    }

}