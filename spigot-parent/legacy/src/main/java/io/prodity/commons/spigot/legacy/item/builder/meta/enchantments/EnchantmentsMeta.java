package io.prodity.commons.spigot.legacy.item.builder.meta.enchantments;

import com.google.common.collect.Maps;
import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import io.prodity.commons.spigot.legacy.item.builder.construct.ItemConstruction;
import io.prodity.commons.spigot.legacy.item.builder.meta.AbstractItemBuilderMeta;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class EnchantmentsMeta extends AbstractItemBuilderMeta<Map<Enchantment, Integer>> implements Map<Enchantment, Integer> {

    public static final BuilderMetaKey<Map<Enchantment, Integer>, ItemConstruction, MutableEnchantmentsMeta, ImmutableEnchantmentsMeta> KEY =
        BuilderMetaKey.<Map<Enchantment, Integer>, ItemConstruction, MutableEnchantmentsMeta, ImmutableEnchantmentsMeta>builder()
            .mutableInstanceClass(MutableEnchantmentsMeta.class)
            .immutableInstanceClass(ImmutableEnchantmentsMeta.class)
            .immutableSupplier(EnchantmentsMeta::immutable)
            .immutableFunction(EnchantmentsMeta::immutable)
            .mutableSupplier(EnchantmentsMeta::mutable)
            .mutableFunction(EnchantmentsMeta::mutable)
            .build();

    public static ImmutableEnchantmentsMeta immutable() {
        return new ImmutableEnchantmentsMeta(null);
    }

    public static ImmutableEnchantmentsMeta immutable(Map<Enchantment, Integer> value) {
        return new ImmutableEnchantmentsMeta(value);
    }

    public static ImmutableEnchantmentsMeta immutable(ItemStack itemStack) {
        return (ImmutableEnchantmentsMeta) EnchantmentsMeta
            .fromItemStack(itemStack, EnchantmentsMeta::immutable);
    }

    public static MutableEnchantmentsMeta mutable() {
        return new MutableEnchantmentsMeta(null);
    }

    public static MutableEnchantmentsMeta mutable(Map<Enchantment, Integer> value) {
        return new MutableEnchantmentsMeta(value);
    }

    public static MutableEnchantmentsMeta mutable(ItemStack itemStack) {
        return (MutableEnchantmentsMeta) EnchantmentsMeta
            .fromItemStack(itemStack, EnchantmentsMeta::mutable);
    }

    private static EnchantmentsMeta fromItemStack(ItemStack itemStack,
        Function<Map<Enchantment, Integer>, EnchantmentsMeta> instanceCreator) {
        return instanceCreator.apply(Maps.newHashMap(itemStack.getEnchantments()));
    }

    protected EnchantmentsMeta() {
        super(EnchantmentsMeta.KEY);
    }

    @Override
    public boolean canApply(ItemConstruction construction) {
        return true;
    }

    @Override
    protected void applyInternally(ItemConstruction construction, Map<Enchantment, Integer> value) {
        final ItemMeta meta = construction.getItemMeta();
        Stream.of(Enchantment.values()).forEach(meta::removeEnchant);
        if (value == null) {
            return;
        }
        value.forEach((enchantment, level) -> {
            if (level == null) {
                return;
            }
            meta.addEnchant(enchantment, level, true);
        });
    }

    @Override
    public MutableEnchantmentsMeta toMutable() {
        return (MutableEnchantmentsMeta) super.toMutable();
    }

    @Override
    public ImmutableEnchantmentsMeta toImmutable() {
        return (ImmutableEnchantmentsMeta) super.toImmutable();
    }

    protected interface MapDelegate extends Map<Enchantment, Integer> {

    }

}