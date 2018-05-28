package io.prodity.commons.spigot.legacy.particle.spawn;

import org.bukkit.Location;
import org.bukkit.Particle;
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
        world.spawnParticle(particle, location, count);
    }

    @Override
    public void spawnParticle(World world, Particle particle, Location location, int count, double offsetX, double offsetY,
        double offsetZ) {
        world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ);
    }

    @Override
    public void spawnParticle(World world, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ,
        double extra) {
        world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra);
    }

    @Override
    public <T1> void spawnParticle(World world, Particle particle, Location location, int count, double offsetX, double offsetY,
        double offsetZ, double extra, T1 data) {
        world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, data);
    }

    @Override
    public <T1> void spawnParticle(World world, Particle particle, Location location, int count, double offsetX, double offsetY,
        double offsetZ, T1 data) {
        world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, data);
    }

    @Override
    public <T1> void spawnParticle(World world, Particle particle, Location location, int count, T1 data) {
        world.spawnParticle(particle, location, count, data);
    }

}