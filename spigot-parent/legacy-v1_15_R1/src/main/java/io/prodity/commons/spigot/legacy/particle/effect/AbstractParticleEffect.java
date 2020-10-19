package io.prodity.commons.spigot.legacy.particle.effect;

import com.google.common.collect.Sets;
import io.prodity.commons.spigot.legacy.particle.spawn.*;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public abstract class AbstractParticleEffect implements ParticleEffect {

    @Getter
    private final ParticleSpawnStrategyGenerator spawnStrategyGenerator;

    protected AbstractParticleEffect() {
        this.spawnStrategyGenerator = new ParticleSpawnStrategyGenerator(this);
    }

    private <T> void spawn(ParticleSpawner<T> spawner, T object, Location location) {
        this.getSpawnStrategy().spawn(spawner, object, location);
    }

    private void iterateRange(Location location, Iterable<Player> players, int range, Consumer<Player> consumer) {
        final double rangeSquared = range * range;
        final World world = location.getWorld();
        for (Player player : players) {
            if (!player.getWorld().equals(world)) {
                continue;
            }
            if (player.getLocation().distanceSquared(location) <= rangeSquared) {
                consumer.accept(player);
            }
        }
    }

    @Override
    public void play(Location location) {
        final Location spawnLocation = this.getLocationToPlayAt(location);
        this.spawn(WorldParticleSpawner.getInstance(), location.getWorld(), spawnLocation);
    }

    @Override
    public void playRange(Location location, int range) {
        final PlayerParticleSpawner spawner = PlayerParticleSpawner.getInstance();
        final Location spawnLocation = this.getLocationToPlayAt(location);
        this.iterateRange(spawnLocation, spawnLocation.getWorld().getPlayers(), range, (player) -> {
            this.spawn(spawner, player, spawnLocation);
        });
    }

    @Override
    public void playFor(Location location, Player... players) {
        final PlayerParticleSpawner spawner = PlayerParticleSpawner.getInstance();
        final Location spawnLocation = this.getLocationToPlayAt(location);
        final World world = spawnLocation.getWorld();
        for (Player player : players) {
            if (player.getWorld().equals(world)) {
                this.spawn(spawner, player, spawnLocation);
            }
        }
    }

    @Override
    public void playFor(Location location, Iterable<Player> players) {
        final PlayerParticleSpawner spawner = PlayerParticleSpawner.getInstance();
        final Location spawnLocation = this.getLocationToPlayAt(location);
        final World world = spawnLocation.getWorld();
        for (Player player : players) {
            if (player.getWorld().equals(world)) {
                this.spawn(spawner, player, spawnLocation);
            }
        }
    }

    @Override
    public void playRangeFor(Location location, int range, Player... players) {
        final PlayerParticleSpawner spawner = PlayerParticleSpawner.getInstance();
        final Location spawnLocation = this.getLocationToPlayAt(location);
        this.iterateRange(spawnLocation, Sets.newHashSet(players), range, (player) -> {
            this.spawn(spawner, player, spawnLocation);
        });
    }

    @Override
    public void playRangeFor(Location location, int range, Iterable<Player> players) {
        final PlayerParticleSpawner spawner = PlayerParticleSpawner.getInstance();
        final Location spawnLocation = this.getLocationToPlayAt(location);
        this.iterateRange(spawnLocation, players, range, (player) -> {
            this.spawn(spawner, player, spawnLocation);
        });
    }

    protected Location getLocationToPlayAt(Location location) {
        return location;
    }

    protected abstract ParticleSpawnStrategy getSpawnStrategy();

}