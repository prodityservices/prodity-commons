package io.prodity.commons.spigot.legacy.item.builder;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import io.prodity.commons.spigot.legacy.builder.MutableBuilder;
import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import io.prodity.commons.spigot.legacy.builder.meta.modifier.MetaModifier;
import io.prodity.commons.spigot.legacy.builder.meta.resolve.MetaResolver;
import io.prodity.commons.spigot.legacy.item.ItemData;
import io.prodity.commons.spigot.legacy.item.builder.construct.ItemConstruction;
import io.prodity.commons.spigot.legacy.item.builder.meta.ItemBuilderMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.ItemMetaResolver;
import java.util.function.Function;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MutableItemBuilder extends MutableBuilder<ItemStack, ItemConstruction, MutableItemBuilder> implements
    ItemBuilder<MutableItemBuilder> {

    public static MutableItemBuilder of(Material material) {
        return new MutableItemBuilder(material, 0, 0, Sets.newHashSet());
    }

    public static MutableItemBuilder of(Material material, @NonNull Iterable<ItemBuilderMeta<?>> meta) {
        return new MutableItemBuilder(material, 0, 0, meta);
    }

    public static MutableItemBuilder of(Material material, int amount) {
        return new MutableItemBuilder(material, amount, 0, Sets.newHashSet());
    }

    public static MutableItemBuilder of(Material material, int amount, int data) {
        return new MutableItemBuilder(material, amount, data, Sets.newHashSet());
    }

    public static MutableItemBuilder of(Material material, int amount, int data, @NonNull Iterable<ItemBuilderMeta<?>> meta) {
        return new MutableItemBuilder(material, amount, data, meta);
    }

    public static MutableItemBuilder of(ItemData itemData) {
        return new MutableItemBuilder(itemData.getType(), itemData.getAmount(), itemData.getData(), Sets.newHashSet());
    }

    public static MutableItemBuilder of(ItemData itemData, @NonNull Iterable<ItemBuilderMeta<?>> meta) {
        return new MutableItemBuilder(itemData.getType(), itemData.getAmount(), itemData.getData(), meta);
    }

    public static MutableItemBuilder fromItemBuilder(@NonNull ItemBuilder itemBuilder) {
        return new MutableItemBuilder(itemBuilder);
    }

    public static MutableItemBuilder fromItemStack(@NonNull ItemStack itemStack) {
        return new MutableItemBuilder(itemStack, ItemMetaResolver.getDefault());
    }

    public static MutableItemBuilder fromItemStack(@NonNull ItemStack itemStack, MetaResolver resolver) {
        return new MutableItemBuilder(itemStack, resolver);
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    private final Material material;
    @Getter
    @Setter
    @Accessors(chain = true)
    private final int amount;
    @Getter
    @Setter
    @Accessors(chain = true)
    private final int data;

    private MutableItemBuilder(@NonNull Material material, int amount, int data, @NonNull Iterable<ItemBuilderMeta<?>> metaIterable) {
        super(metaIterable);
        this.material = material;
        this.amount = amount;
        this.data = data;
    }

    private MutableItemBuilder(ItemBuilder<?> itemBuilder) {
        super(itemBuilder);
        this.material = itemBuilder.getMaterial();
        this.data = itemBuilder.getData();
        this.amount = itemBuilder.getAmount();
    }

    private MutableItemBuilder(ItemStack itemStack, MetaResolver<ItemStack, ItemConstruction> resolver) {
        super(itemStack, resolver);
        this.material = itemStack.getType();
        this.data = itemStack.getDurability();
        this.amount = itemStack.getAmount();
    }

    @Override
    protected MutableItemBuilder clone() {
        return MutableItemBuilder.fromItemBuilder(this);
    }

    @Override
    public MutableItemBuilder toMutable() {
        return new MutableItemBuilder(this);
    }

    @Override
    public ImmutableItemBuilder toImmutable() {
        return ImmutableItemBuilder.fromItemBuilder(this);
    }

    @Override
    public ItemStack construct(Function<MutableItemBuilder, ItemConstruction> constructionFunction,
        Multimap<BuilderMetaKey<?, ItemConstruction, ?, ?>, MetaModifier<?>> additionalModifiers) {
        return super.construct(constructionFunction, additionalModifiers);
    }

    @Override
    public ItemStack build() {
        return super.construct(ItemConstruction::start);
    }

}