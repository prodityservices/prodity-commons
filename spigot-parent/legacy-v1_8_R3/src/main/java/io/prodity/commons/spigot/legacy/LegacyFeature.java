package io.prodity.commons.spigot.legacy;

import io.prodity.commons.inject.Export;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.plugin.ProdityPlugin;
import io.prodity.commons.spigot.legacy.effect.EffectCompositeRepository;
import io.prodity.commons.spigot.legacy.gui.configure.settings.ConfiguredGuiSettingsRepository;
import io.prodity.commons.spigot.legacy.item.repo.ItemRepository;
import io.prodity.commons.spigot.legacy.message.composite.MessageCompositeRepository;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleRepository;
import io.prodity.commons.spigot.legacy.sound.playable.PlayableSoundRepository;
import io.prodity.commons.spigot.legacy.tryto.Try;
import org.bukkit.plugin.java.JavaPlugin;
import org.jvnet.hk2.annotations.Service;

@Export
@Service
public class LegacyFeature implements InjectionFeature {

    @Override
    public void preLoad(ProdityPlugin plugin) {
        if (!(plugin instanceof JavaPlugin) || !this.isLegacyPlugin(plugin)) {
            return;
        }

        final JavaPlugin javaPlugin = (JavaPlugin) plugin;

        Try.run(() -> {
            final MessageCompositeRepository messages = new MessageCompositeRepository(javaPlugin, "messages.yml");
            messages.load();

            final ParticleRepository particles = new ParticleRepository(javaPlugin, "particles.yml");
            particles.load();

            final PlayableSoundRepository sounds = new PlayableSoundRepository(javaPlugin, "sounds.yml");
            sounds.load();

            final EffectCompositeRepository effects = new EffectCompositeRepository(messages, particles, sounds);

            final ItemRepository items = new ItemRepository(javaPlugin, "items.yml");
            items.load();

            final ConfiguredGuiSettingsRepository guiSettings = new ConfiguredGuiSettingsRepository(items, sounds, javaPlugin, "guis.yml");
            guiSettings.load();

            this.bind(plugin, (binder) -> {
                binder.bind(messages).to(MessageCompositeRepository.class);
                binder.bind(particles).to(ParticleRepository.class);
                binder.bind(sounds).to(PlayableSoundRepository.class);
                binder.bind(effects).to(EffectCompositeRepository.class);
                binder.bind(items).to(ItemRepository.class);
                binder.bind(guiSettings).to(ConfiguredGuiSettingsRepository.class);
            });
        });
    }

    private boolean isLegacyPlugin(ProdityPlugin plugin) {
        return plugin.getClass().isAnnotationPresent(InjectLegacy.class);
    }

}