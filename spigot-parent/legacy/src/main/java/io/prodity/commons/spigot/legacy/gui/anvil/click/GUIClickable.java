
package io.prodity.commons.spigot.legacy.gui.anvil.click;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import io.prodity.commons.spigot.legacy.gui.anvil.InventoryGUI;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a clickable {@link Object} linked to specific slots in a chest GUI.
 * <p>
 * Created on Jan 18, 2017.
 *
 * @author FakeNeth
 */
@Getter
@Setter
@Accessors(chain = true)
public class GUIClickable {

    /**
     * The slot this {@link GUIClickable} is assigned to.
     */
    private final ImmutableList<Integer> slots;

    /**
     * The {@link GUIClickHandler} for this {@link GUIClickable}.
     */
    private final GUIClickHandler clickHandler;

    /**
     * Whether or not the {@link GUIClickable} requires that the {@link Inventory} be the top view in order to trigger the {@link GUIClickHandler}.
     */
    private boolean requireTopInventory = true;

    /**
     * The {@link BiFunction} used to retrieve the {@link ItemStack}, if any, that is to be set to the specified slot of the specified {@link InventoryGUI}.
     */
    @Setter(AccessLevel.NONE)
    private BiFunction<InventoryGUI, Integer, ItemStack> cachedItem;

    /**
     * Whether or not this {@link GUIClickable} should automatically cancel the event after it is handled. Defaults to true.
     */
    private boolean autoCancel = true;

    /**
     * @param slots The {@link Collection} of slots that this {@link GUIClickable} is applicable for.
     */
    public GUIClickable(final Collection<? extends Number> slots) {
        this(null, slots);
    }

    /**
     * @param slots The slots that this {@link GUIClickable} is applicable for.
     */
    public GUIClickable(final Number... slots) {
        this(null, slots);
    }

    /**
     * @param clickHandler The {@link GUIClickHandler} used by this {@link GUIClickable}.
     * @param slots The {@link Collection} of slots that this {@link GUIClickable} is applicable for.
     */
    public GUIClickable(final GUIClickHandler clickHandler, final Collection<? extends Number> slots) {
        this.clickHandler = clickHandler;
        this.slots = ImmutableList.<Integer>builder().addAll(slots.stream().mapToInt(Number::intValue).iterator()).build();
    }

    /**
     * @param clickHandler The {@link GUIClickHandler} used by this {@link GUIClickable}.
     * @param slots The slots that this {@link GUIClickable} is applicable for.
     */
    public GUIClickable(final GUIClickHandler clickHandler, final Number... slots) {
        this.clickHandler = clickHandler;

        final Builder<Integer> builder = ImmutableList.builder();
        for (int i = 0; i < slots.length; i++) {
            builder.add(slots[i].intValue());
        }
        this.slots = builder.build();
    }

    /**
     * Handles an {@link InventoryClickEvent} in an {@link InventoryGUI}.
     *
     * @param gui The {@link InventoryGUI} the {@link InventoryClickEvent} occured in.
     * @param event The {@link InventoryClickEvent}.
     */
    public void handleClick(@NonNull final InventoryGUI gui, @NonNull final InventoryClickEvent event) {
        if (this.clickHandler != null) {
            this.clickHandler.handleClick(new GUIClickEvent(gui, this, event));
        }
        if (isAutoCancel()) {
            event.setCancelled(true);
        }
    }

    /**
     * Sets the specified {@link ItemStack} to all of the {@link GUIClickable#getSlots()} in the specified {@link InventoryGUI}.
     *
     * @param inventory The {@link InventoryGUI} to set the {@link ItemStack}s in.
     * @param item The {@link ItemStack} to set.
     * @return This {@link GUIClickable} instance.
     */
    public GUIClickable setItemToSlots(final InventoryGUI inventory, final ItemStack item) {
        getSlots().stream().filter(slot -> slot < inventory.getSize()).forEach(slot -> inventory.setItem(slot, item));
        return this;
    }

    /**
     * Gets whether or not this {@link GUIClickable} has a cached {@link ItemStack}.
     *
     * @return True if it does, false if not.
     */
    public boolean hasCachedItem() {
        return this.cachedItem != null;
    }

    /**
     * Gets the cached {@link ItemStack} of this {@link GUIClickable}.
     *
     * @param inventory The {@link InventoryGUI} the {@link ItemStack} is for.
     * @param slot The slot of the {@link InventoryGUI} the {@link ItemStack} is for.
     * @return The cached {@link ItemStack}, or null if there is no cached {@link ItemStack} or the cached item fetcher returns null.
     */
    public ItemStack getCachedItem(@NonNull final InventoryGUI inventory, @NonNull final Number slot) {
        return hasCachedItem() ? this.cachedItem.apply(inventory, slot.intValue()) : null;
    }

    /**
     * Sets the {@link #getCachedItem()} of this {@link GUIClickable} to all of the {@link GUIClickable#getSlots()} in the specified {@link InventoryGUI}, but
     * only if this {@link GUIClickable}'s {@link #hasCachedItem()} returns true.
     *
     * @param inventory The {@link InventoryGUI} to set the {@link ItemStack}s in.
     * @return This {@link GUIClickable} instance.
     */
    public GUIClickable applyCachedItem(final InventoryGUI inventory) {
        if (hasCachedItem()) {
            getSlots().stream().filter(slot -> slot < inventory.getSize())
                .forEach(slot -> inventory.setItem(slot, getCachedItem(inventory, slot)));
        }
        return this;
    }

    /**
     * Disables auto event cancellation for this {@link GUIClickable}.
     *
     * @return This {@link GUIClickable}.
     */
    public GUIClickable disableAutoCancel() {
        this.autoCancel = false;
        return this;
    }

    /**
     * Sets this {@link GUIClickable}'s cached item fetcher to always return the specified {@link ItemStack}.
     *
     * @param item The {@link ItemStack}.
     * @return This {@link GUIClickable} instance.
     */
    public GUIClickable setCachedItem(final ItemStack item) {
        return setCachedItem((inv, slot) -> item);
    }

    /**
     * Sets this {@link GUIClickable}'s cached item fetcher to always use the specified {@link Supplier} to get the {@link ItemStack} to be used.
     *
     * @param itemSupplier The {@link ItemStack} {@link Supplier} to be used.
     * @return This {@link GUIClickable} instance.
     */
    public GUIClickable setCachedItem(@NonNull final Supplier<ItemStack> itemSupplier) {
        return setCachedItem((inv, slot) -> itemSupplier.get());
    }

    /**
     * Sets this {@link GUIClickable}'s cached item fetcher to the specified {@link BiFunction}.
     *
     * @param cachedItem The {@link ItemStack} {@link BiFunction} to be used.
     * @return This {@link GUIClickable} instance.
     */
    public GUIClickable setCachedItem(final BiFunction<InventoryGUI, Integer, ItemStack> cachedItem) {
        this.cachedItem = cachedItem;
        return this;
    }

    /**
     * Gets whether or not this {@link GUIClickEvent} handles clicks, meaning it has a {@link GUIClickHandler} assigned.
     *
     * @return True if it does, false if not.
     */
    public boolean handlesClicks() {
        return this.clickHandler != null;
    }

}
