package io.prodity.commons.spigot.legacy.sound.playable;

import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.repo.YamlRepository;
import io.prodity.commons.spigot.legacy.sound.playable.collect.list.PlayableSoundArrayList;
import io.prodity.commons.spigot.legacy.sound.playable.collect.list.PlayableSoundList;
import io.prodity.commons.spigot.legacy.sound.playable.impl.ImmutablePlayableSound;
import io.prodity.commons.spigot.legacy.sound.playable.yaml.YamlImmutablePlayableSound;
import java.io.File;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class PlayableSoundRepository extends YamlRepository<ImmutablePlayableSound> {

    public PlayableSoundRepository(File file) {
        super(file);
    }

    public PlayableSoundRepository(Plugin plugin, String filePath) {
        super(plugin, filePath);
    }

    @Override
    protected boolean shouldLoad(ConfigurationSection section, String path) {
        return section.isConfigurationSection(path) && section.getConfigurationSection(path).contains("sound");
    }

    @Override
    protected ImmutablePlayableSound load(ConfigurationSection section, String path) throws YamlException {
        return YamlImmutablePlayableSound.get().load(section, path);
    }

    @Override
    public PlayableSoundList<ImmutablePlayableSound> getList(String... keys) {
        return PlayableSoundArrayList.copyOf(super.getList(keys));
    }

    @Override
    public PlayableSoundList<ImmutablePlayableSound> getList(Iterable<String> keys) {
        return PlayableSoundArrayList.copyOf(super.getList(keys));
    }

}