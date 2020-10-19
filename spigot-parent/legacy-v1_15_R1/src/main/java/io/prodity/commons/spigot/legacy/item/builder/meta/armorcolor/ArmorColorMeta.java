package io.prodity.commons.spigot.legacy.item.builder.meta.armorcolor;

import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import io.prodity.commons.spigot.legacy.color.Color;
import io.prodity.commons.spigot.legacy.color.Colors;
import io.prodity.commons.spigot.legacy.item.builder.construct.ItemConstruction;
import io.prodity.commons.spigot.legacy.item.builder.meta.AbstractItemBuilderMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.function.Function;

public abstract class ArmorColorMeta extends AbstractItemBuilderMeta<Color> {

    public static final BuilderMetaKey<Color, ItemConstruction, MutableArmorColorMeta, ImmutableArmorColorMeta> KEY =
        BuilderMetaKey.<Color, ItemConstruction, MutableArmorColorMeta, ImmutableArmorColorMeta>builder()
            .mutableInstanceClass(MutableArmorColorMeta.class)
            .immutableInstanceClass(ImmutableArmorColorMeta.class)
            .immutableSupplier(ArmorColorMeta::immutable)
            .immutableFunction(ArmorColorMeta::immutable)
            .mutableSupplier(ArmorColorMeta::mutable)
            .mutableFunction(ArmorColorMeta::mutable)
            .build();

    public static boolean isApplicable(ItemStack itemStack) {
        return itemStack.getItemMeta() instanceof LeatherArmorMeta;
    }

    public static ImmutableArmorColorMeta immutable() {
        return new ImmutableArmorColorMeta(null);
    }

    public static ImmutableArmorColorMeta immutable(Color value) {
        return new ImmutableArmorColorMeta(value);
    }

    public static ImmutableArmorColorMeta immutable(ItemStack itemStack) {
        return (ImmutableArmorColorMeta) ArmorColorMeta
            .fromItemStack(itemStack, ArmorColorMeta::immutable, Colors::immutableFromBukkit);
    }

    public static MutableArmorColorMeta mutable() {
        return new MutableArmorColorMeta(null);
    }

    public static MutableArmorColorMeta mutable(Color value) {
        return new MutableArmorColorMeta(value);
    }

    public static MutableArmorColorMeta mutable(ItemStack itemStack) {
        return (MutableArmorColorMeta) ArmorColorMeta
            .fromItemStack(itemStack, ArmorColorMeta::mutable, Colors::mutableFromBukkit);
    }

    private static ArmorColorMeta fromItemStack(ItemStack itemStack, Function<Color, ArmorColorMeta> instanceCreator,
        Function<org.bukkit.Color, Color> colorCreator) {
        if (!ArmorColorMeta.isApplicable(itemStack)) {
            throw new IllegalArgumentException("specified ItemStack is not applicable");
        }
        final org.bukkit.Color color = ((LeatherArmorMeta) itemStack.getItemMeta()).getColor();
        return instanceCreator.apply(color == null ? null : colorCreator.apply(color));
    }

    protected ArmorColorMeta() {
        super(ArmorColorMeta.KEY);
    }

    @Override
    public boolean canApply(ItemConstruction construction) {
        return construction.getItemMeta() instanceof LeatherArmorMeta;
    }

    @Override
    protected void applyInternally(ItemConstruction construction, Color value) {
        final org.bukkit.Color leatherColor = value == null ? null : value.toBukkitColor();
        ((LeatherArmorMeta) construction.getItemMeta()).setColor(leatherColor);
    }

    @Override
    public MutableArmorColorMeta toMutable() {
        return (MutableArmorColorMeta) super.toMutable();
    }

    @Override
    public ImmutableArmorColorMeta toImmutable() {
        return (ImmutableArmorColorMeta) super.toImmutable();
    }

}