package io.prodity.commons.spigot.legacy.item.builder;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import io.prodity.commons.spigot.legacy.builder.ImmutableBuilder;
import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import io.prodity.commons.spigot.legacy.builder.meta.modifier.MetaModifier;
import io.prodity.commons.spigot.legacy.builder.meta.resolve.MetaResolver;
import io.prodity.commons.spigot.legacy.item.ItemData;
import io.prodity.commons.spigot.legacy.item.builder.construct.ItemConstruction;
import io.prodity.commons.spigot.legacy.item.builder.meta.ItemBuilderMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.ItemMetaResolver;
import io.prodity.commons.spigot.legacy.lazy.LazyValue;
import io.prodity.commons.spigot.legacy.lazy.SimpleLazyValue;
import java.util.function.Function;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ImmutableItemBuilder extends ImmutableBuilder<ItemStack, ItemConstruction, ImmutableItemBuilder> implements
    ItemBuilder<ImmutableItemBuilder> {

    public static ImmutableItemBuilder of(Material material) {
        return new ImmutableItemBuilder(material, 0, 0, Sets.newHashSet());
    }

    public static ImmutableItemBuilder of(Material material, @NonNull Iterable<ItemBuilderMeta<?>> meta) {
        return new ImmutableItemBuilder(material, 0, 0, meta);
    }

    public static ImmutableItemBuilder of(Material material, int amount) {
        return new ImmutableItemBuilder(material, amount, 0, Sets.newHashSet());
    }

    public static ImmutableItemBuilder of(Material material, int amount, int data) {
        return new ImmutableItemBuilder(material, amount, data, Sets.newHashSet());
    }

    public static ImmutableItemBuilder of(Material material, int amount, int data, @NonNull Iterable<ItemBuilderMeta<?>> meta) {
        return new ImmutableItemBuilder(material, amount, data, meta);
    }

    public static ImmutableItemBuilder of(ItemData itemData) {
        return new ImmutableItemBuilder(itemData.getType(), itemData.getAmount(), itemData.getData(), Sets.newHashSet());
    }

    public static ImmutableItemBuilder of(ItemData itemData, @NonNull Iterable<ItemBuilderMeta<?>> meta) {
        return new ImmutableItemBuilder(itemData.getType(), itemData.getAmount(), itemData.getData(), meta);
    }

    public static ImmutableItemBuilder fromItemBuilder(ItemBuilder itemBuilder) {
        return new ImmutableItemBuilder(itemBuilder);
    }

    public static ImmutableItemBuilder fromItemStack(ItemStack itemStack) {
        return new ImmutableItemBuilder(itemStack, ItemMetaResolver.getDefault());
    }

    public static ImmutableItemBuilder fromItemStack(ItemStack itemStack, ItemMetaResolver resolver) {
        return new ImmutableItemBuilder(itemStack, resolver);
    }

    @Getter
    private final Material material;
    @Getter
    private final int amount;
    @Getter
    private final int data;
    private final LazyValue<ItemStack> cachedItem = new SimpleLazyValue<>(this::build);

    private ImmutableItemBuilder(@NonNull Material material, int amount, int data, @NonNull Iterable<ItemBuilderMeta<?>> meta) {
        super(meta);
        this.material = material;
        this.amount = amount;
        this.data = data;
    }

    private ImmutableItemBuilder(ItemBuilder<?> itemBuilder) {
        super(itemBuilder);
        this.material = itemBuilder.getMaterial();
        this.data = itemBuilder.getData();
        this.amount = itemBuilder.getAmount();
    }

    private ImmutableItemBuilder(ItemStack itemStack, MetaResolver<ItemStack, ItemConstruction> resolver) {
        super(itemStack, resolver);
        this.material = itemStack.getType();
        this.data = itemStack.getDurability();
        this.amount = itemStack.getAmount();
    }

    @Override
    public MutableItemBuilder toMutable() {
        return MutableItemBuilder.fromItemBuilder(this);
    }

    @Override
    public ImmutableItemBuilder toImmutable() {
        return this;
    }

    @Override
    protected ImmutableItemBuilder clone() {
        return ImmutableItemBuilder.fromItemBuilder(this);
    }

    @Override
    public ItemStack construct(Function<ImmutableItemBuilder, ItemConstruction> constructionFunction,
        Multimap<BuilderMetaKey<?, ItemConstruction, ?, ?>, MetaModifier<?>> additionalModifiers) {
        return super.construct(constructionFunction, additionalModifiers);
    }

    @Override
    public ItemStack build() {
        if (this.cachedItem.isInitialized()) {
            return this.cachedItem.get().clone();
        }
        final ItemStack item = construct(ItemConstruction::start);
        this.cachedItem.setAndInit(item);
        return item.clone();
    }

}