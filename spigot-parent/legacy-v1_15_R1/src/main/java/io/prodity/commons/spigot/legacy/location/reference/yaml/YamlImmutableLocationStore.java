package io.prodity.commons.spigot.legacy.location.reference.yaml;

import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.location.reference.ImmutableLocationStore;
import io.prodity.commons.spigot.legacy.location.reference.LocationReferences;
import io.prodity.commons.spigot.legacy.location.reference.factory.ImmutableLocationStoreFactory;
import io.prodity.commons.spigot.legacy.world.reference.ImmutableWorldReference;
import io.prodity.commons.spigot.legacy.world.reference.WorldReferences;
import io.prodity.commons.spigot.legacy.world.reference.factory.ImmutableWorldReferenceFactory;

public class YamlImmutableLocationStore extends YamlLocationStore<ImmutableLocationStore, ImmutableWorldReference> {

    public static YamlImmutableLocationStore get() {
        return YamlTypeCache.getType(YamlImmutableLocationStore.class);
    }

    public YamlImmutableLocationStore() {
        super(ImmutableLocationStore.class);
    }

    @Override
    protected ImmutableWorldReferenceFactory getWorldReferenceFactory() {
        return WorldReferences.immutableFactory();
    }

    @Override
    protected ImmutableLocationStoreFactory getLocationStoreFactory() {
        return LocationReferences.immutableFactory();
    }

}