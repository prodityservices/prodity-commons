package io.prodity.commons.spigot.legacy.gui;

import io.prodity.commons.spigot.legacy.lazy.LazyValue;
import io.prodity.commons.spigot.legacy.lazy.SimpleLazyValue;
import io.prodity.commons.spigot.legacy.version.Version;
import io.prodity.commons.spigot.legacy.version.VersionProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class GuiProvider implements VersionProvider {

    private static final LazyValue<GuiProvider> LAZY_INSTANCE = new SimpleLazyValue<>(
        () -> Version.getUtilitiesProvider(GuiProvider.class, "gui.GuiProvider"));

    public static GuiProvider getInstance() {
        return GuiProvider.LAZY_INSTANCE.get();
    }

    public abstract void updateInventoryTitle(Player player, Inventory inventory, String newTitle);

}