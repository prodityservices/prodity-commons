
package io.prodity.commons.spigot.legacy.gui.anvil;

import io.prodity.commons.spigot.legacy.gui.anvil.click.GUIClickable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * An abstract implementation of {@link InventoryGUI} containing many of the necessities for basic {@link InventoryGUI} implementations.
 * <p>
 * Created on Jan 19, 2017.
 *
 * @param <T> The extending type for better chained method calls.
 * @author FakeNeth
 */
@Accessors(chain = true)
public abstract class AbstractInventoryGUI<T extends AbstractInventoryGUI<T>> implements InventoryGUI {

    private final T instance = (T) this;

    private final LinkedHashMap<Integer, GUIClickable> clickables = new LinkedHashMap<>();
    private final List<GUIClickable> allClickables = new ArrayList<>();

    @Setter
    @Getter
    private Cooldown clickCooldown;

    @Setter
    @Getter
    private boolean draggingEnabled = false;

    @Setter
    @Getter
    private boolean clickingEnabled = true;

    @Override
    public T addClickable(final GUIClickable... clickables) {
        Stream.of(clickables).forEach(clickable -> {
            clickable.getSlots().forEach(slot -> this.clickables.put(slot, clickable));
            if (!this.allClickables.contains(clickable)) {
                this.allClickables.add(clickable);
            }
        });
        return this.instance;
    }

    @Override
    public T addClickable(final Collection<GUIClickable> clickables) {
        clickables.forEach(clickable -> {
            clickable.getSlots().forEach(slot -> this.clickables.put(slot, clickable));
            if (!this.allClickables.contains(clickable)) {
                this.allClickables.add(clickable);
            }
        });
        return this.instance;
    }

    @Override
    public T removeClickable(final Collection<GUIClickable> clickables) {
        clickables.forEach(clickable -> {
            clickable.getSlots().forEach(this.clickables::remove);
            this.allClickables.remove(clickable);
        });
        return this.instance;
    }

    @Override
    public T removeClickable(final GUIClickable... clickables) {
        Stream.of(clickables).forEach(clickable -> {
            clickable.getSlots().forEach(this.clickables::remove);
            this.allClickables.remove(clickable);
        });
        return this.instance;
    }

    @Override
    public T removeClickableSlot(final Collection<? extends Number> slots) {
        slots.stream().mapToInt(Number::intValue).forEach(i -> {
            final GUIClickable click = this.clickables.remove(i);
            if (!this.clickables.values().contains(click)) {
                this.allClickables.remove(click);
            }
        });
        return this.instance;
    }

    @Override
    public T removeClickableSlot(final Number... slots) {
        Stream.of(slots).mapToInt(Number::intValue).forEach(this.clickables::remove);
        return this.instance;
    }

    @Override
    public GUIClickable getClickable(final Number slot) {
        return this.clickables.get(slot.intValue());
    }

    @Override
    public List<GUIClickable> getClickables() {
        return new ArrayList<>(this.allClickables);
    }

    @Override
    public void handleClickEvent(final InventoryClickEvent event) {
        if (!isClickingEnabled() || (this.clickCooldown != null && !this.clickCooldown.test())) {
            event.setCancelled(true);
            return;
        }
        if (this.clickables.containsKey(event.getSlot())) {
            final GUIClickable clickable = this.clickables.get(event.getSlot());
            if (clickable.handlesClicks() && (!clickable.isRequireTopInventory() || getInventory().equals(event.getClickedInventory()))) {
                clickable.handleClick(this, event);
                return;
            }
        }
        unhandledClick(event);
    }

    @Override
    public final void handleDragEvent(final InventoryDragEvent event) {
        if (!isDraggingEnabled()) {
            event.setCancelled(true);
            return;
        }
        handleDrag(event);
    }

    /**
     * Called when there is an unhnalded {@link InventoryClickEvent} for this {@link InventoryGUI}, meaning no {@link GUIClickable}s were assigned to the slot
     * clicked.
     * <p>
     * The default behaviour of this method is to cancel {@link InventoryClickEvent}, so it must be overriden to prevent that.
     *
     * @param event The {@link InventoryClickEvent}.
     */
    protected void unhandledClick(final InventoryClickEvent event) {
        event.setCancelled(true);
    }

    /**
     * Handles an {@link InventoryDragEvent} if they are enabled for this {@link InventoryGUI}.
     * <p>
     * The default behaviour of this method is to cancel {@link InventoryDragEvent}, so it must be overriden to prevent that.
     *
     * @param event The {@link InventoryDragEvent}.
     */
    protected void handleDrag(final InventoryDragEvent event) {
        event.setCancelled(true);
    }

    @Override
    public boolean hasClickCooldown() {
        return this.clickCooldown != null;
    }

    /**
     * Gets the {@link Inventory} associated with the {@link InventoryGUI}.
     *
     * @return The {@link Inventory}.
     */
    protected abstract Inventory getInventory();

    @Override
    public boolean isOpen() {
        return getInventory() != null;
    }

    @Override
    public void setItem(final int slot, final ItemStack item) throws IndexOutOfBoundsException {
        if (isOpen()) {
            getInventory().setItem(slot, item);
        }
    }

    @Override
    public int getSize() {
        return isOpen() ? getInventory().getSize() : 0;
    }

    /**
     * Sets all of the stored {@link GUIClickable}s' cached {@link ItemStack}s to the {@link Inventory}.
     *
     * @return This instance.
     */
    public T setClickableItems() {
        this.allClickables.stream().filter(GUIClickable::hasCachedItem).forEach(item -> item.applyCachedItem(this));
        return this.instance;
    }

    @Override
    public void updateClickables(@NonNull final Number... slots) {
        Stream.of(slots).forEach(slot -> {
            final GUIClickable clickable = getClickable(slot);
            if (clickable != null) {
                clickable.applyCachedItem(this);
            }
        });
    }

    @Override
    public void updateClickables(@NonNull final Collection<? extends Number> slots) {
        slots.forEach(slot -> {
            final GUIClickable clickable = getClickable(slot);
            if (clickable != null) {
                clickable.applyCachedItem(this);
            }
        });
    }

}
