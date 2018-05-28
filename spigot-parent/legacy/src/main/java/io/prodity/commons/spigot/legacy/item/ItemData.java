package io.prodity.commons.spigot.legacy.item;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemData {

    public static ItemData with(Material type) {
        return new ItemData(type, 0, 1);
    }

    public static ItemData withData(Material type, int data) {
        return new ItemData(type, data, 1);
    }

    public static ItemData withAmount(Material type, int amount) {
        return new ItemData(type, 0, amount);
    }

    public static ItemData withDataAndAmount(Material type, int data, int amount) {
        return new ItemData(type, data, amount);
    }

    @Getter
    private final Material type;
    @Getter
    @Setter
    private int amount;
    @Getter
    @Setter
    private int data;

    private ItemData(Material type, int data, int amount) {
        this.type = type;
        this.amount = amount;
        this.data = data;
    }

    public ItemStack toItemStack() {
        return new ItemStack(this.type, this.amount, (short) this.data);
    }

}
