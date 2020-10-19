
package io.prodity.commons.spigot.legacy.gui.anvil;

import io.prodity.commons.spigot.legacy.gui.Gui;
import io.prodity.commons.spigot.legacy.gui.anvil.click.GUIClickable;
import io.prodity.commons.spigot.legacy.gui.close.GuiCloseReason;
import io.prodity.commons.spigot.legacy.plugin.PluginUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * A GUI for an {@link AnvilInventory}, allowing textual input & a bit more.
 * <p>
 * Created on Jan 18, 2017.
 *
 * @author FakeNeth
 */
@Getter
@Accessors(chain = true)
public abstract class AnvilGUI extends AbstractInventoryGUI<AnvilGUI> {

    private final UUID uniqueId;

    @Getter
    private final AnvilFactory anvilFactory;
    private final JavaPlugin plugin;

    /**
     * The {@link Player} associated with this {@link AnvilGUI}.
     */
    private Player player;

    /**
     * The {@link Inventory} this {@link AnvilGUI} uses..
     */
    private AnvilGuiInventory inventory;

    /**
     * The {@link AnvilGUIListener} for this {@link AnvilGUI}.
     */
    @Getter(AccessLevel.NONE)
    protected AnvilGUIListener listener;

    @Getter(AccessLevel.NONE)
    private boolean isDisposing = false;

    private GuiCloseReason closeReason;

    private boolean valid;

    public AnvilGUI(UUID uniqueId, AnvilFactory anvilFactory, JavaPlugin plugin) {
        this.uniqueId = uniqueId;
        this.anvilFactory = anvilFactory;
        this.plugin = plugin;
    }

    /**
     * Opens the {@link Inventory} of this {@link AnvilGUI}.
     *
     * @param player The {@link Player} to open the {@link Inventory} for.
     * @param defaultText The default text for the anvil.
     */
    public final void open(@NonNull Player player, @Nullable String defaultText) {
        if (this.valid) {
            throw new IllegalStateException("AnvilGUI is already open");
        }

        this.player = player;

        final MetadataValue metadataValue = this.createMetadataValue();
        this.player.setMetadata(Gui.METADATA_KEY, metadataValue);

        final InventoryView activeInventory = this.player.getOpenInventory();
        if (activeInventory != null) {
            activeInventory.close();
        }

        if (!this.isOpen()) {
            this.inventory = this.anvilFactory.createInventory(this.player);
        }

        this.anvilFactory.registerGui(this);
        Bukkit.getPluginManager().registerEvents(this.listener = new AnvilGUIListener(), this.plugin);

        this.setClickableItems();

        this.inventory.open();
        this.valid = true;

        this.onOpen();

        this.inventory.addListener(new AnvilInputListener() {

            @Override
            public void onNameChange(String oldName, String name) {
                AnvilGUI.this.updateSlot(AnvilSlot.OUTPUT);
                AnvilGUI.this.handleInput(oldName);
            }

            @Override
            public void onInvalidName(String name) {
                AnvilGUI.this.handleInvalidInput(name);
            }

        });

        this.updateAll();
        this.inventory.setCurrentText((defaultText == null) ? " " : defaultText);
    }

    protected void onOpen() {
    }

    private MetadataValue createMetadataValue() {
        final JavaPlugin providingPlugin = PluginUtil.getProvidingPlugin();
        return new FixedMetadataValue(providingPlugin, this.uniqueId);
    }

    public String getCurrentText() {
        final String text = this.inventory.getCurrentText();
        return text == null ? "" : text;
    }

    public void close() {
        this.setCloseReason(GuiCloseReason.FORCEFUL);
        this.player.closeInventory();
    }

    private void invalidate() {
        this.valid = false;
        this.inventory.clear();
        HandlerList.unregisterAll(this.listener);
        this.anvilFactory.unregisterGui(this);
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.onClose(this.closeReason), 1L);
    }

    protected void onClose(GuiCloseReason closeReason) {
    }

    private void setCloseReason(@Nullable GuiCloseReason closeReason) {
        if (this.closeReason == null) {
            this.closeReason = closeReason;
        }
    }

    @Override
    public int getSize() {
        return 3;
    }

    @Override
    public void setItem(int slot, ItemStack item) throws IndexOutOfBoundsException {
        if (this.isOpen()) {
            AnvilSlot.fromNumerical(slot).setItem(this, item);
        }
    }

    /**
     * Handles input from the {@link Player}.
     * <p>
     * Defaults to doing nothing; can be overridden to change.
     *
     * @param oldText The text that was replaced by the current text (which was inputted).
     */
    public void handleInput(String oldText) {
        this.updateSlot(AnvilSlot.OUTPUT);
    }

    public void handleInvalidInput(String invalidInput) {
        this.updateSlot(AnvilSlot.OUTPUT);
    }

    /**
     * Sets the current text of this {@link AnvilGUI} if this {@link AnvilGUI} is open. Does not invoke {@link #handleInput(String)}.
     *
     * @param text The text to set.
     */
    public void setCurrentText(String text) {
        text = text.trim();
        this.inventory.setCurrentText(text);
    }

    @Override
    public final void handleClickEvent(InventoryClickEvent event) {
        if (!this.isClickingEnabled() || ((this.getClickCooldown() != null) && !this.getClickCooldown().test())) {
            event.setCancelled(true);
            this.updateSlot(AnvilSlot.MIDDLE);
            return;
        }

        final GUIClickable clickable = this.getClickable(event.getSlot());
        if ((clickable != null) && clickable.handlesClicks() && (!clickable.isRequireTopInventory() || this.getInventory().equals(event
            .getClickedInventory()))) {
            clickable.handleClick(this, event);
            return;
        }

        this.unhandledClick(event);
    }

    /**
     * Invokes the super method and also updates the {@link AnvilSlot#OUTPUT}.
     */
    @Override
    protected void unhandledClick(InventoryClickEvent event) {
        event.setCancelled(true);
        this.updateSlot(AnvilSlot.MIDDLE);
    }

    /**
     * Gets the {@link ItemStack} that should be in the {@link AnvilSlot#INPUT}.
     * <p>
     * The {@link ItemStack} does not represent the {@link ItemStack} currently in the {@link Inventory}, as this method is called to get the {@link ItemStack}
     * that <i>should</i> be in the {@link AnvilSlot#INPUT}.
     * <p>
     * Defaults to returning null; can be overridden to change.
     *
     * @return The input slot {@link ItemStack}. Null if there should be no {@link ItemStack} in the slot, however this will not allow any text input.
     */
    public ItemStack getInputItem() {
        return this.inventory.getItem(0);
    }

    /**
     * Gets the {@link ItemStack} that should be in the {@link AnvilSlot#MIDDLE}.
     * <p>
     * The {@link ItemStack} does not represent the {@link ItemStack} currently in the {@link Inventory}, as this method is called to get the {@link ItemStack}
     * that <i>should</i> be in the {@link AnvilSlot#MIDDLE}.
     * <p>
     * Defaults to returning null; can be overridden to change.
     *
     * @return The input slot {@link ItemStack}. Null if there should be no {@link ItemStack} in the slot.
     */
    public ItemStack getMiddleItem() {
        return this.inventory.getItem(1);
    }

    /**
     * Gets the {@link ItemStack} that should be in the {@link AnvilSlot#OUTPUT}.
     * <p>
     * The {@link ItemStack} does not represent the {@link ItemStack} currently in the {@link Inventory}, as this method is called to get the {@link ItemStack}
     * that <i>should</i> be in the {@link AnvilSlot#OUTPUT}.
     * <p>
     * Defaults to returning null; can be overridden to change.
     *
     * @return The input slot {@link ItemStack}. Null if there should be no {@link ItemStack} in the slot.
     */
    public ItemStack getOutputItem() {
        return this.inventory.getItem(2);
    }

    ItemStack getInputItem0() {
        final ItemStack item = this.getInputItem();
        this.inventory.setItem(0, item);
        return item;
    }

    ItemStack getMiddleItem0() {
        final ItemStack item = this.getMiddleItem();
        this.inventory.setItem(1, item);
        return item;
    }

    ItemStack getOutputItem0() {
        final ItemStack item = this.getOutputItem();
        this.inventory.setItem(2, item);
        return item;
    }

    /**
     * Updates the {@link ItemStack} for the specified {@link AnvilSlot}(s), but only if this {@link AnvilGUI} is open.
     *
     * @param slots The {@link AnvilSlot}(s).
     * @return This {@link AnvilGUI} instance.
     */
    public AnvilGUI updateSlot(AnvilSlot... slots) {
        if (this.isOpen()) {
            Stream.of(slots).forEach(slot -> this.setItem(slot.intValue(), slot.fetchItem(this)));
        }
        return this;
    }

    /**
     * Updates all {@link AnvilSlot}s for this {@link AnvilGUI}.
     *
     * @return This {@link AnvilGUI} instance.
     */
    public AnvilGUI updateAll() {
        this.updateSlot(AnvilSlot.MIDDLE, AnvilSlot.INPUT, AnvilSlot.OUTPUT);
        return this;
    }

    public String getDefaultText() {
        return null;
    }

    /**
     * The {@link Listener} for {@link AnvilGUI}.
     * <p>
     * Created on Nov 19, 2016.
     *
     * @author FakeNeth
     */
    protected class AnvilGUIListener implements Listener {

        public AnvilGUIListener() {
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onInvClick(InventoryClickEvent event) {
            if (event.getInventory().equals(AnvilGUI.this.inventory) && AnvilGUI.this.isOpen()) {
                AnvilGUI.this.handleClickEvent(event);
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void invDragEvent(InventoryDragEvent event) {
            if (event.getInventory().equals(AnvilGUI.this.inventory) && AnvilGUI.this.isOpen()) {
                AnvilGUI.this.handleDragEvent(event);
            }
        }

        @EventHandler
        public void on(PlayerQuitEvent event) {
            final Player player = event.getPlayer();
            if (!Objects.equals(player, AnvilGUI.this.getPlayer())) {
                return;
            }

            AnvilGUI.this.closeReason = GuiCloseReason.PLAYER_QUIT;
            AnvilGUI.this.invalidate();
        }

        @EventHandler
        public void on(InventoryCloseEvent event) {
            final Player player = (Player) event.getPlayer();
            if (!Objects.equals(player, AnvilGUI.this.getPlayer())) {
                return;
            }
            final Inventory inventory = event.getInventory();
            if (!Objects.equals(inventory, AnvilGUI.this.inventory)) {
                return;
            }

            if (AnvilGUI.this.closeReason == null) {
                final Object metadataValue = Gui.getPlayerMetadataValue(player);
                if (!Objects.equals(AnvilGUI.this.uniqueId, metadataValue)) {
                    AnvilGUI.this.closeReason = GuiCloseReason.INVENTORY_OVERRIDE;
                } else {
                    AnvilGUI.this.closeReason = GuiCloseReason.PLAYER_CLOSED;
                }
            }

            AnvilGUI.this.invalidate();
        }
    }

}
