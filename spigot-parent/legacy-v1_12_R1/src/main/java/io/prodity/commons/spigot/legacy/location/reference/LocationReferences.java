package io.prodity.commons.spigot.legacy.location.reference;

import io.prodity.commons.spigot.legacy.location.reference.factory.ImmutableLocationStoreFactory;
import io.prodity.commons.spigot.legacy.location.reference.factory.MutableLocationStoreFactory;
import io.prodity.commons.spigot.legacy.world.reference.WorldReferences;

public class LocationReferences {

    private static LocationReferences instance;

    private static LocationReferences getInstance() {
        if (LocationReferences.instance == null) {
            LocationReferences.instance = new LocationReferences();
        }
        return LocationReferences.instance;
    }

    public static MutableLocationStoreFactory mutableFactory() {
        return LocationReferences.getInstance().mutableReferenceFactory;
    }

    public static ImmutableLocationStoreFactory immutableFactory() {
        return LocationReferences.getInstance().immutableReferenceFactory;
    }

    private final MutableLocationStoreFactory mutableReferenceFactory;
    private final ImmutableLocationStoreFactory immutableReferenceFactory;

    private LocationReferences() {
        this.mutableReferenceFactory = new MutableLocationStoreFactory(WorldReferences.mutableFactory());
        this.immutableReferenceFactory = new ImmutableLocationStoreFactory(WorldReferences.immutableFactory());
    }

}