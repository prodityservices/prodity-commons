package io.prodity.commons.spigot.legacy.particle.spawn;

import org.bukkit.Location;

@FunctionalInterface
public interface ParticleSpawnStrategy {

    <T> void spawn(ParticleSpawner<T> spawner, T subject, Location location);

}