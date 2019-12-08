package io.prodity.commons.spigot.legacy.particle.spawn;

import io.prodity.commons.spigot.legacy.particle.Particle;
import io.prodity.commons.spigot.legacy.particle.effect.data.ParticleData;
import org.bukkit.Location;
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
        particle.display(0, 0, 0, 0, count, location, player);
    }

    @Override
    public void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY,
        double offsetZ) {
        particle.display((float) offsetX, (float) offsetY, (float) offsetZ, 0F, count, location, player);
    }

    @Override
    public void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY,
        double offsetZ, double extra) {
        particle.display((float) offsetX, (float) offsetY, (float) offsetZ, (float) extra, count, location, player);
    }

    @Override
    public void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX,
        double offsetY, double offsetZ, double extra, ParticleData<?> data) {
        final Particle.ParticleData particleData = data.toSendableData();
        particle.display(particleData, (float) offsetX, (float) offsetZ, (float) offsetX, (float) extra, count, location, player);
    }

    @Override
    public void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX,
        double offsetY, double offsetZ, ParticleData<?> data) {
        final Particle.ParticleData particleData = data.toSendableData();
        particle.display(particleData, (float) offsetX, (float) offsetZ, (float) offsetX, 0F, count, location, player);
    }

    @Override
    public void spawnParticle(Player player, Particle particle, Location location, int count, ParticleData<?> data) {
        final Particle.ParticleData particleData = data.toSendableData();
        particle.display(particleData, 0F, 0F, 0F, 0F, count, location, player);
    }

}