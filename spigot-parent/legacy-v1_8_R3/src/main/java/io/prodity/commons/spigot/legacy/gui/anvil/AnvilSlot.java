
package io.prodity.commons.spigot.legacy.gui.anvil;

import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Represents a slot in an {@link AnvilInventory}. Also extends {@link Number} as the slot is numerical.
 * <p>
 * Created on Jan 19, 2017.
 *
 * @author FakeNeth
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnvilSlot extends Number {

    private static final long serialVersionUID = 1963846148985588049L;

    /**
     * The output slot. AKA the right slot.
     */
    public static final AnvilSlot OUTPUT = new AnvilSlot(2, AnvilGUI::getOutputItem0, (gui, stack) -> {
        gui.getInventory().setItem(2, stack);
        gui.getAnvilFactory().fakeSetItem(gui.getPlayer(), 2, stack);
    });

    /**
     * The first input slot. AKA the left slot.
     */
    public static final AnvilSlot INPUT = new AnvilSlot(0, AnvilGUI::getInputItem0, (gui, stack) -> {
        gui.getInventory().setItem(0, stack);
        if (stack != null && stack.hasItemMeta()) {
            final ItemMeta itemMeta = stack.getItemMeta();
            final String cur = gui.getCurrentText();
            itemMeta.setDisplayName(((cur == null) || cur.isEmpty()) ? " " : cur);
            stack.setItemMeta(itemMeta);
        }
        gui.getAnvilFactory().fakeSetItem(gui.getPlayer(), 0, stack);
    });

    /**
     * The middle. AKA the left slot.
     */
    public static final AnvilSlot MIDDLE = new AnvilSlot(1, AnvilGUI::getMiddleItem0, (gui, stack) -> {
        gui.getInventory().setItem(1, stack);
        gui.getAnvilFactory().fakeSetItem(gui.getPlayer(), 1, stack);
    });

    /**
     * Gets all {@link AnvilSlot} values in an array.
     *
     * @return The array of {@link AnvilSlot} values.
     */
    public static AnvilSlot[] values() {
        return new AnvilSlot[]{AnvilSlot.INPUT, AnvilSlot.MIDDLE, AnvilSlot.OUTPUT};
    }

    /**
     * Gets the {@link AnvilSlot} with the specified numerical slot.
     *
     * @param slot The numerical slot.
     * @return The {@link AnvilSlot}, or null if one doesn't exist.
     */
    @Nullable
    public static AnvilSlot fromNumerical(int slot) {
        return (slot == 0) ? AnvilSlot.INPUT : ((slot == 1) ? AnvilSlot.MIDDLE : ((slot == 2) ? AnvilSlot.OUTPUT : null));
    }

    /**
     * The numerical slot value.
     */
    @Delegate(types = {Number.class})
    private final Integer numericalValue;

    private final Function<AnvilGUI, ItemStack> itemSupplier;

    private final BiConsumer<AnvilGUI, ItemStack> itemSetter;

    /**
     * Gets the {@link ItemStack} for the specified {@link AnvilGUI} that should be placed in this {@link AnvilSlot}.
     *
     * @param gui The {@link AnvilGUI}.
     * @return The {@link ItemStack}, or null if one doesn't exist.
     */
    public ItemStack fetchItem(AnvilGUI gui) {
        return this.itemSupplier.apply(gui);
    }

    /**
     * Sets the specified {@link ItemStack} in this {@link AnvilSlot} to the specified {@link AnvilGUI}.
     *
     * @param gui The {@link AnvilGUI}.
     * @param stack The {@link ItemStack} to set.
     */
    public void setItem(AnvilGUI gui, ItemStack stack) {
        this.itemSetter.accept(gui, stack);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof AnvilSlot) {
            return ((AnvilSlot) obj).numericalValue == this.numericalValue;
        }
        if (obj instanceof Number) {
            return obj.equals(this.numericalValue);
        }
        return false;
    }

}
