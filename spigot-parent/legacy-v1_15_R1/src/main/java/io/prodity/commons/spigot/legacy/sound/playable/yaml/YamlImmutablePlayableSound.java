package io.prodity.commons.spigot.legacy.sound.playable.yaml;

import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.sound.Sounds;
import io.prodity.commons.spigot.legacy.sound.playable.impl.ImmutablePlayableSound;
import org.bukkit.Sound;

public class YamlImmutablePlayableSound extends YamlPlayableSound<ImmutablePlayableSound> {

    public static YamlImmutablePlayableSound get() {
        return YamlTypeCache.getType(YamlImmutablePlayableSound.class);
    }

    public YamlImmutablePlayableSound() {
        super(ImmutablePlayableSound.class);
    }

    @Override
    protected ImmutablePlayableSound create(Sound sound, float volume, float pitch) {
        return Sounds.immutable(sound, volume, pitch);
    }

}