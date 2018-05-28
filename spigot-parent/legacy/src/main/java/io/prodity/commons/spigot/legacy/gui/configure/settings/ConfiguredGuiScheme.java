package io.prodity.commons.spigot.legacy.gui.configure.settings;

import io.prodity.commons.spigot.legacy.gui.configure.ConfiguredGui;
import io.prodity.commons.spigot.legacy.gui.scheme.MenuScheme;
import io.prodity.commons.spigot.legacy.sound.playable.impl.ImmutablePlayableSound;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "Builder")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfiguredGuiScheme<T extends ConfiguredGui<T>> {

    protected final ImmutableMap<String, ConfiguredGuiItem> items;
    protected final ImmutablePlayableSound clickSound;
    @Getter
    private final String key;
    @Getter
    private final MenuScheme scheme;
    @Getter
    private final boolean redrawAlways;
    @Getter
    private final boolean cycleItems;

    protected ConfiguredGuiScheme(ConfiguredGuiScheme<T> scheme) {
        this.key = scheme.key;
        this.scheme = scheme.scheme;
        this.items = ImmutableMap.copyOf(scheme.items);
        this.redrawAlways = scheme.redrawAlways;
        this.cycleItems = scheme.cycleItems;
        this.clickSound = scheme.clickSound;
    }

    public boolean isKey(String key) {
        return Objects.equals(this.key, key);
    }

    public Optional<ImmutablePlayableSound> getClickSound() {
        return Optional.ofNullable(this.clickSound);
    }

    public ImmutableSet<String> getItemKeys() {
        return this.items.keySet();
    }

    public Iterator<String> getItemKeysToApply() {
        if (this.cycleItems) {
            return Iterators.cycle(this.items.keySet());
        }
        return this.items.keySet().iterator();
    }

    public ConfiguredGuiItem getItem(String key) {
        return this.items.get(key);
    }

    public ImmutableCollection<ConfiguredGuiItem> getItems() {
        return this.items.values();
    }

    public void verifyHasItem(String key) throws IllegalStateException {
        if (!this.hasItem(key)) {
            throw new IllegalStateException("gui scheme '" + this.key + "' requires item '" + key + "' but it is not present");
        }
    }

    public boolean hasItem(String key) {
        return this.items.containsKey(key);
    }

    public Optional<ImmutablePlayableSound> getClickSound(ConfiguredGuiItem item) {
        if (item.getClickSound().isPresent()) {
            return item.getClickSound();
        }
        return item.isUseSchemeClickSound() ? this.getClickSound() : Optional.empty();
    }

}