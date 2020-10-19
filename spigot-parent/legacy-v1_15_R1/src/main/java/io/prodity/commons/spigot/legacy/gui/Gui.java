package io.prodity.commons.spigot.legacy.gui;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.prodity.commons.spigot.legacy.gui.close.GuiCloseHandler;
import io.prodity.commons.spigot.legacy.gui.close.GuiCloseReason;
import io.prodity.commons.spigot.legacy.gui.slot.SimpleSlot;
import io.prodity.commons.spigot.legacy.gui.slot.Slot;
import io.prodity.commons.spigot.legacy.plugin.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("Duplicates")
public abstract class Gui<SELF extends Gui<SELF>> {

    public static final String METADATA_KEY = "active-gui";

    public static Object getPlayerMetadataValue(Player player) {
        Preconditions.checkNotNull(player, "player");
        final JavaPlugin providingPlugin = PluginUtil.getProvidingPlugin();
        final List<MetadataValue> metadata = player.getMetadata(Gui.METADATA_KEY);
        for (MetadataValue value : metadata) {
            if (Objects.equals(providingPlugin, value.getOwningPlugin())) {
                return value.value();
            }
        }
        return null;
    }

    private final UUID uniqueId;

    private final Plugin plugin;
    private final Player player;
    private final Map<Integer, Slot> slots;
    private final List<GuiCloseHandler> closeHandlers;
    private final List<Listener> listeners;
    private final Listener listener;
    private Inventory inventory;

    private String title;

    private boolean firstDraw;
    private Function<Player, Gui<?>> fallbackGui;
    private boolean valid;
    private GuiCloseReason closeReason;

    protected Gui(UUID uniqueId, Plugin plugin, Player player, int lines, @Nullable String initialTitle) {
        this(uniqueId, plugin, player, Bukkit.createInventory(player, lines * 9, initialTitle == null ? "" : initialTitle));
    }

    protected Gui(UUID uniqueId, Plugin plugin, Player player, Inventory inventory) {
        Preconditions.checkNotNull(uniqueId, "uniqueId");
        Preconditions.checkNotNull(plugin, "plugin");
        Preconditions.checkNotNull(player, "player");
        Preconditions.checkNotNull(inventory, "inventory");
        this.uniqueId = uniqueId;
        this.plugin = plugin;
        this.player = player;
        this.slots = Maps.newHashMap();
        this.closeHandlers = Lists.newLinkedList();
        this.listeners = Lists.newArrayList();
        this.listener = new GuiListener();
        this.firstDraw = true;
        this.valid = false;
        this.inventory = inventory;
        this.title = inventory.getType().getDefaultTitle();
    }

    public void redraw() {
        if (this.isFirstDraw()) {
            this.listeners.forEach((listener) -> Bukkit.getPluginManager().registerEvents(listener, this.plugin));
            this.bind(() -> this.listeners.forEach((listener) -> HandlerList.unregisterAll(listener)));
        }
    }

    protected SELF getSelf() {
        return (SELF) this;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Inventory getHandle() {
        return this.inventory;
    }

    public String getTitle() {
        return this.title;
    }

    public SELF setTitle(String title) {
        this.title = title;
        if (this.isValid()) {
            GuiProvider.getInstance().updateInventoryTitle(this.player, this.inventory, this.title == null ? "" : this.title);
        }
        return this.getSelf();
    }

    public Optional<GuiCloseReason> getCloseReason() {
        return Optional.ofNullable(this.closeReason);
    }

    private void setCloseReason(GuiCloseReason closeReason) {
        if (this.closeReason == null) {
            this.closeReason = closeReason;
        }
    }

    public boolean isFirstDraw() {
        return this.firstDraw;
    }

    public boolean isValid() {
        return this.valid;
    }

    @Nullable
    public Function<Player, Gui<?>> getFallbackGui() {
        return this.fallbackGui;
    }

    public void setFallbackGui(@Nullable Function<Player, Gui<?>> fallbackGui) {
        this.fallbackGui = fallbackGui;
    }

    protected Slot createSlot(int slot) {
        return new SimpleSlot(this, slot);
    }

    public Slot getSlot(int slot) {
        if (slot < 0 || slot >= this.inventory.getSize()) {
            throw new IllegalArgumentException("Invalid slot id: " + slot);
        }

        return this.slots.computeIfAbsent(slot, i -> this.createSlot(i));
    }

    public void setItem(GuiItem item, int slot) {
        this.getSlot(slot).applyFromItem(item);
    }

    public void setItem(GuiItem item, int... slots) {
        Preconditions.checkNotNull(item, "item");
        for (int slot : slots) {
            this.setItem(item, slot);
        }
    }

    public void setItem(GuiItem item, Iterable<Integer> slots) {
        Preconditions.checkNotNull(item, "item");
        Preconditions.checkNotNull(slots, "slots");
        for (int slot : slots) {
            this.setItem(item, slot);
        }
    }

    public int getFirstEmpty() throws IndexOutOfBoundsException {
        final int slot = this.inventory.firstEmpty();
        if (slot < 0) {
            throw new IndexOutOfBoundsException("no empty slots");
        }
        return slot;
    }

    public Optional<Slot> getFirstEmptySlot() {
        final int slot = this.inventory.firstEmpty();
        if (slot < 0) {
            return Optional.empty();
        }
        return Optional.of(this.getSlot(slot));
    }

    public void addItem(GuiItem item) {
        Preconditions.checkNotNull(item, "item");
        this.getFirstEmptySlot().ifPresent(slot -> slot.applyFromItem(item));
    }

    public void addItem(Iterable<GuiItem> items) {
        Preconditions.checkNotNull(items, "items");
        items.forEach(this::addItem);
    }

    public void fillWith(GuiItem item) {
        Preconditions.checkNotNull(item, "item");
        for (int i = 0; i < this.inventory.getSize(); ++i) {
            this.setItem(item, i);
        }
    }

    public void removeItem(int slot) {
        this.getSlot(slot).clear();
    }

    public void removeItem(int... slots) {
        for (int slot : slots) {
            this.removeItem(slot);
        }
    }

    public void removeItem(Iterable<Integer> slots) {
        Preconditions.checkNotNull(slots, "slots");
        slots.forEach(this::removeItem);
    }

    public SELF bindListener(Listener listener) {
        if (this.isValid()) {
            Bukkit.getPluginManager().registerEvents(listener, this.plugin);
        }
        this.listeners.add(listener);
        return this.getSelf();
    }

    public SELF bindListenerIfFirstDraw(Listener listener) {
        return this.bindListenerIfFirstDraw(() -> listener);
    }

    public SELF bindListenerIfFirstDraw(Supplier<Listener> listener) {
        if (this.isFirstDraw()) {
            this.bindListener(listener.get());
        }
        return this.getSelf();
    }

    public SELF bindIfFirstDraw(Runnable runnable) {
        return this.bindIfFirstDraw(GuiCloseHandler.of(runnable));
    }

    public SELF bindIfFirstDraw(Runnable runnable, GuiCloseReason... reasons) {
        return this.bindIfFirstDraw(GuiCloseHandler.of(runnable, reasons));
    }

    public SELF bindIfFirstDraw(Runnable runnable, Iterable<GuiCloseReason> reasons) {
        return this.bindIfFirstDraw(GuiCloseHandler.of(runnable, reasons));
    }

    public SELF bindIfFirstDraw(GuiCloseHandler closeHandler) {
        if (this.isFirstDraw()) {
            this.bind(closeHandler);
        }
        return this.getSelf();
    }

    public SELF bind(Runnable runnable) {
        return this.bind(GuiCloseHandler.of(runnable));
    }

    public SELF bind(Runnable runnable, GuiCloseReason... reasons) {
        return this.bind(GuiCloseHandler.of(runnable, reasons));
    }

    public SELF bind(Runnable runnable, Iterable<GuiCloseReason> reasons) {
        return this.bind(GuiCloseHandler.of(runnable, reasons));
    }

    public SELF bind(GuiCloseHandler closeHandler) {
        this.closeHandlers.add(closeHandler);
        return this.getSelf();
    }

    public void clearItems() {
        this.inventory.clear();
        this.slots.values().forEach(Slot::clearBindings);
    }

    private MetadataValue createMetadataValue() {
        final JavaPlugin providingPlugin = PluginUtil.getProvidingPlugin();
        return new FixedMetadataValue(providingPlugin, this.uniqueId);
    }

    public void open() throws IllegalStateException {
        if (this.valid) {
            throw new IllegalStateException("gui is already open");
        }

        try {
            this.firstDraw = true;
            this.redraw();
            this.firstDraw = false;
        } catch (Exception exception) {
            exception.printStackTrace();
            this.closeReason = GuiCloseReason.FORCEFUL;
            this.invalidate();
            return;
        }

        Bukkit.getPluginManager().registerEvents(this.listener, this.plugin);

        final MetadataValue metadataValue = this.createMetadataValue();
        this.player.setMetadata(Gui.METADATA_KEY, metadataValue);

        this.player.openInventory(this.inventory);

        this.valid = true;
    }

    public void close(boolean useFallbackGui) {
        if (!useFallbackGui) {
            this.setFallbackGui(null);
        }
        this.close();
    }

    public void close() {
        this.setCloseReason(GuiCloseReason.FORCEFUL);
        this.player.closeInventory();
    }

    private void invalidate() {
        this.valid = false;
        this.clearItems();
        HandlerList.unregisterAll(this.listener);

        this.closeHandlers.stream()
            .filter((closeHandler) -> closeHandler.shouldHandle(this.closeReason))
            .forEach(GuiCloseHandler::run);
        this.closeHandlers.clear();
    }

    private class GuiListener implements Listener {

        private final long cooldownMillis = 200L;
        private Instant lastClick;

        public boolean testCooldown() {
            if (this.lastClick == null || Duration.between(this.lastClick, Instant.now()).toMillis() > this.cooldownMillis) {
                this.lastClick = Instant.now();
                return true;
            }

            return false;
        }

        @EventHandler
        public void on(InventoryClickEvent event) {
            final Inventory inventory = event.getInventory();
            final InventoryHolder inventoryHolder = inventory.getHolder();
            if (!(Objects.equals(inventoryHolder, Gui.this.getPlayer()))) {
                return;
            }

            event.setCancelled(true);
            if (!Gui.this.isValid()) {
                Gui.this.close();
            }

            final int slotId = event.getRawSlot();
            if (slotId != event.getSlot()) {
                return;
            }

            if (!this.testCooldown()) {
                return;
            }
            this.lastClick = Instant.now();

            final Slot slot = Gui.this.slots.get(slotId);
            if (slot != null) {
                slot.handle(event);
            }
        }

        @EventHandler
        public void on(PlayerQuitEvent event) {
            final Player player = event.getPlayer();
            if (!Gui.this.isValid() || !Objects.equals(player, Gui.this.getPlayer())) {
                return;
            }

            Gui.this.closeReason = GuiCloseReason.PLAYER_QUIT;
            Gui.this.invalidate();
        }

        @EventHandler
        public void on(InventoryCloseEvent event) {
            final Player player = (Player) event.getPlayer();
            if (!Gui.this.isValid() || !Objects.equals(player, Gui.this.getPlayer())) {
                return;
            }
            final Inventory inventory = event.getInventory();
            if (!Objects.equals(inventory, Gui.this.inventory)) {
                return;
            }

            if (Gui.this.closeReason == null) {
                final Object metadataValue = Gui.getPlayerMetadataValue(player);
                if (!Objects.equals(Gui.this.uniqueId, metadataValue)) {
                    Gui.this.closeReason = GuiCloseReason.INVENTORY_OVERRIDE;
                } else {
                    Gui.this.closeReason = GuiCloseReason.PLAYER_CLOSED;
                }
            }

            Gui.this.invalidate();

            Bukkit.getScheduler().runTaskLater(Gui.this.getPlugin(), () -> {
                final Function<Player, Gui<?>> fallbackFunction = Gui.this.getFallbackGui();
                final Gui<?> fallback = fallbackFunction == null ? null : fallbackFunction.apply(player);
                if (fallback != null && player.isOnline() && Gui.this.closeReason != GuiCloseReason.INVENTORY_OVERRIDE) {
                    fallback.open();
                }
            }, 1L);
        }
    }

}