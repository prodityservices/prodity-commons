package io.prodity.commons.spigot.legacy.particle.effect;

import io.prodity.commons.spigot.legacy.location.reference.LocationReference;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface ParticleEffectPlayer {

    void play(Location location);

    default void play(LocationReference locationReference) {
        this.play(locationReference.getBukkitLocation());
    }

    void playRange(Location location, int range);

    default void playRange(LocationReference locationReference, int range) {
        this.playRangeFor(locationReference.getBukkitLocation(), range);
    }

    void playFor(Location location, Player... players);

    default void playFor(LocationReference locationReference, Player... players) {
        this.playFor(locationReference.getBukkitLocation(), players);
    }

    void playFor(Location location, Iterable<Player> players);

    default void playFor(LocationReference locationReference, Iterable<Player> players) {
        this.playFor(locationReference, players);
    }

    void playRangeFor(Location location, int range, Player... players);

    default void playRangeFor(LocationReference locationReference, int range, Player... players) {
        this.playRangeFor(locationReference.getBukkitLocation(), range, players);
    }

    void playRangeFor(Location location, int range, Iterable<Player> players);

    default void playRangeFor(LocationReference locationReference, int range, Iterable<Player> players) {
        this.playRangeFor(locationReference.getBukkitLocation(), range, players);
    }

}
