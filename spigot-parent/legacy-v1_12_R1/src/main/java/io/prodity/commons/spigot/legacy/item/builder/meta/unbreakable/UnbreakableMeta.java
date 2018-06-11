package io.prodity.commons.spigot.legacy.item.builder.meta.unbreakable;

import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import io.prodity.commons.spigot.legacy.item.builder.construct.ItemConstruction;
import io.prodity.commons.spigot.legacy.item.builder.meta.AbstractItemBuilderMeta;
import java.util.function.Function;
import org.bukkit.inventory.ItemStack;

public abstract class UnbreakableMeta extends AbstractItemBuilderMeta<Boolean> {

    public static final BuilderMetaKey<Boolean, ItemConstruction, MutableUnbreakableMeta, ImmutableUnbreakableMeta> KEY =
        BuilderMetaKey.<Boolean, ItemConstruction, MutableUnbreakableMeta, ImmutableUnbreakableMeta>builder()
            .mutableInstanceClass(MutableUnbreakableMeta.class)
            .immutableInstanceClass(ImmutableUnbreakableMeta.class)
            .immutableSupplier(UnbreakableMeta::immutable)
            .immutableFunction(UnbreakableMeta::immutable)
            .mutableSupplier(UnbreakableMeta::mutable)
            .mutableFunction(UnbreakableMeta::mutable)
            .build();

    public static ImmutableUnbreakableMeta immutable() {
        return new ImmutableUnbreakableMeta(false);
    }

    public static ImmutableUnbreakableMeta immutable(boolean value) {
        return new ImmutableUnbreakableMeta(value);
    }

    public static ImmutableUnbreakableMeta immutable(ItemStack itemStack) {
        return (ImmutableUnbreakableMeta) UnbreakableMeta
            .fromItemStack(itemStack, UnbreakableMeta::immutable);
    }

    public static MutableUnbreakableMeta mutable() {
        return new MutableUnbreakableMeta(false);
    }

    public static MutableUnbreakableMeta mutable(boolean value) {
        return new MutableUnbreakableMeta(value);
    }

    public static MutableUnbreakableMeta mutable(ItemStack itemStack) {
        return (MutableUnbreakableMeta) UnbreakableMeta
            .fromItemStack(itemStack, UnbreakableMeta::mutable);
    }

    private static UnbreakableMeta fromItemStack(ItemStack itemStack, Function<Boolean, UnbreakableMeta> instanceCreator) {
        final boolean unbreakable = itemStack.getItemMeta().isUnbreakable();
        return instanceCreator.apply(unbreakable);
    }

    protected UnbreakableMeta() {
        super(UnbreakableMeta.KEY);
    }

    @Override
    public boolean canApply(ItemConstruction construction) {
        return true;
    }

    @Override
    protected void applyInternally(ItemConstruction construction, Boolean value) {
        construction.getItemMeta().setUnbreakable(value == null ? false : value);
    }

    @Override
    public MutableUnbreakableMeta toMutable() {
        return (MutableUnbreakableMeta) super.toMutable();
    }

    @Override
    public ImmutableUnbreakableMeta toImmutable() {
        return (ImmutableUnbreakableMeta) super.toImmutable();
    }

}