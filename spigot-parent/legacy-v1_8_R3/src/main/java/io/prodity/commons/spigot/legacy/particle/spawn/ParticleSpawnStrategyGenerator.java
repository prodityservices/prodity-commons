package io.prodity.commons.spigot.legacy.particle.spawn;

import io.prodity.commons.spigot.legacy.particle.Particle;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.data.ParticleData;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ParticleSpawnStrategyGenerator {

    private final ParticleEffect effect;

    public ParticleSpawnStrategyGenerator(ParticleEffect effect) {
        this.effect = effect;
    }

    public ParticleSpawnStrategy generate() {
        final Particle particle = this.effect.getParticle();
        final int amount = this.effect.getAmount();
        final Optional<? extends ParticleData<?>> data = this.effect.getData();
        final Optional<Double> speed = this.effect.getSpeed();

        final boolean dataPresent = data.isPresent();
        final boolean speedPresent = speed.isPresent();
        final boolean offsetPresent = this.effect.getOffsets().isAnyPresent();
        final Vector offsets = this.effect.getOffsets().getBukkitVector();

        if (speedPresent && dataPresent && offsetPresent) {
            return new ParticleSpawnStrategy() {
                @Override
                public <T> void spawn(ParticleSpawner<T> spawner, T subject, Location location) {
                    spawner.spawnParticle(subject, particle, location, amount, offsets.getX(), offsets.getY(), offsets.getZ(), speed.get(),
                        data.get());
                }
            };
        } else if (offsetPresent && dataPresent) {
            return new ParticleSpawnStrategy() {
                @Override
                public <T> void spawn(ParticleSpawner<T> spawner, T subject, Location location) {
                    spawner.spawnParticle(subject, particle, location, amount, offsets.getX(), offsets.getY(), offsets.getZ(),
                        data.get());
                }
            };
        } else if (offsetPresent && speedPresent) {
            return new ParticleSpawnStrategy() {
                @Override
                public <T> void spawn(ParticleSpawner<T> spawner, T subject, Location location) {
                    spawner.spawnParticle(subject, particle, location, amount, offsets.getX(), offsets.getY(), offsets.getZ(),
                        speed.get());
                }
            };
        } else if (dataPresent) {
            return new ParticleSpawnStrategy() {
                @Override
                public <T> void spawn(ParticleSpawner<T> spawner, T subject, Location location) {
                    spawner.spawnParticle(subject, particle, location, amount, data.get());
                }
            };
        } else {
            return new ParticleSpawnStrategy() {
                @Override
                public <T> void spawn(ParticleSpawner<T> spawner, T subject, Location location) {
                    spawner.spawnParticle(subject, particle, location, amount);
                }
            };
        }
    }

}
