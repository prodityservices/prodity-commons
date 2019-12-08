package io.prodity.commons.spigot.legacy.item.builder.meta.skullowner;

import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import io.prodity.commons.spigot.legacy.item.builder.construct.ItemConstruction;
import io.prodity.commons.spigot.legacy.item.builder.meta.AbstractItemBuilderMeta;
import java.util.function.Function;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public abstract class SkullOwnerMeta extends AbstractItemBuilderMeta<String> {

    public static final BuilderMetaKey<String, ItemConstruction, MutableSkullOwnerMeta, ImmutableSkullOwnerMeta> KEY =
        BuilderMetaKey.<String, ItemConstruction, MutableSkullOwnerMeta, ImmutableSkullOwnerMeta>builder()
            .mutableInstanceClass(MutableSkullOwnerMeta.class)
            .immutableInstanceClass(ImmutableSkullOwnerMeta.class)
            .immutableSupplier(SkullOwnerMeta::immutable)
            .immutableFunction(SkullOwnerMeta::immutable)
            .mutableSupplier(SkullOwnerMeta::mutable)
            .mutableFunction(SkullOwnerMeta::mutable)
            .build();

    public static boolean isApplicable(ItemStack itemStack) {
        return itemStack.getItemMeta() instanceof SkullMeta;
    }

    public static ImmutableSkullOwnerMeta immutable() {
        return new ImmutableSkullOwnerMeta(null);
    }

    public static ImmutableSkullOwnerMeta immutable(String value) {
        return new ImmutableSkullOwnerMeta(value);
    }

    public static ImmutableSkullOwnerMeta immutable(ItemStack itemStack) {
        return (ImmutableSkullOwnerMeta) SkullOwnerMeta
            .fromItemStack(itemStack, SkullOwnerMeta::immutable);
    }

    public static MutableSkullOwnerMeta mutable() {
        return new MutableSkullOwnerMeta(null);
    }

    public static MutableSkullOwnerMeta mutable(String value) {
        return new MutableSkullOwnerMeta(value);
    }

    public static MutableSkullOwnerMeta mutable(ItemStack itemStack) {
        return (MutableSkullOwnerMeta) SkullOwnerMeta
            .fromItemStack(itemStack, SkullOwnerMeta::mutable);
    }

    private static SkullOwnerMeta fromItemStack(ItemStack itemStack, Function<String, SkullOwnerMeta> instanceCreator) {
        if (!SkullOwnerMeta.isApplicable(itemStack)) {
            throw new IllegalArgumentException("specified ItemStack is not applicable");
        }
        //final OfflinePlayer owner = ((SkullMeta) itemStack.getItemMeta()).getOwningPlayer();
        return instanceCreator.apply(((SkullMeta) itemStack.getItemMeta()).getOwner());
    }

    protected SkullOwnerMeta() {
        super(SkullOwnerMeta.KEY);
    }

    @Override
    public boolean canApply(ItemConstruction construction) {
        return construction.getItemMeta() instanceof SkullMeta;
    }

    @Override
    protected void applyInternally(ItemConstruction construction, String value) {
        final SkullMeta skullMeta = (SkullMeta) construction.getItemMeta();
        skullMeta.setOwner(value);
    }

    @Override
    public MutableSkullOwnerMeta toMutable() {
        return (MutableSkullOwnerMeta) super.toMutable();
    }

    @Override
    public ImmutableSkullOwnerMeta toImmutable() {
        return (ImmutableSkullOwnerMeta) super.toImmutable();
    }

}