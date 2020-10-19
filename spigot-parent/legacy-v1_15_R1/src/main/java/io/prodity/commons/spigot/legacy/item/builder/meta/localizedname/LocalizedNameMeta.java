package io.prodity.commons.spigot.legacy.item.builder.meta.localizedname;

import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import io.prodity.commons.spigot.legacy.item.builder.construct.ItemConstruction;
import io.prodity.commons.spigot.legacy.item.builder.meta.AbstractItemBuilderMeta;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

public abstract class LocalizedNameMeta extends AbstractItemBuilderMeta<String> {

    public static final BuilderMetaKey<String, ItemConstruction, MutableLocalizedNameMeta, ImmutableLocalizedNameMeta> KEY =
        BuilderMetaKey.<String, ItemConstruction, MutableLocalizedNameMeta, ImmutableLocalizedNameMeta>builder()
            .mutableInstanceClass(MutableLocalizedNameMeta.class)
            .immutableInstanceClass(ImmutableLocalizedNameMeta.class)
            .immutableSupplier(LocalizedNameMeta::immutable)
            .immutableFunction(LocalizedNameMeta::immutable)
            .mutableSupplier(LocalizedNameMeta::mutable)
            .mutableFunction(LocalizedNameMeta::mutable)
            .build();

    public static ImmutableLocalizedNameMeta immutable() {
        return new ImmutableLocalizedNameMeta(null);
    }

    public static ImmutableLocalizedNameMeta immutable(String value) {
        return new ImmutableLocalizedNameMeta(value);
    }

    public static ImmutableLocalizedNameMeta immutable(ItemStack itemStack) {
        return (ImmutableLocalizedNameMeta) LocalizedNameMeta
            .fromItemStack(itemStack, LocalizedNameMeta::immutable);
    }

    public static MutableLocalizedNameMeta mutable() {
        return new MutableLocalizedNameMeta(null);
    }

    public static MutableLocalizedNameMeta mutable(String value) {
        return new MutableLocalizedNameMeta(value);
    }

    public static MutableLocalizedNameMeta mutable(ItemStack itemStack) {
        return (MutableLocalizedNameMeta) LocalizedNameMeta
            .fromItemStack(itemStack, LocalizedNameMeta::mutable);
    }

    private static LocalizedNameMeta fromItemStack(ItemStack itemStack, Function<String, LocalizedNameMeta> instanceCreator) {
        final String name = itemStack.getItemMeta().getLocalizedName();
        return instanceCreator.apply(name);
    }

    protected LocalizedNameMeta() {
        super(LocalizedNameMeta.KEY);
    }

    @Override
    public boolean canApply(ItemConstruction construction) {
        return true;
    }

    @Override
    protected void applyInternally(ItemConstruction construction, String value) {
        construction.getItemMeta().setLocalizedName(value);
    }

    @Override
    public MutableLocalizedNameMeta toMutable() {
        return (MutableLocalizedNameMeta) super.toMutable();
    }

    @Override
    public ImmutableLocalizedNameMeta toImmutable() {
        return (ImmutableLocalizedNameMeta) super.toImmutable();
    }

}