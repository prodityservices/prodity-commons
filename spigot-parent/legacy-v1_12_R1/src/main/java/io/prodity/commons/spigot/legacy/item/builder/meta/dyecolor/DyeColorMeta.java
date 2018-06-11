package io.prodity.commons.spigot.legacy.item.builder.meta.dyecolor;

import com.google.common.collect.ImmutableMap;
import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import io.prodity.commons.spigot.legacy.item.builder.construct.ItemConstruction;
import io.prodity.commons.spigot.legacy.item.builder.meta.AbstractItemBuilderMeta;
import io.prodity.commons.spigot.legacy.pair.ImmutablePair;
import io.prodity.commons.spigot.legacy.pair.Pair;
import java.util.function.Function;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class DyeColorMeta extends AbstractItemBuilderMeta<DyeColor> {

    public static final BuilderMetaKey<DyeColor, ItemConstruction, MutableDyeColorMeta, ImmutableDyeColorMeta> KEY =
        BuilderMetaKey.<DyeColor, ItemConstruction, MutableDyeColorMeta, ImmutableDyeColorMeta>builder()
            .mutableInstanceClass(MutableDyeColorMeta.class)
            .immutableInstanceClass(ImmutableDyeColorMeta.class)
            .immutableSupplier(DyeColorMeta::immutable)
            .immutableFunction(DyeColorMeta::immutable)
            .mutableSupplier(DyeColorMeta::mutable)
            .mutableFunction(DyeColorMeta::mutable)
            .build();

    public static final ImmutableMap<Material, Pair<Function<DyeColor, Byte>, Function<Byte, DyeColor>>> TYPES =
        ImmutableMap.<Material, Pair<Function<DyeColor, Byte>, Function<Byte, DyeColor>>>builder()
            //Wool
            .put(Material.WOOL, ImmutablePair.create(DyeColor::getWoolData, DyeColor::getByWoolData))
            .put(Material.STAINED_CLAY, ImmutablePair.create(DyeColor::getWoolData, DyeColor::getByWoolData))
            .put(Material.STAINED_GLASS, ImmutablePair.create(DyeColor::getWoolData, DyeColor::getByWoolData))
            .put(Material.STAINED_GLASS_PANE, ImmutablePair.create(DyeColor::getWoolData, DyeColor::getByWoolData))
            .put(Material.CONCRETE, ImmutablePair.create(DyeColor::getWoolData, DyeColor::getByWoolData))
            .put(Material.CONCRETE_POWDER, ImmutablePair.create(DyeColor::getWoolData, DyeColor::getByWoolData))
            .put(Material.BED, ImmutablePair.create(DyeColor::getWoolData, DyeColor::getByWoolData))
            //Dye
            .put(Material.INK_SACK, ImmutablePair.create(DyeColor::getDyeData, DyeColor::getByDyeData))
            .put(Material.BANNER, ImmutablePair.create(DyeColor::getDyeData, DyeColor::getByDyeData))
            .build();

    public static boolean isApplicable(ItemStack itemStack) {
        return DyeColorMeta.TYPES.containsKey(itemStack.getType());
    }

    public static ImmutableDyeColorMeta immutable() {
        return new ImmutableDyeColorMeta(null);
    }

    public static ImmutableDyeColorMeta immutable(DyeColor value) {
        return new ImmutableDyeColorMeta(value);
    }

    public static ImmutableDyeColorMeta immutable(ItemStack itemStack) {
        return (ImmutableDyeColorMeta) DyeColorMeta
            .fromItemStack(itemStack, DyeColorMeta::immutable);
    }

    public static MutableDyeColorMeta mutable() {
        return new MutableDyeColorMeta(null);
    }

    public static MutableDyeColorMeta mutable(DyeColor value) {
        return new MutableDyeColorMeta(value);
    }

    public static MutableDyeColorMeta mutable(ItemStack itemStack) {
        return (MutableDyeColorMeta) DyeColorMeta
            .fromItemStack(itemStack, DyeColorMeta::mutable);
    }

    private static DyeColorMeta fromItemStack(ItemStack itemStack, Function<DyeColor, DyeColorMeta> instanceCreator) {
        if (!DyeColorMeta.isApplicable(itemStack)) {
            throw new IllegalArgumentException("specified ItemStack is not applicable");
        }
        final byte data = new Short(itemStack.getDurability()).byteValue();
        final DyeColor color = DyeColorMeta.TYPES.get(itemStack.getType()).getValue().apply(data);
        return instanceCreator.apply(color);
    }

    protected DyeColorMeta() {
        super(DyeColorMeta.KEY);
    }

    @Override
    public boolean canApply(ItemConstruction construction) {
        final Material type = construction.getItemData().getType();
        return DyeColorMeta.TYPES.containsKey(type);
    }

    @Override
    protected void applyInternally(ItemConstruction construction, DyeColor value) {
        final Material type = construction.getItemData().getType();
        final byte data = value == null ? (byte) 0 : DyeColorMeta.TYPES.get(type).getKey().apply(value);
        construction.getItemData().setData((int) data);
    }

    @Override
    public MutableDyeColorMeta toMutable() {
        return (MutableDyeColorMeta) super.toMutable();
    }

    @Override
    public ImmutableDyeColorMeta toImmutable() {
        return (ImmutableDyeColorMeta) super.toImmutable();
    }

}