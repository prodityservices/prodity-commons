package io.prodity.commons.spigot.legacy.sound.playable.impl;

import io.prodity.commons.spigot.legacy.sound.playable.AbstractPlayableSound;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Sound;

public class ImmutablePlayableSound extends AbstractPlayableSound {

    @Getter
    private final Sound sound;

    @Getter
    private final float volume;

    @Getter
    private final float pitch;

    public ImmutablePlayableSound(@NonNull Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

}
