package io.prodity.commons.spigot.legacy.particle.effect.offset;

import io.prodity.commons.spigot.legacy.particle.effect.MutableParticleEffect;
import io.prodity.commons.spigot.legacy.vector.optional.MutableOptionalVector;
import java.util.function.Function;
import org.bukkit.util.Vector;

public interface MutableOffsetParticleEffect extends OffsetParticleEffect, MutableParticleEffect {

    MutableOffsetParticleEffect setLocationOffset(MutableOptionalVector locationOffset);

    MutableOffsetParticleEffect modifyLocationOffset(Function<MutableOptionalVector, MutableOptionalVector> modifier);

    @Override
    MutableOptionalVector getLocationOffset();

    default MutableOffsetParticleEffect setLocationOffset(Vector vector) {
        return this.setLocationOffset(MutableOptionalVector.fromVector(vector));
    }

}