package io.prodity.commons.spigot.legacy.gui.configure.settings;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.repo.YamlRepository;
import io.prodity.commons.spigot.legacy.config.yaml.types.bukkit.YamlSection;
import io.prodity.commons.spigot.legacy.config.yaml.types.collection.YamlImmutableList;
import io.prodity.commons.spigot.legacy.config.yaml.types.collection.YamlList;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlBoolean;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlColorizedString;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlInt;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlString;
import io.prodity.commons.spigot.legacy.gui.configure.ConfiguredGui;
import io.prodity.commons.spigot.legacy.gui.configure.paginated.settings.ConfiguredPaginatedSettings;
import io.prodity.commons.spigot.legacy.gui.configure.paginated.settings.button.PaginatedButtonType;
import io.prodity.commons.spigot.legacy.gui.configure.paginated.settings.button.PaginatedButtonValidState;
import io.prodity.commons.spigot.legacy.gui.configure.paginated.settings.button.PaginatedControlButton;
import io.prodity.commons.spigot.legacy.gui.configure.paginated.settings.button.PaginatedControlButton.ButtonData;
import io.prodity.commons.spigot.legacy.gui.scheme.MenuScheme;
import io.prodity.commons.spigot.legacy.item.builder.ImmutableItemBuilder;
import io.prodity.commons.spigot.legacy.item.repo.ItemRepository;
import io.prodity.commons.spigot.legacy.sound.playable.PlayableSoundRepository;
import io.prodity.commons.spigot.legacy.sound.playable.impl.ImmutablePlayableSound;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfiguredGuiSettingsRepository extends YamlRepository<ConfiguredGuiSettings<?>> {

    @Getter
    private final ItemRepository itemRepository;

    @Getter
    private final PlayableSoundRepository soundRepository;

    public ConfiguredGuiSettingsRepository(ItemRepository itemRepository, PlayableSoundRepository soundRepository, JavaPlugin plugin,
        String fileName) {
        super(plugin, fileName);
        this.itemRepository = itemRepository;
        this.soundRepository = soundRepository;
    }

    public ConfiguredGuiSettingsRepository(ItemRepository itemRepository, PlayableSoundRepository soundRepository, File file) {
        super(file);
        this.itemRepository = itemRepository;
        this.soundRepository = soundRepository;
    }

    @Override
    protected boolean shouldLoad(ConfigurationSection section, String path) {
        return section.isConfigurationSection(path) && section.getConfigurationSection(path).isString("title");
    }

    @Override
    protected ConfiguredGuiSettings load(ConfigurationSection section, String path) throws YamlException {
        final ConfigurationSection settingsSection = YamlSection.get().load(section, path);

        if (settingsSection.contains("paginated")) {
            return this.loadPaginatedSettings(settingsSection);
        } else {
            return this.loadSettings(settingsSection);
        }
    }

    private ConfiguredGuiSettings loadSettings(ConfigurationSection section) throws YamlException {
        final ConfiguredGuiSettings.Builder builder = ConfiguredGuiSettings.builder();
        builder.itemRepository(this.itemRepository);
        builder.key(section.getName());

        final String title = YamlColorizedString.get().load(section, "title");
        builder.title(title);

        final int lines = YamlInt.get().load(section, "lines");
        builder.lines(lines);

        final ImmutableMap.Builder<String, ConfiguredGuiScheme> schemes = ImmutableMap.builder();
        if (section.contains("schemes")) {
            final ConfigurationSection schemesSection = YamlSection.get().load(section, "schemes");

            for (String schemeKey : schemesSection.getKeys(false)) {
                final ConfigurationSection schemeSection = YamlSection.get().load(schemesSection, schemeKey);
                final ConfiguredGuiScheme scheme = this.loadScheme(schemeSection);
                schemes.put(schemeKey, scheme);
            }
        }
        builder.schemes(schemes.build());

        this.loadSoundIfPresent(section, "open-sound", builder::openSound);
        this.loadSoundIfPresent(section, "close-sound", builder::closeSound);

        return builder.build();
    }

    private ConfiguredPaginatedSettings loadPaginatedSettings(ConfigurationSection section) throws YamlException {
        final ConfiguredPaginatedSettings.PaginatedBuilder builder = ConfiguredPaginatedSettings.paginatedBuilder();

        final ConfiguredGuiSettings parentSettings = this.loadSettings(section);
        builder.settings(parentSettings);

        final ConfigurationSection paginatedSection = YamlSection.get().load(section, "paginated");

        final ImmutableList<Integer> elementSlots = YamlImmutableList.get().load(paginatedSection, "slots");
        builder.elementSlots(elementSlots);

        final ImmutableItemBuilder itemBuilder = this.loadItem(paginatedSection, "item", () -> ImmutableItemBuilder.of(Material.AIR));
        builder.elementItemBuilder(itemBuilder);

        final ImmutableMap.Builder<PaginatedButtonType, PaginatedControlButton> buttons = ImmutableMap.builder();

        final PaginatedControlButton nextPageButton = this.loadButton(PaginatedButtonType.NEXT_PAGE, paginatedSection, "next-page");
        buttons.put(PaginatedButtonType.NEXT_PAGE, nextPageButton);

        final PaginatedControlButton previousPageButton = this
            .loadButton(PaginatedButtonType.PREVIOUS_PAGE, paginatedSection, "previous-page");
        buttons.put(PaginatedButtonType.PREVIOUS_PAGE, previousPageButton);

        builder.buttons(buttons.build());

        final ImmutablePlayableSound elementClickSound = this.loadSoundIfPresent(paginatedSection, "click-sound");
        builder.elementClickSound(elementClickSound);

        return builder.build();
    }

    private PaginatedControlButton loadButton(PaginatedButtonType type, ConfigurationSection section, String path) throws YamlException {
        final ConfigurationSection buttonSection = YamlSection.get().load(section, path);
        final int slot = YamlInt.get().load(buttonSection, "slot");

        final ButtonData validData = this.loadButtonData(PaginatedButtonValidState.VALID, buttonSection, "valid");
        final ButtonData invalidData = this.loadButtonData(PaginatedButtonValidState.INVALID, buttonSection, "invalid");

        return new PaginatedControlButton(type, slot, validData, invalidData);
    }

    private ButtonData loadButtonData(PaginatedButtonValidState state, ConfigurationSection section, String path) throws YamlException {
        final ConfigurationSection buttonSection = YamlSection.get().load(section, path);
        final ButtonData.Builder builder = ButtonData.builder();
        builder.state(state);

        this.loadSoundIfPresent(buttonSection, "click-sound", builder::clickSound);

        final ImmutableItemBuilder itemBuilder = this.loadItem(buttonSection, "item");
        builder.itemBuilder(itemBuilder);

        return builder.build();
    }

    private ConfiguredGuiScheme loadScheme(ConfigurationSection section) throws YamlException {
        final ConfiguredGuiScheme.Builder builder = ConfiguredGuiScheme.builder();
        builder.key(section.getName());

        final List<String> maskStrings = YamlList.get().load(section, "mask");
        final MenuScheme scheme = new MenuScheme();
        maskStrings.forEach(scheme::mask);
        builder.scheme(scheme);

        final List<Object> itemKeys = YamlList.get().load(section, "items");
        final ImmutableMap.Builder<String, ConfiguredGuiItem> items = ImmutableMap.builder();

        for (Object itemObject : itemKeys) {
            final ConfiguredGuiItem.Builder guiItemBuilder = ConfiguredGuiItem.builder();

            final String itemKey;
            if (itemObject instanceof String) {
                itemKey = itemObject.toString();
                guiItemBuilder.useSchemeClickSound(true);
                guiItemBuilder.clickSound(Optional.empty());
            } else if (itemObject instanceof Map) {
                final Map<String, Map<String, Object>> itemMap = (Map<String, Map<String, Object>>) itemObject;
                itemKey = Iterables.get(itemMap.keySet(), 0);

                final Map<String, Object> itemAttributes = itemMap.get(itemKey);

                guiItemBuilder.useSchemeClickSound((boolean) itemAttributes.getOrDefault("use-scheme-sound", true));

                final String clickSoundId = itemAttributes.getOrDefault("click-sound", "").toString();
                if (clickSoundId == null || clickSoundId.isEmpty()) {
                    guiItemBuilder.clickSound(Optional.empty());
                } else {
                    final ImmutablePlayableSound clickSound = this.soundRepository.get(clickSoundId);
                    guiItemBuilder.clickSound(Optional.ofNullable(clickSound));
                }

            } else {
                throw new YamlException(section, itemObject.toString(),
                    "unknown configuration type for item, class=" + itemObject.getClass().getName());
            }
            guiItemBuilder.key(itemKey);

            final Optional<ImmutableItemBuilder> itemBuilder = Optional.ofNullable(this.itemRepository.getOrDefault(itemKey, null));
            guiItemBuilder.itemBuilder(itemBuilder);

            items.put(itemKey, guiItemBuilder.build());
        }

        builder.items(items.build());

        this.loadSoundIfPresent(section, "click-sound", builder::clickSound);

        final boolean cycleItems = YamlBoolean.get().loadOrDefault(section, "cycle-items", true);
        builder.cycleItems(cycleItems);

        final boolean redrawAlways = YamlBoolean.get().loadOrDefault(section, "redraw-always", false);
        builder.redrawAlways(redrawAlways);

        return builder.build();
    }

    public <T extends ConfiguredGui<T>> ConfiguredGuiSettings<T> getForType(String key) {
        return (ConfiguredGuiSettings<T>) get(key);
    }

    public ConfiguredPaginatedSettings getAsPaginated(String key) throws IllegalArgumentException {
        final ConfiguredGuiSettings settings = this.get(key);
        if (!(settings instanceof ConfiguredPaginatedSettings)) {
            throw new IllegalArgumentException("ConfiguredGuiSettings of key '" + key + "' is not ConfiguredPaginatedSettings");
        }
        return (ConfiguredPaginatedSettings) settings;
    }

    private ImmutablePlayableSound loadSound(ConfigurationSection section, String path) throws YamlException {
        final String soundKey = YamlString.get().load(section, path);
        return this.soundRepository.get(soundKey);
    }

    private ImmutablePlayableSound loadSoundIfPresent(ConfigurationSection section, String path) throws YamlException {
        if (!section.contains(path)) {
            return null;
        }
        final String soundKey = YamlString.get().load(section, path);
        return this.soundRepository.get(soundKey);
    }

    private void loadSoundIfPresent(ConfigurationSection section, String path, Consumer<ImmutablePlayableSound> ifPresent)
        throws YamlException {
        if (!section.contains(path)) {
            return;
        }
        final ImmutablePlayableSound sound = this.loadSound(section, path);
        if (sound != null) {
            ifPresent.accept(sound);
        }
    }

    private ImmutableItemBuilder loadItem(ConfigurationSection section, String path) throws YamlException {
        return this.loadItem(section, path, null);
    }

    private ImmutableItemBuilder loadItem(ConfigurationSection section, String path, Supplier<ImmutableItemBuilder> defaultIfMissing)
        throws YamlException {
        final String itemKey = YamlString.get().loadOrDefault(section, path, null);
        if (itemKey == null) {
            if (defaultIfMissing != null) {
                return defaultIfMissing.get();
            }
            throw new YamlException(section, path, "no item key is specified");
        }
        final ImmutableItemBuilder itemBuilder = this.itemRepository.get(itemKey);
        if (itemBuilder == null) {
            if (defaultIfMissing != null) {
                return defaultIfMissing.get();
            }
            throw new YamlException(section, path, "no item exists for itemKey=" + itemKey);
        }
        return itemBuilder;
    }

}