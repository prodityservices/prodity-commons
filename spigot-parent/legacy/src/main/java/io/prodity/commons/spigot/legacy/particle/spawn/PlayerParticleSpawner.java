package io.prodity.commons.spigot.legacy.particle.spawn;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class PlayerParticleSpawner implements ParticleSpawner<Player> {

    private static PlayerParticleSpawner instance;

    public static PlayerParticleSpawner getInstance() {
        if (PlayerParticleSpawner.instance == null) {
            PlayerParticleSpawner.instance = new PlayerParticleSpawner();
        }
        return PlayerParticleSpawner.instance;
    }

    private PlayerParticleSpawner() {
    }

    @Override
    public void spawnParticle(Player player, Particle particle, Location location, int count) {
        player.spawnParticle(particle, location, count);
    }

    @Override
    public void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY,
        double offsetZ) {
        player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ);
    }

    @Override
    public void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY,
        double offsetZ,
        double extra) {
        player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra);
    }

    @Override
    public <T1> void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY,
        double offsetZ, double extra, T1 data) {
        player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, data);
    }

    @Override
    public <T1> void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY,
        double offsetZ, T1 data) {
        player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, data);
    }

    @Override
    public <T1> void spawnParticle(Player player, Particle particle, Location location, int count, T1 data) {
        player.spawnParticle(particle, location, count, data);
    }

}