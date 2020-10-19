package io.prodity.commons.spigot.legacy.location.reference.yaml;

import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.location.reference.LocationReferences;
import io.prodity.commons.spigot.legacy.location.reference.MutableLocationStore;
import io.prodity.commons.spigot.legacy.location.reference.factory.MutableLocationStoreFactory;
import io.prodity.commons.spigot.legacy.world.reference.MutableWorldReference;
import io.prodity.commons.spigot.legacy.world.reference.WorldReferences;
import io.prodity.commons.spigot.legacy.world.reference.factory.MutableWorldReferenceFactory;

public class YamlMutableLocationStore extends YamlLocationStore<MutableLocationStore, MutableWorldReference> {

    public static YamlMutableLocationStore get() {
        return YamlTypeCache.getType(YamlMutableLocationStore.class);
    }

    public YamlMutableLocationStore() {
        super(MutableLocationStore.class);
    }

    @Override
    protected MutableWorldReferenceFactory getWorldReferenceFactory() {
        return WorldReferences.mutableFactory();
    }

    @Override
    protected MutableLocationStoreFactory getLocationStoreFactory() {
        return LocationReferences.mutableFactory();
    }

}