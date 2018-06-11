package io.prodity.commons.spigot.legacy.particle.spawn;

import io.prodity.commons.spigot.legacy.particle.Particle;
import io.prodity.commons.spigot.legacy.particle.effect.data.ParticleData;
import org.bukkit.Location;
import org.bukkit.World;

public class WorldParticleSpawner implements ParticleSpawner<World> {

    private static WorldParticleSpawner instance;

    public static WorldParticleSpawner getInstance() {
        if (WorldParticleSpawner.instance == null) {
            WorldParticleSpawner.instance = new WorldParticleSpawner();
        }
        return WorldParticleSpawner.instance;
    }

    private WorldParticleSpawner() {
    }

    @Override
    public void spawnParticle(World world, Particle particle, Location location, int count) {
        particle.display(0, 0, 0, 0, count, location, 256);
    }

    @Override
    public void spawnParticle(World world, Particle particle, Location location, int count, double offsetX, double offsetY,
        double offsetZ) {
        particle.display((float) offsetX, (float) offsetY, (float) offsetZ, 0F, count, location, 256);
    }

    @Override
    public void spawnParticle(World world, Particle particle, Location location, int count, double offsetX, double offsetY,
        double offsetZ, double extra) {
        particle.display((float) offsetX, (float) offsetY, (float) offsetZ, (float) extra, count, location, 256);
    }

    @Override
    public void spawnParticle(World world, Particle particle, Location location, int count, double offsetX,
        double offsetY, double offsetZ, double extra, ParticleData<?> data) {
        final Particle.ParticleData particleData = data.toSendableData();
        particle.display(particleData, (float) offsetX, (float) offsetZ, (float) offsetX, (float) extra, count, location, 256);
    }

    @Override
    public void spawnParticle(World world, Particle particle, Location location, int count, double offsetX,
        double offsetY, double offsetZ, ParticleData<?> data) {
        final Particle.ParticleData particleData = data.toSendableData();
        particle.display(particleData, (float) offsetX, (float) offsetZ, (float) offsetX, 0F, count, location, 256);
    }

    @Override
    public void spawnParticle(World world, Particle particle, Location location, int count, ParticleData<?> data) {
        final Particle.ParticleData particleData = data.toSendableData();
        particle.display(particleData, 0F, 0F, 0F, 0F, count, location, 256);
    }

}