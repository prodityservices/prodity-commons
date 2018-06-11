package io.prodity.commons.spigot.legacy.item.builder.construct;

import io.prodity.commons.spigot.legacy.builder.construct.BuilderConstruction;
import io.prodity.commons.spigot.legacy.item.ItemData;
import io.prodity.commons.spigot.legacy.item.builder.ImmutableItemBuilder;
import io.prodity.commons.spigot.legacy.item.builder.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class ItemConstruction implements BuilderConstruction<ItemStack> {

    public static ItemConstruction start(ItemBuilder builder) {
        final ItemData itemData = ItemData.withDataAndAmount(builder.getMaterial(), builder.getData(), builder.getAmount());
        final ItemMeta itemMeta = itemData.toItemStack().getItemMeta();
        return new ItemConstruction(builder, itemData, itemMeta);
    }

    @Getter
    private final ImmutableItemBuilder builder;
    @Getter
    private final ItemData itemData;
    @Getter
    @Setter
    private ItemMeta itemMeta;

    private ItemConstruction(ItemBuilder builder, ItemData itemData, ItemMeta itemMeta) {
        this.builder = builder.toImmutable();
        this.itemMeta = itemMeta;
        this.itemData = itemData;
    }

    @Override
    public ItemStack construct() {
        final ItemStack itemStack = this.itemData.toItemStack();
        itemStack.setItemMeta(this.itemMeta);
        return itemStack;
    }

}