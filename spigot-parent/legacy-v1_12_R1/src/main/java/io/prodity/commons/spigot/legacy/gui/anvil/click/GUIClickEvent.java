
package io.prodity.commons.spigot.legacy.gui.anvil.click;

import io.prodity.commons.spigot.legacy.gui.anvil.AbstractInventoryGUI;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

/**
 * A wrapper for an {@link InventoryClickEvent}.
 * <p>
 * Created on Jan 18, 2017.
 *
 * @author FakeNeth
 */
@Getter
public class GUIClickEvent {

    /**
     * The {@link InventoryClickEvent} wrapped by this {@link GUIClickEvent}.
     */
    private final InventoryClickEvent wrappedEvent;

    /**
     * THe {@link Player} who clicked.
     */
    private final Player player;

    /**
     * The {@link AbstractInventoryGUI} the event occurred in.
     */
    private final AbstractInventoryGUI gui;

    /**
     * The {@link GUIClickable} that was clicked and is handling this {@link GUIClickEvent}.
     */
    private final GUIClickable clickable;

    /**
     * @param gui The {@link AbstractInventoryGUI} the event occurred in.
     * @param clickable The {@link GUIClickable} that was clicked and is handling this {@link GUIClickEvent}.
     * @param event The {@link InventoryClickEvent} being wrapped.
     */
    public GUIClickEvent(@NonNull AbstractInventoryGUI gui, @NonNull GUIClickable clickable, @NonNull InventoryClickEvent event) {
        this.gui = gui;
        this.clickable = clickable;
        this.wrappedEvent = event;
        this.player = (Player) this.wrappedEvent.getWhoClicked();
    }

    /**
     * Gets whether or not any {@link Inventory} was clicked.
     *
     * @return True if there was an {@link Inventory} clicked, false if the click was outside of all {@link Inventory}s.
     */
    public boolean wasAnyClicked() {
        return this.wrappedEvent.getClickedInventory() != null;
    }

    /**
     * Gets whether or not the {@link Inventory} of the {@link InventoryView} was clicked.
     *
     * @return True the top {@link Inventory} was clicked, false if not.
     */
    public boolean wasTopClicked() {
        return this.wasAnyClicked() && this.wrappedEvent.getView().getTopInventory().equals(this.wrappedEvent.getClickedInventory());
    }

    /**
     * Gets the {@link Inventory} that was clicked.
     *
     * @return The {@link Inventory}, null if one wasn't clicked.
     */
    public Inventory getClickedInv() {
        return this.wrappedEvent.getClickedInventory();
    }

    /**
     * Sets whether or not the {@link InventoryClickEvent} should be cancelled.
     *
     * @param cancelled True to cancel, false to not.
     */
    public void setCancelled(boolean cancelled) {
        this.wrappedEvent.setCancelled(cancelled);
    }

    /**
     * Gets whether or not an {@link ItemStack} was clicked.
     *
     * @return True if one was, false if not
     */
    public boolean wasItemClicked() {
        return this.wrappedEvent.getCurrentItem() != null && this.wrappedEvent.getCurrentItem().getType() != Material.AIR;
    }

    /**
     * Gets the {@link ItemStack} that was clicked, if any.
     *
     * @return The {@link ItemStack}.
     */
    public ItemStack getItem() {
        return this.wrappedEvent.getCurrentItem();
    }

    /**
     * Updates the {@link ItemStack} for the clicked {@link GUIClickable} if it has a cached {@link ItemStack}.
     *
     * @return This {@link GUIClickEvent}.
     */
    public GUIClickEvent updateSlotItem() {
        this.clickable.applyCachedItem(this.gui);
        return this;
    }

}
