package io.prodity.commons.spigot.gui;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.prodity.commons.plugin.ProdityPlugin;
import io.prodity.commons.spigot.gui.close.GuiCloseHandler;
import io.prodity.commons.spigot.gui.close.GuiCloseReason;
import io.prodity.commons.spigot.gui.slot.SimpleSlot;
import io.prodity.commons.spigot.gui.slot.Slot;
import io.prodity.commons.spigot.plugin.ProditySpigotPlugin;
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

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Gui<SELF extends Gui<SELF>> {

    public static final String METADATA_KEY = "active-gui";

    private final UUID uniqueId;

    private final ProditySpigotPlugin plugin;
    private final Player player;
    private final Map<Integer, SimpleSlot> slots;
    private final List<GuiCloseHandler> closeHandlers;
    private final List<Listener> listeners;
    private final Listener listener;
    private final Inventory inventory;
    private final GuiProvider guiProvider;

    private String title;

    private boolean firstDraw;
    private Function<Player, Gui<?>> fallbackGui;
    private boolean valid;
    private GuiCloseReason closeReason;

    /**
     * Constructs a new GUI
     * @param uniqueId a UUID unique to this type of inventory.  Generally every class
     * @param plugin
     * @param player
     * @param lines
     * @param initialTitle
     */
    public Gui(UUID uniqueId, ProditySpigotPlugin plugin, Player player, int lines, String initialTitle) {
        Preconditions.checkNotNull(plugin, "plugin");
        Preconditions.checkNotNull(player, "player");
        Preconditions.checkArgument(lines >= 1, "lines<1");
        Preconditions.checkNotNull(initialTitle, "initialTitle");

        this.uniqueId = uniqueId;
        this.plugin = plugin;
        this.player = player;
        this.slots = Maps.newHashMap();
        this.closeHandlers = Lists.newLinkedList();
        this.listeners = Lists.newArrayList();
        this.listener = new GuiListener();
        this.title = initialTitle;
        this.inventory = Bukkit.createInventory(this.player, lines * 9, this.title);
        this.guiProvider = plugin.getServices().getService(GuiProvider.class);

        this.firstDraw = true;
        this.valid = false;
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

    public Optional<GuiCloseReason> getCloseReason() {
        return Optional.ofNullable(this.closeReason);
    }

    private void setCloseReason(GuiCloseReason closeReason) {
        if (this.closeReason == null) {
            this.closeReason = closeReason;
        }
    }

    public SELF setTitle(String title) {
        this.title = title;
        if (this.isValid()) {
            this.guiProvider.updateInventoryTitle(this.player, this.inventory, this.title);
        }
        return this.getSelf();
    }

    public boolean isFirstDraw() {
        return this.firstDraw;
    }

    public boolean isValid() {
        return this.valid;
    }

    public Function<Player, Gui<?>> getFallbackGui() {
        return this.fallbackGui;
    }

    public Gui setFallbackGui(Function<Player, Gui<?>> fallbackGui) {
        this.fallbackGui = fallbackGui;
        return this;
    }

    /**
     * Retrieves a Slot from a specific x,y coordinate where
     * y increases as it goes down, and x increases as it goes
     * right.
     *
     * @param x number of columns from the left
     * @param y number of rows from the top
     * @return Slot at that point
     * @throws IllegalArgumentException if the coordinate doesn't represent a Slot
     */
    public Slot getSlot(int x, int y) {
        return this.getSlot(y * 9 + x);
    }

    public Slot getSlot(int slot) {
        if (slot < 0 || slot >= this.inventory.getSize()) {
            throw new IllegalArgumentException("Invalid slot id: " + slot);
        }

        return this.slots.computeIfAbsent(slot, i -> new SimpleSlot(this, i));
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

    private MetadataValue createMetadataKey() {
        return new FixedMetadataValue(this.plugin, this.uniqueId);
    }

    public void open() throws IllegalStateException {
        if (this.valid) {
            throw new IllegalStateException("gui is already open");
        }
        if (this.title == null) {
            throw new IllegalStateException("no title was set");
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

        final MetadataValue metadataValue = this.createMetadataKey();
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
            if (!Objects.equals(inventory, Gui.this.getHandle())) {
                return;
            }

            final Function<Player, Gui<?>> fallbackFunction = Gui.this.getFallbackGui();
            final Gui<?> fallback = fallbackFunction == null ? null : fallbackFunction.apply(player);

            if (Gui.this.closeReason == null) {
                final List<MetadataValue> metadata = player.getMetadata(Gui.METADATA_KEY);
                final Object metadataValue = metadata == null || metadata.isEmpty() ? null : metadata.get(0).value();
                if (metadataValue != null && !metadataValue.equals(Gui.this.uniqueId)) {
                    Gui.this.closeReason = GuiCloseReason.INVENTORY_OVERRIDE;
                } else {
                    Gui.this.closeReason = GuiCloseReason.PLAYER_CLOSED;
                }
            }

            Gui.this.invalidate();

            Bukkit.getScheduler().runTaskLater(Gui.this.getPlugin(), () -> {
                if (player.isOnline() && fallback != null) {
                    fallback.open();
                }
            }, 1L);
        }
    }

}