package io.prodity.commons.spigot.legacy.location.reference.yaml;

import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.types.bukkit.YamlSection;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlDouble;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlFloat;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlString;
import io.prodity.commons.spigot.legacy.location.reference.LocationStore;
import io.prodity.commons.spigot.legacy.location.reference.factory.LocationStoreFactory;
import io.prodity.commons.spigot.legacy.world.reference.WorldReference;
import io.prodity.commons.spigot.legacy.world.reference.factory.WorldReferenceFactory;
import org.bukkit.configuration.ConfigurationSection;

public abstract class YamlLocationStore<L extends LocationStore, W extends WorldReference> extends AbstractYamlType<L> {

    protected YamlLocationStore(Class<L> clazz) {
        super(clazz, false);
    }

    @Override
    protected L loadInternally(ConfigurationSection section, String path) throws YamlException {

        final ConfigurationSection locationSection = YamlSection.get().load(section, path);

        final String worldName = YamlString.get().load(locationSection, "world");
        final double x = YamlDouble.get().load(locationSection, "x");
        final double y = YamlDouble.get().load(locationSection, "y");
        final double z = YamlDouble.get().load(locationSection, "z");

        final float yaw = YamlFloat.get().loadOrDefault(locationSection, "yaw", 0F);
        final float pitch = YamlFloat.get().loadOrDefault(locationSection, "pitch", 0F);

        final W worldReference = this.getWorldReferenceFactory().byName(worldName);
        return this.getLocationStoreFactory().of(worldReference, x, y, z, yaw, pitch);
    }

    protected abstract WorldReferenceFactory<? extends W, ? extends W> getWorldReferenceFactory();

    protected abstract LocationStoreFactory<W, L> getLocationStoreFactory();

}