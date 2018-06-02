
package io.prodity.commons.spigot.legacy.gui.anvil;

import io.prodity.commons.spigot.legacy.gui.anvil.click.GUIClickHandler;
import io.prodity.commons.spigot.legacy.gui.anvil.click.GUIClickable;
import java.util.Collection;
import java.util.List;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a GUI within an {@link Inventory}.
 * <p>
 * Created on Jan 19, 2017.
 *
 * @author FakeNeth
 */
public interface InventoryGUI {

    /**
     * Gets whether or not the {@link InventoryGUI} is open.
     *
     * @return True if it is, false if not.
     */
    boolean isOpen();

    /**
     * Sets the {@link ItemStack} to the specified slot in the {@link InventoryGUI}.
     *
     * @param slot The slot to set.
     * @param item The {@link ItemStack} to set.
     * @throws IndexOutOfBoundsException If the specified slot is < 0 or >= the {@link Inventory}'s size.
     */
    void setItem(final int slot, final ItemStack item) throws IndexOutOfBoundsException;

    /**
     * Gets the size of this {@link InventoryGUI}.
     *
     * @return The size.
     */
    int getSize();

    /**
     * Adds the specified {@link GUIClickable}(s) to this {@link InventoryGUI}.
     * <p>
     * If there are any slot conflicts with other {@link GUIClickable}, this {@link GUIClickable} will override any pre-existing ones.
     *
     * @param clickables The {@link GUIClickable}s to add.
     * @return This {@link InventoryGUI} instance.
     */
    InventoryGUI addClickable(final GUIClickable... clickables);

    /**
     * Adds the specified {@link GUIClickable}(s) to this {@link InventoryGUI}.
     * <p>
     * If there are any slot conflicts with other {@link GUIClickable}, this {@link GUIClickable} will override any pre-existing ones.
     *
     * @param clickables The {@link Collection} of {@link GUIClickable}s to add.
     * @return This {@link InventoryGUI} instance.
     */
    InventoryGUI addClickable(final Collection<GUIClickable> clickables);

    /**
     * Removes the specified {@link GUIClickable}(s).
     *
     * @param clickables The {@link Collection} of {@link GUIClickable}s to remove.
     * @return This {@link InventoryGUI} instance.
     */
    InventoryGUI removeClickable(final Collection<GUIClickable> clickables);

    /**
     * Removes the specified {@link GUIClickable}(s).
     *
     * @param clickables The {@link GUIClickable}s to remove.
     * @return This {@link InventoryGUI} instance.
     */
    InventoryGUI removeClickable(final GUIClickable... clickables);

    /**
     * Removes the specified {@link GUIClickable}(s) at the specified slot(s).
     *
     * @param slots The {@link Collection} of slots of the {@link GUIClickable}s to remove.
     * @return This {@link InventoryGUI} instance.
     */
    InventoryGUI removeClickableSlot(final Collection<? extends Number> slots);

    /**
     * Removes the specified {@link GUIClickable}(s) at the specified slot(s).
     *
     * @param slots The slots of the {@link GUIClickable}s to remove.
     * @return This {@link InventoryGUI} instance.
     */
    InventoryGUI removeClickableSlot(final Number... slots);

    /**
     * Gets the {@link GUIClickable} associated with the specified slot.
     *
     * @param slot The slot.
     * @return The {@link GUIClickable}, or null if one doesn't exist for the specified slot.
     */
    GUIClickable getClickable(@NonNull final Number slot);

    /**
     * Gets a {@link List} of all {@link GUIClickable}s for this {@link InventoryGUI}.
     *
     * @return The {@link List} of all {@link GUIClickable}s.
     */
    List<GUIClickable> getClickables();

    /**
     * Gets whether or not clicking is enabled in the {@link InventoryGUI}.
     *
     * @return True if it is, false if not.
     */
    boolean isClickingEnabled();

    /**
     * Gets whether or not clicking is enabled in this {@link InventoryGUI}.<br>
     * If it isn't, no {@link GUIClickHandler}s will be called and all {@link InventoryClickEvent}s will be cancelled.
     *
     * @param clickingEnabled True to enable clicking, false to disable it.
     * @return This {@link InventoryGUI} instance.
     */
    InventoryGUI setClickingEnabled(final boolean clickingEnabled);

    /**
     * Gets whether or not dragging is enabled in the {@link InventoryGUI}.
     *
     * @return True if it is, false if not.
     */
    boolean isDraggingEnabled();

    /**
     * Gets whether or not dragging is enabled in this {@link InventoryGUI}.<br>
     * If it isn't, all {@link InventoryDragEvent}s will be cancelled.
     *
     * @param draggingEnabled True to enable dragging, false to disable it.
     * @return This {@link InventoryGUI} instance.
     */
    InventoryGUI setDraggingEnabled(final boolean draggingEnabled);

    /**
     * Handles an {@link InventoryClickEvent} when it occurs in this {@link InventoryGUI}.
     *
     * @param event The {@link InventoryClickEvent}.
     */
    void handleClickEvent(final InventoryClickEvent event);

    /**
     * Handles an {@link InventoryDragEvent} when it occurs in this {@link InventoryGUI}.
     *
     * @param event The {@link InventoryDragEvent}.
     */
    void handleDragEvent(final InventoryDragEvent event);

    /**
     * Called when the {@link InventoryGUI} is closed. Can be overridden, as it has no default functionality.
     */
    default void onClose() {
    }

    /**
     * Called when the {@link InventoryGUI} is opened. Can be overridden, as it has no default functionality.
     */
    default void onOpen() {
    }

    /**
     * Updates the {@link GUIClickable}s at the specified slots if they exists.
     *
     * @param slots The slots.
     */
    void updateClickables(@NonNull final Number... slots);

    /**
     * Updates the {@link GUIClickable}s at the specified slots if they exists.
     *
     * @param slots The slots.
     */
    void updateClickables(@NonNull final Collection<? extends Number> slots);

    /**
     * Gets the {@link Cooldown} for clicks in this {@link InventoryGUI}.
     *
     * @return The {@link Cooldown}, null if there is none.
     */
    Cooldown getClickCooldown();

    /**
     * Gets whether or not there is a click {@link Cooldown} for this {@link InventoryGUI}.
     *
     * @return True if there is, false if not.
     */
    boolean hasClickCooldown();

    /**
     * Sets the click {@link Cooldown} to the one specified.
     *
     * @param clickCooldown The {@link Cooldown}. Null to disable the click cooldown (not reccomended!).
     * @return This {@link InventoryGUI} instance.
     */
    InventoryGUI setClickCooldown(final Cooldown clickCooldown);

}
