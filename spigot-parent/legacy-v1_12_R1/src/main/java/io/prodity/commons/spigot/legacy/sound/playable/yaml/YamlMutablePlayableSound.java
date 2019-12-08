package io.prodity.commons.spigot.legacy.sound.playable.yaml;

import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.sound.Sounds;
import io.prodity.commons.spigot.legacy.sound.playable.impl.MutablePlayableSound;
import org.bukkit.Sound;

public class YamlMutablePlayableSound extends YamlPlayableSound<MutablePlayableSound> {

    public static YamlMutablePlayableSound get() {
        return YamlTypeCache.getType(YamlMutablePlayableSound.class);
    }

    public YamlMutablePlayableSound() {
        super(MutablePlayableSound.class);
    }

    @Override
    protected MutablePlayableSound create(Sound sound, float volume, float pitch) {
        return Sounds.mutable(sound, volume, pitch);
    }

}