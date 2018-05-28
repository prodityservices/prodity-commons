package io.prodity.commons.spigot.legacy.sound.playable.yaml;

import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.types.bukkit.YamlSection;
import io.prodity.commons.spigot.legacy.config.yaml.types.enumeration.YamlEnum;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlDouble;
import io.prodity.commons.spigot.legacy.sound.playable.PlayableSound;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;

public abstract class YamlPlayableSound<T extends PlayableSound> extends AbstractYamlType<T> {

    public static final class Keys {

        public static final String SOUND = "sound";
        public static final String VOLUME = "volume";
        public static final String PITCH = "pitch";

    }

    protected YamlPlayableSound(Class<T> clazz) {
        super(clazz, false);
    }

    private float loadFloat(ConfigurationSection section, String path) throws YamlException {
        if (section.contains(path)) {
            return YamlDouble.get().load(section, path).floatValue();
        } else {
            return 1F;
        }
    }

    @Override
    public T loadInternally(ConfigurationSection section, String path) throws YamlException {
        final ConfigurationSection soundSection = YamlSection.get().load(section, path);

        final Sound sound = YamlEnum.get(Sound.class).load(soundSection, Keys.SOUND);
        final float volume = this.loadFloat(soundSection, Keys.VOLUME);
        final float pitch = this.loadFloat(soundSection, Keys.PITCH);

        return this.create(sound, volume, pitch);
    }

    protected abstract T create(Sound sound, float volume, float pitch);

}
