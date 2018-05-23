package io.prodity.commons.spigot.gui;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.Consumer;

public class GuiItem {

    public static final class Builder {

        private final ItemStack itemStack;
        private final Map<ClickType, Consumer<InventoryClickEvent>> handlers;

        private Builder(ItemStack itemStack) {
            this.itemStack = Preconditions.checkNotNull(itemStack, "itemStack");
            this.handlers = Maps.newHashMap();
        }

        public Builder bind(ClickType type, Consumer<InventoryClickEvent> handler) {
            Preconditions.checkNotNull(type, "type");
            if (handler != null) {
                this.handlers.put(type, handler);
            } else {
                this.handlers.remove(type);
            }
            return this;
        }

        public Builder bind(ClickType type, Runnable handler) {
            Preconditions.checkNotNull(type, "type");
            if (handler != null) {
                this.handlers.put(type, (event) -> handler.run());
            } else {
                this.handlers.remove(type);
            }
            return this;
        }

        public Builder bind(Consumer<InventoryClickEvent> handler, ClickType... types) {
            for (ClickType type : types) {
                this.bind(type, handler);
            }
            return this;
        }

        public Builder bind(Runnable handler, ClickType... types) {
            for (ClickType type : types) {
                this.bind(type, handler);
            }
            return this;
        }

        public <T extends Runnable> Builder bindAllRunnables(Iterable<Map.Entry<ClickType, T>> handlers) {
            Preconditions.checkNotNull(handlers, "handlers");
            for (Map.Entry<ClickType, T> handler : handlers) {
                this.bind(handler.getKey(), handler.getValue());
            }
            return this;
        }

        public <T extends Consumer<InventoryClickEvent>> Builder bindAllConsumers(Iterable<Map.Entry<ClickType, T>> handlers) {
            Preconditions.checkNotNull(handlers, "handlers");
            for (Map.Entry<ClickType, T> handler : handlers) {
                this.bind(handler.getKey(), handler.getValue());
            }
            return this;
        }

        public GuiItem build() {
            return new GuiItem(this.handlers, this.itemStack);
        }

    }

    public static GuiItem.Builder builder(ItemStack itemStack) {
        return new Builder(itemStack);
    }

    private final Map<ClickType, Consumer<InventoryClickEvent>> handlers;
    private final ItemStack itemStack;

    public GuiItem(Map<ClickType, Consumer<InventoryClickEvent>> handlers, ItemStack itemStack) {
        this.handlers = ImmutableMap.copyOf(Preconditions.checkNotNull(handlers, "handlers"));
        this.itemStack = Preconditions.checkNotNull(itemStack, "itemStack");
    }

    public Map<ClickType, Consumer<InventoryClickEvent>> getHandlers() {
        return this.handlers;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

}