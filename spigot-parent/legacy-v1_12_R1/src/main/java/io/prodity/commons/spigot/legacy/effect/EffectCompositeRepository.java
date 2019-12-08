package io.prodity.commons.spigot.legacy.effect;

import io.prodity.commons.spigot.legacy.message.composite.MessageComposite;
import io.prodity.commons.spigot.legacy.message.composite.MessageCompositeRepository;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleRepository;
import io.prodity.commons.spigot.legacy.sound.playable.PlayableSound;
import io.prodity.commons.spigot.legacy.sound.playable.PlayableSoundRepository;

public class EffectCompositeRepository {

    private final MessageCompositeRepository messages;
    private final ParticleRepository particles;
    private final PlayableSoundRepository sounds;

    public EffectCompositeRepository(MessageCompositeRepository messages, ParticleRepository particles, PlayableSoundRepository sounds) {
        this.messages = messages;
        this.particles = particles;
        this.sounds = sounds;
    }

    public EffectComposite get(String key) {
        final MessageComposite messageComposite = this.messages.get(key);
        final ParticleEffect particleEffect = this.particles.get(key);
        final PlayableSound playableSound = this.sounds.get(key);
        return new EffectComposite(messageComposite, particleEffect, playableSound);
    }

}