package io.prodity.commons.spigot.legacy.effect;

import com.google.common.collect.Lists;
import io.prodity.commons.spigot.legacy.message.composite.MessageComposite;
import io.prodity.commons.spigot.legacy.message.replace.Replacer;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffect;
import io.prodity.commons.spigot.legacy.sound.playable.PlayableSound;
import java.util.Optional;
import java.util.function.Consumer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EffectComposite {

    private final MessageComposite message;
    private final ParticleEffect particle;
    private final PlayableSound sound;

    public EffectComposite(MessageComposite message, ParticleEffect particle, PlayableSound sound) {
        this.message = message;
        this.particle = particle;
        this.sound = sound;
    }

    public EffectComposite(MessageComposite message, ParticleEffect particle) {
        this(message, particle, null);
    }

    public EffectComposite(MessageComposite message, PlayableSound sound) {
        this(message, null, sound);
    }

    public EffectComposite(ParticleEffect particle, PlayableSound sound) {
        this(null, particle, sound);
    }

    public EffectComposite(MessageComposite message) {
        this(message, null, null);
    }

    public EffectComposite(PlayableSound sound) {
        this(null, null, sound);
    }

    public EffectComposite(ParticleEffect particle) {
        this(null, particle, null);
    }

    public MessageComposite getMessage() {
        return this.message;
    }

    public ParticleEffect getParticle() {
        return this.particle;
    }

    public PlayableSound getSound() {
        return this.sound;
    }

    public void playPrivately(CommandSender... commandSenders) {
        this.playPrivately(null, commandSenders);
    }

    public void playPrivately(Replacer replacer, CommandSender... commandSenders) {
        this.playPrivately(replacer, Lists.newArrayList(commandSenders));
    }

    public void playPrivately(Iterable<? extends CommandSender> commandSenders) {
        this.playPrivately(null, commandSenders);
    }

    public void playPrivately(Replacer replacer, Iterable<? extends CommandSender> commandSenders) {
        if (commandSenders == null) {
            return;
        }
        this.ifPresent(this.message, (message) -> message.send(replacer, commandSenders));
        for (CommandSender commandSender : commandSenders) {
            if (commandSender instanceof Player) {
                final Player player = (Player) commandSender;
                this.ifPresent(this.particle, (particle) -> particle.playFor(player.getLocation(), player));
                this.ifPresent(this.sound, (sound) -> sound.play(player.getLocation(), player));
            }
        }
    }

    public void playGlobally(CommandSender... commandSenders) {
        this.playGlobally(null, commandSenders);
    }

    public void playGlobally(Replacer replacer, CommandSender... commandSenders) {
        this.playGlobally(replacer, Lists.newArrayList(commandSenders));
    }

    public void playGlobally(Iterable<? extends CommandSender> commandSenders) {
        this.playGlobally(null, commandSenders);
    }

    public void playGlobally(Replacer replacer, Iterable<? extends CommandSender> commandSenders) {
        if (commandSenders == null) {
            return;
        }
        this.ifPresent(this.message, (message) -> message.send(replacer, commandSenders));
        for (CommandSender commandSender : commandSenders) {
            if (commandSender instanceof Player) {
                final Player player = (Player) commandSender;
                this.ifPresent(this.particle, (particle) -> particle.play(player.getLocation()));
                this.ifPresent(this.sound, (sound) -> sound.play(player.getLocation()));
            }
        }
    }

    private <T> void ifPresent(T object, Consumer<T> action) {
        Optional.ofNullable(object).ifPresent(action);
    }

}