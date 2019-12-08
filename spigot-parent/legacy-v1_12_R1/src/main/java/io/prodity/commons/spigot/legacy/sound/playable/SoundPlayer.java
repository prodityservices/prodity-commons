package io.prodity.commons.spigot.legacy.sound.playable;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface SoundPlayer {

    void play(Player player);

    void play(Location location);

    void play(Location location, Player... players);

    void play(Location location, Iterable<Player> players);

}