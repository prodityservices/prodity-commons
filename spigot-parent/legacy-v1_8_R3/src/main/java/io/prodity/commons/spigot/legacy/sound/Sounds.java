package io.prodity.commons.spigot.legacy.sound;

import io.prodity.commons.spigot.legacy.sound.playable.impl.ImmutablePlayableSound;
import io.prodity.commons.spigot.legacy.sound.playable.impl.MutablePlayableSound;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public enum Sounds {

    ;

    public static MutablePlayableSound mutable(Sound sound) {
        return new MutablePlayableSound(sound, 1F, 1F);
    }

    public static MutablePlayableSound mutable(Sound sound, float volume, float pitch) {
        return new MutablePlayableSound(sound, volume, pitch);
    }

    public static MutablePlayableSound mutableWithVolume(Sound sound, float volume) {
        return new MutablePlayableSound(sound, volume, 1F);
    }

    public static MutablePlayableSound mutableWithPitch(Sound sound, float pitch) {
        return new MutablePlayableSound(sound, 1F, pitch);
    }

    public static ImmutablePlayableSound immutable(Sound sound) {
        return new ImmutablePlayableSound(sound, 1F, 1F);
    }

    public static ImmutablePlayableSound immutable(Sound sound, float volume, float pitch) {
        return new ImmutablePlayableSound(sound, volume, pitch);
    }

    public static ImmutablePlayableSound immutableWithVolume(Sound sound, float volume) {
        return new ImmutablePlayableSound(sound, volume, 1F);
    }

    public static ImmutablePlayableSound immutableWithPitch(Sound sound, float pitch) {
        return new ImmutablePlayableSound(sound, 1F, pitch);
    }

    public static void play(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public static void play(Player player, Sound sound) {
        Sounds.play(player, sound, 1F, 1F);
    }

    public static void playWithVolume(Player player, Sound sound, float volume) {
        Sounds.play(player, sound, volume, 1F);
    }

    public static void playWithPitch(Player player, Sound sound, float pitch) {
        Sounds.play(player, sound, 1F, pitch);
    }

    public static void play(Location location, Sound sound, float volume, float pitch) {
        location.getWorld().playSound(location, sound, volume, pitch);
    }

    public static void play(Location location, Sound sound) {
        Sounds.play(location, sound, 1F, 1F);
    }

    public static void playWithVolume(Location location, Sound sound, float volume) {
        Sounds.play(location, sound, volume, 1F);
    }

    public static void playWithPitch(Location location, Sound sound, float pitch) {
        Sounds.play(location, sound, 1F, pitch);
    }

    public static void play(Location location, Sound sound, float volume, float pitch, Iterable<Player> players) {
        players.forEach((player) -> player.playSound(location, sound, volume, pitch));
    }

    public static void play(Location location, Sound sound, Iterable<Player> players) {
        Sounds.play(location, sound, 1F, 1F, players);
    }

    public static void playWithVolume(Location location, Sound sound, float volume, Iterable<Player> players) {
        Sounds.play(location, sound, volume, 1F, players);
    }

    public static void playWithPitch(Location location, Sound sound, float pitch, Iterable<Player> players) {
        Sounds.play(location, sound, 1F, pitch, players);
    }

    public static void play(Location location, Sound sound, float volume, float pitch, Player... players) {
        for (Player player : players) {
            player.playSound(location, sound, volume, pitch);
        }
    }

    public static void play(Location location, Sound sound, Player... players) {
        Sounds.play(location, sound, 1F, 1F, players);
    }

    public static void playWithVolume(Location location, Sound sound, float volume, Player... players) {
        Sounds.play(location, sound, volume, 1F, players);
    }

    public static void playWithPitch(Location location, Sound sound, float pitch, Player... players) {
        Sounds.play(location, sound, 1F, pitch, players);
    }

}