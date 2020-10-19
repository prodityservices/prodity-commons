package io.prodity.commons.spigot.legacy.sound.playable.impl;

import io.prodity.commons.spigot.legacy.sound.playable.AbstractPlayableSound;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Sound;

public class MutablePlayableSound extends AbstractPlayableSound {

    @Getter
    @Setter
    private final float volume;
    @Getter
    @Setter
    private final float pitch;
    @Getter
    @Setter
    @NonNull
    private final Sound sound;

    public MutablePlayableSound(@NonNull Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }


}
