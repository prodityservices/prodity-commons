package io.prodity.commons.spigot.legacy.particle.spawn;

import org.bukkit.Location;
import org.bukkit.Particle;

public interface ParticleSpawner<T> {

    void spawnParticle(T object, Particle particle, Location location, int count);

    <T1> void spawnParticle(T object, Particle particle, Location location, int count, T1 data);

    void spawnParticle(T object, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ);

    void spawnParticle(T object, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ,
        double extra);

    <T1> void spawnParticle(T object, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ,
        T1 data);

    <T1> void spawnParticle(T object, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ,
        double extra, T1 data);

}
