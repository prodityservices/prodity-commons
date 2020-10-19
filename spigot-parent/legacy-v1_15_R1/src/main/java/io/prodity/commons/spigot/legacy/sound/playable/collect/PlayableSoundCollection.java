package io.prodity.commons.spigot.legacy.sound.playable.collect;

import io.prodity.commons.spigot.legacy.sound.playable.PlayableSound;
import io.prodity.commons.spigot.legacy.sound.playable.SoundPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface PlayableSoundCollection<T extends PlayableSound> extends Collection<T>, SoundPlayer {

    @Override
    default void play(Player player) {
        this.forEach((sound) -> sound.play(player));
    }

    @Override
    default void play(Location location) {
        this.forEach((sound) -> sound.play(location));
    }

    @Override
    default void play(Location location, Player... players) {
        this.forEach((sound) -> sound.play(location, players));
    }

    @Override
    default void play(Location location, Iterable<Player> players) {
        this.forEach((sound) -> sound.play(location, players));
    }

}