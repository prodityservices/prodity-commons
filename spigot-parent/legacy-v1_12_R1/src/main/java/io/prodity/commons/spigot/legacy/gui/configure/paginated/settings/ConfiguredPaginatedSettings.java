package io.prodity.commons.spigot.legacy.gui.configure.paginated.settings;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.prodity.commons.spigot.legacy.gui.configure.ConfiguredGui;
import io.prodity.commons.spigot.legacy.gui.configure.paginated.settings.button.PaginatedButtonType;
import io.prodity.commons.spigot.legacy.gui.configure.paginated.settings.button.PaginatedControlButton;
import io.prodity.commons.spigot.legacy.gui.configure.settings.ConfiguredGuiSettings;
import io.prodity.commons.spigot.legacy.item.builder.ImmutableItemBuilder;
import io.prodity.commons.spigot.legacy.sound.playable.impl.ImmutablePlayableSound;
import java.util.Optional;
import lombok.Getter;

public class ConfiguredPaginatedSettings<T extends ConfiguredGui<T>> extends ConfiguredGuiSettings<T> {

    @Getter
    private final ImmutableMap<PaginatedButtonType, PaginatedControlButton> buttons;

    @Getter
    private final ImmutableList<Integer> elementSlots;

    @Getter
    private final ImmutableItemBuilder elementItemBuilder;

    @Getter
    private final Optional<ImmutablePlayableSound> elementClickSound;

    @lombok.Builder(builderClassName = "PaginatedBuilder", builderMethodName = "paginatedBuilder")
    protected ConfiguredPaginatedSettings(ConfiguredGuiSettings<T> settings,
        ImmutableMap<PaginatedButtonType, PaginatedControlButton> buttons,
        ImmutableList<Integer> elementSlots, ImmutableItemBuilder elementItemBuilder, ImmutablePlayableSound elementClickSound) {
        super(settings);
        this.buttons = buttons;
        this.elementSlots = elementSlots;
        this.elementItemBuilder = elementItemBuilder;
        this.elementClickSound = Optional.ofNullable(elementClickSound);
    }

}
