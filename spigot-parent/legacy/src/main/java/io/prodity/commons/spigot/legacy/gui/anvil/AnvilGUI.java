
package io.prodity.commons.spigot.legacy.gui.anvil;

import io.prodity.commons.spigot.legacy.gui.anvil.click.GUIClickable;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

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

    @Setter(AccessLevel.NONE)
    private String defaultText = "";

    public AnvilGUI(AnvilFactory anvilFactory, JavaPlugin plugin) {
        this.anvilFactory = anvilFactory;
        this.plugin = plugin;
    }

    /**
     * Opens the {@link Inventory} of this {@link AnvilGUI}.
     *
     * @param player The {@link Player} to open the {@link Inventory} for.
     * @param title The title to set. Currently does not work.
     * @param defaultText The default text for the anvil.
     */
    public final void open(@NonNull final Player player, @NonNull final String title, final String defaultText) {
        this.defaultText = defaultText;
        this.player = player;

        if (!isOpen()) {
            this.inventory = this.anvilFactory.createInventory(this.player);
        }


        this.anvilFactory.registerGui(this);
        Bukkit.getPluginManager().registerEvents(this.listener = new AnvilGUIListener(), this.plugin);

        setClickableItems();

        onOpen();

        this.inventory.addListener(new AnvilInputListener() {

            @Override
            public void onNameChange(final String oldName, final String name) {
                AnvilGUI.this.updateSlot(AnvilSlot.OUTPUT);
                AnvilGUI.this.handleInput(oldName);
            }

            @Override
            public void onInvalidName(final String name) {
                AnvilGUI.this.handleInvalidInput(name);
            }
        });
        updateAll();
        this.inventory.setCurrentText((defaultText == null) ? "" : ChatColor.stripColor(defaultText.trim()));
    }

    public String getCurrentText() {
        return this.inventory.getCurrentText();
    }

    public void terminate() {
        if (this.isDisposing) {
            return;
        }
        this.isDisposing = true;

        if (this.inventory != null) {
            onClose();
            close();
            this.inventory = null;
        }

        if (this.listener != null) {
            HandlerList.unregisterAll(this.listener);
            this.listener = null;
        }
        this.player = null;
        this.isDisposing = false;
    }

    @Override
    public int getSize() {
        return 3;
    }

    /**
     * Closes this {@link AnvilGUI}.
     *
     * @return This instance of {@link AnvilGUI}.
     */
    public final AnvilGUI close() {
        if ((this.inventory != null) && (this.player != null)) {
            this.inventory.clear();
            this.player.closeInventory();
        }
        return this;
    }

    @Override
    public void setItem(final int slot, final ItemStack item) throws IndexOutOfBoundsException {
        if (isOpen()) {
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
    public void handleInput(final String oldText) {
        updateSlot(AnvilSlot.OUTPUT);
    }

    public void handleInvalidInput(final String invalidInput) {
        updateSlot(AnvilSlot.OUTPUT);
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
    public final void handleClickEvent(final InventoryClickEvent event) {
        if (!isClickingEnabled() || ((getClickCooldown() != null) && !getClickCooldown().test())) {
            event.setCancelled(true);
            updateSlot(AnvilSlot.MIDDLE);
            return;
        }

        final GUIClickable clickable = getClickable(event.getSlot());
        if ((clickable != null) && clickable.handlesClicks() && (!clickable.isRequireTopInventory() || getInventory().equals(event
            .getClickedInventory()))) {
            clickable.handleClick(this, event);
            return;
        }

        unhandledClick(event);
    }

    /**
     * Invokes the super method and also updates the {@link AnvilSlot#OUTPUT}.
     */
    @Override
    protected void unhandledClick(final InventoryClickEvent event) {
        event.setCancelled(true);
        updateSlot(AnvilSlot.MIDDLE);
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
        final ItemStack item = getInputItem();
        this.inventory.setItem(0, item);
        return item;
    }

    ItemStack getMiddleItem0() {
        final ItemStack item = getMiddleItem();
        this.inventory.setItem(1, item);
        return item;
    }

    ItemStack getOutputItem0() {
        final ItemStack item = getOutputItem();
        this.inventory.setItem(2, item);
        return item;
    }

    /**
     * Updates the {@link ItemStack} for the specified {@link AnvilSlot}(s), but only if this {@link AnvilGUI} is open.
     *
     * @param slots The {@link AnvilSlot}(s).
     * @return This {@link AnvilGUI} instance.
     */
    public AnvilGUI updateSlot(final AnvilSlot... slots) {
        if (isOpen()) {
            Stream.of(slots).forEach(slot -> setItem(slot.intValue(), slot.fetchItem(this)));
        }
        return this;
    }

    /**
     * Updates all {@link AnvilSlot}s for this {@link AnvilGUI}.
     *
     * @return This {@link AnvilGUI} instance.
     */
    public AnvilGUI updateAll() {
        updateSlot(AnvilSlot.MIDDLE, AnvilSlot.INPUT, AnvilSlot.OUTPUT);
        return this;
    }

    public String getDefaultText() {
        return null;
    }

    //

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
        public void onInvClick(final InventoryClickEvent event) {
            if (event.getInventory().equals(AnvilGUI.this.inventory) && isOpen()) {
                handleClickEvent(event);
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void invDragEvent(final InventoryDragEvent event) {
            if (event.getInventory().equals(AnvilGUI.this.inventory) && isOpen()) {
                handleDragEvent(event);
            }
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void invClose(final InventoryCloseEvent event) {
            if (!event.getInventory().equals(AnvilGUI.this.inventory)) {
                return;
            }
            AnvilGUI.this.anvilFactory.unregisterGui(AnvilGUI.this);
        }

    }

}
