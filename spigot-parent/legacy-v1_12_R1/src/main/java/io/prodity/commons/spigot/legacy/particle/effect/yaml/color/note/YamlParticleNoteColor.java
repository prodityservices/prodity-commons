package io.prodity.commons.spigot.legacy.particle.effect.yaml.color.note;

import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlInt;
import io.prodity.commons.spigot.legacy.particle.effect.color.ParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.factory.ParticleFactory;
import org.bukkit.configuration.ConfigurationSection;

public abstract class YamlParticleNoteColor<T extends ParticleColor> extends AbstractYamlType<T> {

    protected YamlParticleNoteColor(Class<T> clazz) {
        super(clazz, false);
    }

    public int loadNote(ConfigurationSection section, String path) throws YamlException {
        final int note = YamlInt.get().load(section, path);
        if (note < 0 || note > 24) {
            throw new YamlException(section, path, "number must fit in range 0-24 (inclusive)");
        }
        return note;
    }

    @Override
    protected T loadInternally(ConfigurationSection section, String path) throws YamlException {
        final int note = this.loadNote(section, path);
        return this.getFactory().noteColor(note);
    }

    protected abstract ParticleFactory<?, ?, ?, ? extends T> getFactory();

}