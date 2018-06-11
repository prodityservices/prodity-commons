package io.prodity.commons.spigot.legacy.gui.slot;

import com.google.common.base.Preconditions;
import io.prodity.commons.spigot.legacy.gui.Gui;
import io.prodity.commons.spigot.legacy.gui.GuiItem;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;


public class SimpleSlot implements Slot {

    private final Gui<?> gui;
    private final int id;
    private final Map<ClickType, Set<Consumer<InventoryClickEvent>>> handlers;

    public SimpleSlot(Gui gui, int id) {
        this.gui = gui;
        this.id = id;
        this.handlers = Collections.synchronizedMap(new EnumMap<>(ClickType.class));
    }

    @Override
    public void handle(InventoryClickEvent event) {
        final Set<Consumer<InventoryClickEvent>> handlers = this.handlers.get(event.getClick());
        if (handlers == null) {
            return;
        }
        for (Consumer<InventoryClickEvent> handler : handlers) {
            try {
                handler.accept(event);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public Gui<?> gui() {
        return this.gui;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public Slot applyFromItem(GuiItem item) {
        Preconditions.checkNotNull(item, "item");
        this.setItem(item.getItemStack());
        this.clearBindings();
        this.bindAllConsumers(item.getHandlers().entrySet());
        return this;
    }

    @Override
    public ItemStack getItem() {
        return this.gui.getHandle().getItem(this.id);
    }

    @Override
    public boolean hasItem() {
        return this.getItem() != null;
    }

    @Override
    public Slot setItem(ItemStack item) {
        Preconditions.checkNotNull(item, "item");
        this.gui.getHandle().setItem(this.id, item);
        return this;
    }

    @Override
    public Slot clear() {
        this.clearItem();
        this.clearBindings();
        return this;
    }

    @Override
    public Slot clearItem() {
        this.gui.getHandle().clear(this.id);
        return this;
    }

    @Override
    public Slot clearBindings() {
        this.handlers.clear();
        return this;
    }

    @Override
    public Slot clearBindings(ClickType type) {
        this.handlers.remove(type);
        return this;
    }

    @Override
    public Slot bind(Consumer<InventoryClickEvent> handler) {
        return this.bind(handler, ClickType.values());
    }

    @Override
    public Slot bind(Runnable handler) {
        return this.bind(handler, ClickType.values());
    }

    @Override
    public Slot bind(Consumer<InventoryClickEvent> handler, ClickType type) {
        Preconditions.checkNotNull(type, "type");
        Preconditions.checkNotNull(handler, "handler");
        this.handlers.computeIfAbsent(type, t -> ConcurrentHashMap.newKeySet()).add(handler);
        return this;
    }

    @Override
    public Slot bind(Runnable handler, ClickType type) {
        Preconditions.checkNotNull(type, "type");
        Preconditions.checkNotNull(handler, "handler");
        this.handlers.computeIfAbsent(type, t -> ConcurrentHashMap.newKeySet()).add((event) -> handler.run());
        return this;
    }

    @Override
    public Slot bind(Consumer<InventoryClickEvent> handler, ClickType... types) {
        for (ClickType type : types) {
            this.bind(handler, type);
        }
        return this;
    }

    @Override
    public Slot bind(Runnable handler, ClickType... types) {
        for (ClickType type : types) {
            this.bind(handler, type);
        }
        return this;
    }

    @Override
    public <T extends Runnable> Slot bindAllRunnables(Iterable<Map.Entry<ClickType, T>> handlers) {
        Preconditions.checkNotNull(handlers, "handlers");
        for (Map.Entry<ClickType, T> handler : handlers) {
            this.bind(handler.getValue(), handler.getKey());
        }
        return this;
    }

    @Override
    public <T extends Consumer<InventoryClickEvent>> Slot bindAllConsumers(Iterable<Map.Entry<ClickType, T>> handlers) {
        Preconditions.checkNotNull(handlers, "handlers");
        for (Map.Entry<ClickType, T> handler : handlers) {
            this.bind(handler.getValue(), handler.getKey());
        }
        return this;
    }

}