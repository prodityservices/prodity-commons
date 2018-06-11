package io.prodity.commons.spigot.legacy.sound.playable;

import org.bukkit.Sound;

public interface PlayableSound extends SoundPlayer {

    Sound getSound();

    float getVolume();

    float getPitch();

}
