package io.prodity.commons.spigot.legacy.particle.effect;

import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.repo.YamlRepository;
import io.prodity.commons.spigot.legacy.config.yaml.types.bukkit.YamlSection;
import io.prodity.commons.spigot.legacy.particle.effect.collect.list.ParticleEffectArrayList;
import io.prodity.commons.spigot.legacy.particle.effect.collect.list.ParticleEffectList;
import io.prodity.commons.spigot.legacy.particle.effect.offset.ImmutableOffsetParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.YamlImmutableParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.offset.YamlImmutableOffsetParticleEffect;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class ParticleRepository extends YamlRepository<ImmutableParticleEffect> {

    public ParticleRepository(Plugin plugin, String fileName) {
        super(plugin, fileName);
    }

    public ParticleRepository(File file) {
        super(file);
    }

    @Override
    protected boolean shouldLoad(ConfigurationSection section, String path) {
        return section.isConfigurationSection(path) && section.getConfigurationSection(path).contains("particle");
    }

    @Override
    protected ImmutableParticleEffect load(ConfigurationSection section, String path) throws YamlException {
        final ConfigurationSection effectSection = YamlSection.get().load(section, path);
        if (effectSection.contains("location-offset")) {
            return YamlImmutableOffsetParticleEffect.get().load(section, path);
        }
        return YamlImmutableParticleEffect.get().load(section, path);
    }

    public ImmutableOffsetParticleEffect getAsOffset(String key) {
        final ImmutableParticleEffect effect = this.get(key);
        if (!(effect instanceof ImmutableOffsetParticleEffect)) {
            throw new IllegalArgumentException("particle effect of key '" + key + "' is not offset");
        }
        return (ImmutableOffsetParticleEffect) effect;
    }

    @Override
    public ParticleEffectList<ImmutableParticleEffect> getList(String... keys) {
        return ParticleEffectArrayList.copyOf(super.getList(keys));
    }

    @Override
    public ParticleEffectList<ImmutableParticleEffect> getList(Iterable<String> keys) {
        return ParticleEffectArrayList.copyOf(super.getList(keys));
    }

}