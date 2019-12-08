package io.prodity.commons.spigot.legacy.item.builder.meta.lore;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import io.prodity.commons.spigot.legacy.item.builder.construct.ItemConstruction;
import io.prodity.commons.spigot.legacy.item.builder.meta.AbstractItemBuilderMeta;
import java.util.List;
import java.util.function.Function;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class LoreMeta extends AbstractItemBuilderMeta<List<String>> implements List<String> {

    public static final BuilderMetaKey<List<String>, ItemConstruction, MutableLoreMeta, ImmutableLoreMeta> KEY =
        BuilderMetaKey.<List<String>, ItemConstruction, MutableLoreMeta, ImmutableLoreMeta>builder()
            .mutableInstanceClass(MutableLoreMeta.class)
            .immutableInstanceClass(ImmutableLoreMeta.class)
            .immutableSupplier(LoreMeta::immutable)
            .immutableFunction(LoreMeta::immutable)
            .mutableSupplier(LoreMeta::mutable)
            .mutableFunction(LoreMeta::mutable)
            .build();

    public static ImmutableLoreMeta immutable() {
        return new ImmutableLoreMeta(null);
    }

    public static ImmutableLoreMeta immutable(List<String> value) {
        return new ImmutableLoreMeta(value);
    }

    public static ImmutableLoreMeta immutable(String... lines) {
        return new ImmutableLoreMeta(ImmutableList.copyOf(lines));
    }

    public static ImmutableLoreMeta immutable(ItemStack itemStack) {
        return (ImmutableLoreMeta) LoreMeta
            .fromItemStack(itemStack, LoreMeta::immutable);
    }

    public static MutableLoreMeta mutable() {
        return new MutableLoreMeta(null);
    }

    public static MutableLoreMeta mutable(List<String> value) {
        return new MutableLoreMeta(value);
    }

    public static MutableLoreMeta mutable(ItemStack itemStack) {
        return (MutableLoreMeta) LoreMeta
            .fromItemStack(itemStack, LoreMeta::mutable);
    }

    public static ImmutableLoreMeta mutable(String... lines) {
        return new ImmutableLoreMeta(Lists.newArrayList(lines));
    }

    private static LoreMeta fromItemStack(ItemStack itemStack, Function<List<String>, LoreMeta> instanceCreator) {
        final List<String> lore = Lists.newArrayList(itemStack.getItemMeta().getLore());
        return instanceCreator.apply(lore);
    }

    public LoreMeta() {
        super(LoreMeta.KEY);
    }

    @Override
    public boolean canApply(ItemConstruction construction) {
        return true;
    }

    private List<String> colorizeList(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, ChatColor.translateAlternateColorCodes('&', list.get(i)));
        }
        return list;
    }

    @Override
    public void applyInternally(ItemConstruction construction, List<String> value) {
        final ItemMeta meta = construction.getItemMeta();
        final List<String> lore = value == null ? Lists.newArrayList() : Lists.newArrayList(value);
        this.colorizeList(lore);
        meta.setLore(lore);
    }

    @Override
    public MutableLoreMeta toMutable() {
        return (MutableLoreMeta) super.toMutable();
    }

    @Override
    public ImmutableLoreMeta toImmutable() {
        return (ImmutableLoreMeta) super.toImmutable();
    }

    protected interface ListDelegate extends List<String> {

    }

}