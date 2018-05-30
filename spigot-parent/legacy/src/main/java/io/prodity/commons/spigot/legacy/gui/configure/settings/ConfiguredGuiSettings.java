package io.prodity.commons.spigot.legacy.gui.configure.settings;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.prodity.commons.spigot.legacy.gui.configure.ConfiguredGui;
import io.prodity.commons.spigot.legacy.item.repo.ItemRepository;
import io.prodity.commons.spigot.legacy.message.replace.Replacer;
import io.prodity.commons.spigot.legacy.placeholder.PlaceholderHelper;
import io.prodity.commons.spigot.legacy.sound.playable.impl.ImmutablePlayableSound;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.entity.Player;

@Builder(builderClassName = "Builder")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfiguredGuiSettings<T extends ConfiguredGui<T>> {

    @Getter
    private final ItemRepository itemRepository;

    @Getter
    private final String key;

    @Getter
    private final String title;

    @Getter
    private final int lines;

    @Getter
    private final ImmutableMap<String, ConfiguredGuiScheme<T>> schemes;

    private final ImmutablePlayableSound openSound;

    private final ImmutablePlayableSound closeSound;

    protected ConfiguredGuiSettings(ConfiguredGuiSettings settings) {
        this.itemRepository = settings.itemRepository;
        this.key = settings.key;
        this.title = settings.title;
        this.lines = settings.lines;
        this.schemes = settings.schemes;
        this.openSound = settings.openSound;
        this.closeSound = settings.closeSound;
    }

    public String getTitle(Replacer replacer) {
        return replacer.replace(this.title);
    }

    public String getTitle(Player player) {
        return PlaceholderHelper.setPlaceholders(player, this.title);
    }

    public String getTitle(Player player, Replacer replacer) {
        return replacer.replace(player, this.title);
    }

    public void verifyHasScheme(String key) throws IllegalStateException {
        if (!this.hasScheme(key)) {
            throw new IllegalStateException("gui settings '" + this.key + "' requires scheme '" + key + "' but it is not present");
        }
    }

    public boolean hasScheme(String key) {
        return this.schemes.containsKey(key);
    }

    public ConfiguredGuiScheme<T> getScheme(String key) {
        return this.schemes.get(key);
    }

    public ImmutableCollection<ConfiguredGuiScheme<T>> getSchemesAsCollection() {
        return this.schemes.values();
    }

    public ImmutableSet<String> getSchemeKeys() {
        return this.schemes.keySet();
    }

    public Optional<ImmutablePlayableSound> getOpenSound() {
        return Optional.ofNullable(this.openSound);
    }

    public Optional<ImmutablePlayableSound> getCloseSound() {
        return Optional.ofNullable(this.closeSound);
    }

}