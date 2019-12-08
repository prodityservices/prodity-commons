package io.prodity.commons.spigot.legacy.item.builder.meta.itemflags;

import com.google.common.collect.Sets;
import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import io.prodity.commons.spigot.legacy.item.builder.construct.ItemConstruction;
import io.prodity.commons.spigot.legacy.item.builder.meta.AbstractItemBuilderMeta;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class ItemFlagsMeta extends AbstractItemBuilderMeta<Set<ItemFlag>> implements Set<ItemFlag> {

    public static final BuilderMetaKey<Set<ItemFlag>, ItemConstruction, MutableItemFlagsMeta, ImmutableItemFlagsMeta> KEY =
        BuilderMetaKey.<Set<ItemFlag>, ItemConstruction, MutableItemFlagsMeta, ImmutableItemFlagsMeta>builder()
            .mutableInstanceClass(MutableItemFlagsMeta.class)
            .immutableInstanceClass(ImmutableItemFlagsMeta.class)
            .immutableSupplier(ItemFlagsMeta::immutable)
            .immutableFunction(ItemFlagsMeta::immutable)
            .mutableSupplier(ItemFlagsMeta::mutable)
            .mutableFunction(ItemFlagsMeta::mutable)
            .build();

    public static ImmutableItemFlagsMeta immutable() {
        return new ImmutableItemFlagsMeta(null);
    }

    public static ImmutableItemFlagsMeta immutable(Collection<ItemFlag> value) {
        return new ImmutableItemFlagsMeta(value);
    }

    public static ImmutableItemFlagsMeta immutable(ItemStack itemStack) {
        return (ImmutableItemFlagsMeta) ItemFlagsMeta
            .fromItemStack(itemStack, ItemFlagsMeta::immutable);
    }

    public static MutableItemFlagsMeta mutable() {
        return new MutableItemFlagsMeta(null);
    }

    public static MutableItemFlagsMeta mutable(Collection<ItemFlag> value) {
        return new MutableItemFlagsMeta(value);
    }

    public static MutableItemFlagsMeta mutable(ItemStack itemStack) {
        return (MutableItemFlagsMeta) ItemFlagsMeta
            .fromItemStack(itemStack, ItemFlagsMeta::mutable);
    }

    private static ItemFlagsMeta fromItemStack(ItemStack itemStack, Function<Set<ItemFlag>, ItemFlagsMeta> instanceCreator) {
        final Set<ItemFlag> flags = Sets.newHashSet(itemStack.getItemMeta().getItemFlags());
        return instanceCreator.apply(flags);
    }

    public ItemFlagsMeta() {
        super(ItemFlagsMeta.KEY);
    }

    @Override
    public boolean canApply(ItemConstruction construction) {
        return true;
    }

    @Override
    public void applyInternally(ItemConstruction construction, Set<ItemFlag> value) {
        final ItemMeta meta = construction.getItemMeta();
        Stream.of(ItemFlag.values()).forEach(meta::removeItemFlags);
        if (value == null) {
            return;
        }
        value.forEach(meta::addItemFlags);
    }

    @Override
    public MutableItemFlagsMeta toMutable() {
        return (MutableItemFlagsMeta) super.toMutable();
    }

    @Override
    public ImmutableItemFlagsMeta toImmutable() {
        return (ImmutableItemFlagsMeta) super.toImmutable();
    }

    protected interface SetDelegate extends Set<ItemFlag> {

    }

}