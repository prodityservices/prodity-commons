package io.prodity.commons.spigot.legacy.world.reference;

import io.prodity.commons.spigot.legacy.world.reference.factory.ImmutableWorldReferenceFactory;
import io.prodity.commons.spigot.legacy.world.reference.factory.MutableWorldReferenceFactory;

public class WorldReferences {

    private static WorldReferences instance;

    private static WorldReferences getInstance() {
        if (WorldReferences.instance == null) {
            WorldReferences.instance = new WorldReferences();
        }
        return WorldReferences.instance;
    }

    public static MutableWorldReferenceFactory mutableFactory() {
        return WorldReferences.getInstance().mutableReferenceFactory;
    }

    public static ImmutableWorldReferenceFactory immutableFactory() {
        return WorldReferences.getInstance().immutableReferenceFactory;
    }

    private final MutableWorldReferenceFactory mutableReferenceFactory;
    private final ImmutableWorldReferenceFactory immutableReferenceFactory;

    private WorldReferences() {
        this.mutableReferenceFactory = new MutableWorldReferenceFactory();
        this.immutableReferenceFactory = new ImmutableWorldReferenceFactory();
    }

}
