package io.prodity.commons.spigot.legacy.item.builder.meta.displayname;

import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import io.prodity.commons.spigot.legacy.item.builder.construct.ItemConstruction;
import io.prodity.commons.spigot.legacy.item.builder.meta.AbstractItemBuilderMeta;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

public abstract class DisplayNameMeta extends AbstractItemBuilderMeta<String> {

    public static final BuilderMetaKey<String, ItemConstruction, MutableDisplayNameMeta, ImmutableDisplayNameMeta> KEY =
        BuilderMetaKey.<String, ItemConstruction, MutableDisplayNameMeta, ImmutableDisplayNameMeta>builder()
            .immutableInstanceClass(ImmutableDisplayNameMeta.class)
            .mutableInstanceClass(MutableDisplayNameMeta.class)
            .immutableSupplier(DisplayNameMeta::immutable)
            .immutableFunction(DisplayNameMeta::immutable)
            .mutableSupplier(DisplayNameMeta::mutable)
            .mutableFunction(DisplayNameMeta::mutable)
            .build();

    public static ImmutableDisplayNameMeta immutable() {
        return new ImmutableDisplayNameMeta(null);
    }

    public static ImmutableDisplayNameMeta immutable(String value) {
        return new ImmutableDisplayNameMeta(value);
    }

    public static ImmutableDisplayNameMeta immutable(ItemStack itemStack) {
        return (ImmutableDisplayNameMeta) DisplayNameMeta
            .fromItemStack(itemStack, DisplayNameMeta::immutable);
    }

    public static MutableDisplayNameMeta mutable() {
        return new MutableDisplayNameMeta(null);
    }

    public static MutableDisplayNameMeta mutable(String value) {
        return new MutableDisplayNameMeta(value);
    }

    public static MutableDisplayNameMeta mutable(ItemStack itemStack) {
        return (MutableDisplayNameMeta) DisplayNameMeta
            .fromItemStack(itemStack, DisplayNameMeta::mutable);
    }

    private static DisplayNameMeta fromItemStack(ItemStack itemStack, Function<String, DisplayNameMeta> instanceCreator) {
        final String name = itemStack.getItemMeta().getDisplayName();
        return instanceCreator.apply(name);
    }

    protected DisplayNameMeta() {
        super(DisplayNameMeta.KEY);
    }

    @Override
    public boolean canApply(ItemConstruction construction) {
        return true;
    }

    @Override
    protected void applyInternally(ItemConstruction construction, String value) {
        value = value == null ? null : ChatColor.translateAlternateColorCodes('&', value);
        construction.getItemMeta().setDisplayName(value);
    }

    @Override
    public MutableDisplayNameMeta toMutable() {
        return (MutableDisplayNameMeta) super.toMutable();
    }

    @Override
    public ImmutableDisplayNameMeta toImmutable() {
        return (ImmutableDisplayNameMeta) super.toImmutable();
    }

}