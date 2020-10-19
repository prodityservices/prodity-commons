package io.prodity.commons.spigot.legacy.gui.scheme;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.prodity.commons.spigot.legacy.gui.Gui;
import io.prodity.commons.spigot.legacy.gui.GuiItem;
import io.prodity.commons.spigot.legacy.gui.slot.Slot;

import java.util.List;
import java.util.function.Consumer;

public class MenuPopulator implements Cloneable {

    private final Gui gui;
    private final ImmutableList<Integer> slots;
    protected List<Integer> remainingSlots;
    private Slot lastSlotUsed;

    public MenuPopulator(Gui<?> gui, MenuScheme scheme) {
        Preconditions.checkNotNull(gui, "gui");
        Preconditions.checkNotNull(scheme, "scheme");

        this.remainingSlots = scheme.getMaskedIndexes();
        Preconditions.checkArgument(this.remainingSlots.size() > 0, "no slots in scheme");

        this.gui = gui;
        this.slots = ImmutableList.copyOf(this.remainingSlots);
    }

    public MenuPopulator(Gui<?> gui, List<Integer> slots) {
        Preconditions.checkNotNull(gui, "gui");
        Preconditions.checkNotNull(slots, "slots");
        Preconditions.checkArgument(slots.size() > 0, "no slots in list");

        this.gui = gui;
        this.slots = ImmutableList.copyOf(slots);
        this.remainingSlots = Lists.newArrayList(this.slots);
    }

    private MenuPopulator(MenuPopulator other) {
        this.gui = other.gui;
        this.slots = other.slots;
        this.remainingSlots = Lists.newArrayList(this.slots);
    }

    public ImmutableList<Integer> getSlots() {
        return this.slots;
    }

    public Slot getLastSlotUsed() {
        return this.lastSlotUsed;
    }

    public void reset() {
        this.remainingSlots = Lists.newArrayList(this.slots);
    }

    public MenuPopulator consume(Consumer<Slot> action) {
        if (this.tryConsume(action)) {
            return this;
        } else {
            throw new IllegalStateException("No more slots");
        }
    }

    public MenuPopulator consumeIfSpace(Consumer<Slot> action) {
        this.tryConsume(action);
        return this;
    }

    public boolean tryConsume(Consumer<Slot> action) {
        Preconditions.checkNotNull(action, "action");
        if (this.remainingSlots.isEmpty()) {
            return false;
        }

        final int slotIndex = this.remainingSlots.remove(0);
        final Slot slot = this.gui.getSlot(slotIndex);
        action.accept(slot);
        this.lastSlotUsed = slot;
        return true;
    }

    public MenuPopulator accept(GuiItem item) {
        return this.consume(slot -> slot.applyFromItem(item));
    }

    public MenuPopulator acceptIfSpace(GuiItem item) {
        return this.consumeIfSpace(slot -> slot.applyFromItem(item));
    }

    public boolean placeIfSpace(GuiItem item) {
        return this.tryConsume(slot -> slot.applyFromItem(item));
    }

    public int getRemainingSpace() {
        return this.remainingSlots.size();
    }

    public boolean hasSpace() {
        return !this.remainingSlots.isEmpty();
    }

    @Override
    public MenuPopulator clone() {
        return new MenuPopulator(this);
    }

}