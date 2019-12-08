package io.prodity.commons.spigot.legacy.particle.effect;

import com.google.common.collect.ImmutableSet;
import io.prodity.commons.spigot.legacy.lazy.LazyValue;
import io.prodity.commons.spigot.legacy.lazy.SimpleLazyValue;
import io.prodity.commons.spigot.legacy.particle.Particle;
import io.prodity.commons.spigot.legacy.particle.effect.color.ParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.color.note.ParticleNoteColor;
import io.prodity.commons.spigot.legacy.particle.effect.color.ordinary.OrdinaryParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.data.ParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.data.block.BlockParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.data.item.ItemParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.factory.ImmutableParticleFactory;
import io.prodity.commons.spigot.legacy.particle.effect.factory.MutableParticleFactory;
import java.util.Set;

public enum ParticleEffects {

    ;

    public static final Set<Particle> ORDINARY_COLOR_PARTICLES = ImmutableSet.<Particle>builder()
        .add(Particle.REDSTONE)
        .add(Particle.SPELL_MOB)
        .add(Particle.SPELL_MOB_AMBIENT)
        .build();
    public static final Set<Particle> NOTE_COLOR_PARTICLES = ImmutableSet.<Particle>builder()
        .add(Particle.NOTE)
        .build();
    public static final Set<Particle> BLOCK_DATA_PARTICLES = ImmutableSet.<Particle>builder()
        .add(Particle.BLOCK_CRACK)
        .add(Particle.BLOCK_DUST)
        .build();
    public static final Set<Particle> ITEM_DATA_PARTICLES = ImmutableSet.<Particle>builder()
        .add(Particle.ITEM_CRACK)
        .build();
    public static final Set<Particle> DIRECTIONAL_PARTICLES = ImmutableSet.<Particle>builder()
        .add(Particle.EXPLOSION_NORMAL)
        .add(Particle.FIREWORKS_SPARK)
        .add(Particle.WATER_BUBBLE)
        .add(Particle.WATER_SPLASH)
        .add(Particle.WATER_WAKE)
        .add(Particle.SUSPENDED_DEPTH)
        .add(Particle.CRIT)
        .add(Particle.CRIT_MAGIC)
        .add(Particle.SMOKE_NORMAL)
        .add(Particle.SMOKE_LARGE)
        .add(Particle.VILLAGER_HAPPY)
        .add(Particle.TOWN_AURA)
        .add(Particle.PORTAL)
        .add(Particle.ENCHANTMENT_TABLE)
        .add(Particle.FLAME)
        .add(Particle.CLOUD)
        .add(Particle.SNOW_SHOVEL)
        .add(Particle.ITEM_CRACK)
        .add(Particle.BLOCK_DUST)
        .build();
    private static final LazyValue<MutableParticleFactory> mutableFactory = new SimpleLazyValue<>(MutableParticleFactory::new);
    private static final LazyValue<ImmutableParticleFactory> immutableFactory = new SimpleLazyValue<>(ImmutableParticleFactory::new);

    public static MutableParticleFactory mutable() {
        return ParticleEffects.mutableFactory.get();
    }

    public static ImmutableParticleFactory immutable() {
        return ParticleEffects.immutableFactory.get();
    }

    public static void verifyAmount(int amount) throws IllegalArgumentException {
        if (amount < 0) {
            throw new IllegalArgumentException("the particle amount '" + amount + "' must be >=0");
        }
    }

    public static ParticleData<?> convertData(Particle particle, ParticleData<?> data) throws IllegalArgumentException {
        ParticleEffects.verifyDataRequired(particle);
        if (ParticleEffects.ITEM_DATA_PARTICLES.contains(particle)) {
            return data instanceof ItemParticleData ? data : data.toItemData();
        } else {
            return data instanceof BlockParticleData ? data : data.toBlockData();
        }
    }

    public static boolean isValidData(Particle particle, ParticleData data) throws IllegalArgumentException {
        if (data instanceof BlockParticleData) {
            return ParticleEffects.BLOCK_DATA_PARTICLES.contains(particle);
        }
        if (data instanceof ItemParticleData) {
            return ParticleEffects.ITEM_DATA_PARTICLES.contains(particle);
        }
        throw new IllegalArgumentException("unknown ParticleData type: " + data.getClass().getName());
    }

    public static void verifyData(Particle particle, ParticleData data) throws IllegalArgumentException {
        if (data instanceof BlockParticleData) {
            if (!ParticleEffects.BLOCK_DATA_PARTICLES.contains(particle)) {
                throw new IllegalArgumentException("particle type '" + particle.name() + "' can not have BlockParticleData");
            }
        }
        if (data instanceof ItemParticleData) {
            if (!ParticleEffects.ITEM_DATA_PARTICLES.contains(particle)) {
                throw new IllegalArgumentException("particle type '" + particle.name() + "' can not have ItemParticleData");
            }
        }
    }

    public static boolean requiresData(Particle particle) {
        return ParticleEffects.BLOCK_DATA_PARTICLES.contains(particle) || ParticleEffects.ITEM_DATA_PARTICLES.contains(particle);
    }


    public static void verifyDataRequired(Particle particle) throws IllegalArgumentException {
        if (!ParticleEffects.requiresData(particle)) {
            throw new IllegalArgumentException("particle '" + particle.name() + "' can not have ParticleData");
        }
    }

    public static void verifyNoDataRequired(Particle particle) throws IllegalArgumentException {
        if (ParticleEffects.requiresData(particle)) {
            throw new IllegalArgumentException("particle '" + particle.name() + "' requires ParticleData");
        }
    }

    public static boolean isValidColor(Particle particle, ParticleColor color) throws IllegalArgumentException {
        if (color instanceof OrdinaryParticleColor) {
            return ParticleEffects.ORDINARY_COLOR_PARTICLES.contains(particle);
        }
        if (color instanceof ParticleNoteColor) {
            return ParticleEffects.NOTE_COLOR_PARTICLES.contains(particle);
        }
        throw new IllegalArgumentException("unknown ParticleColor type: " + color.getClass().getName());
    }

    public static void verifyColor(Particle particle, ParticleColor color) throws IllegalArgumentException {
        if (color instanceof OrdinaryParticleColor) {
            if (!ParticleEffects.ORDINARY_COLOR_PARTICLES.contains(particle)) {
                throw new IllegalArgumentException("particle type '" + particle.name() + "' can not have a color");
            }
        }
        if (color instanceof ParticleNoteColor) {
            if (!ParticleEffects.NOTE_COLOR_PARTICLES.contains(particle)) {
                throw new IllegalArgumentException("particle type '" + particle.name() + "' can not have a note color");
            }
        }
    }

    public static boolean isDirectional(Particle particle) {
        return ParticleEffects.DIRECTIONAL_PARTICLES.contains(particle);
    }

    public static void verifyDirectional(Particle particle) {
        if (!ParticleEffects.isDirectional(particle)) {
            throw new IllegalArgumentException("the particle '" + particle.name() + "' is not directional");
        }
    }

}