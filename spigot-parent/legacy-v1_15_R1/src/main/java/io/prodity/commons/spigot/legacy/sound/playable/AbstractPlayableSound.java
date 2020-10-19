package io.prodity.commons.spigot.legacy.sound.playable;

import io.prodity.commons.spigot.legacy.sound.Sounds;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class AbstractPlayableSound implements PlayableSound {

    @Override
    public void play(Player player) {
        Sounds.play(player, this.getSound(), this.getVolume(), this.getPitch());
    }

    @Override
    public void play(Location location) {
        Sounds.play(location, this.getSound(), this.getVolume(), this.getPitch());
    }

    @Override
    public void play(Location location, Player... players) {
        Sounds.play(location, this.getSound(), this.getVolume(), this.getPitch(), players);
    }

    @Override
    public void play(Location location, Iterable<Player> players) {
        Sounds.play(location, this.getSound(), this.getVolume(), this.getPitch(), players);
    }

}