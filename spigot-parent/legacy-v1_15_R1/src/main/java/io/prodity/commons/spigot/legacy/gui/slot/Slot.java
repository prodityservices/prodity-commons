package io.prodity.commons.spigot.legacy.gui.slot;

import io.prodity.commons.spigot.legacy.gui.Gui;
import io.prodity.commons.spigot.legacy.gui.GuiItem;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.Consumer;

public interface Slot {

    Gui<?> gui();

    int getId();

    Slot applyFromItem(GuiItem item);

    ItemStack getItem();

    Slot setItem(ItemStack item);

    boolean hasItem();

    Slot clear();

    Slot clearItem();

    Slot clearBindings();

    Slot clearBindings(ClickType type);

    Slot bind(Consumer<InventoryClickEvent> handler);

    Slot bind(Runnable handler);

    Slot bind(Consumer<InventoryClickEvent> handler, ClickType type);

    Slot bind(Runnable handler, ClickType type);

    Slot bind(Consumer<InventoryClickEvent> handler, ClickType... types);

    Slot bind(Runnable handler, ClickType... types);

    <T extends Runnable> Slot bindAllRunnables(Iterable<Map.Entry<ClickType, T>> handlers);

    <T extends Consumer<InventoryClickEvent>> Slot bindAllConsumers(Iterable<Map.Entry<ClickType, T>> handlers);

    void handle(InventoryClickEvent event);

}