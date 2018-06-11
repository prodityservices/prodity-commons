package io.prodity.commons.spigot.legacy.particle.spawn;

import io.prodity.commons.spigot.legacy.particle.Particle;
import io.prodity.commons.spigot.legacy.particle.effect.data.ParticleData;
import org.bukkit.Location;

public interface ParticleSpawner<T> {

    void spawnParticle(T object, Particle particle, Location location, int count);

    void spawnParticle(T object, Particle particle, Location location, int count, ParticleData<?> data);

    void spawnParticle(T object, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ);

    void spawnParticle(T object, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ,
        double extra);

    void spawnParticle(T object, Particle particle, Location location, int count, double offsetX,
        double offsetY, double offsetZ, ParticleData<?> data);

    void spawnParticle(T object, Particle particle, Location location, int count, double offsetX,
        double offsetY, double offsetZ, double extra, ParticleData<?> data);

}
