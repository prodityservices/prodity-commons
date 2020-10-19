package io.prodity.commons.spigot.legacy.particle.effect.collect;

import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffectPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface ParticleEffectCollection<T extends ParticleEffect> extends Collection<T>, ParticleEffectPlayer {

    @Override
    default void play(Location location) {
        this.forEach((effect) -> effect.play(location));
    }

    @Override
    default void playRange(Location location, int range) {
        this.forEach((effect) -> effect.playRange(location, range));
    }

    @Override
    default void playFor(Location location, Player... players) {
        this.forEach((effect) -> effect.playFor(location, players));
    }

    @Override
    default void playFor(Location location, Iterable<Player> players) {
        this.forEach((effect) -> effect.playFor(location, players));
    }

    @Override
    default void playRangeFor(Location location, int range, Player... players) {
        this.forEach((effect) -> effect.playRangeFor(location, range, players));
    }

    @Override
    default void playRangeFor(Location location, int range, Iterable<Player> players) {
        this.forEach((effect) -> effect.playRangeFor(location, range, players));
    }

}