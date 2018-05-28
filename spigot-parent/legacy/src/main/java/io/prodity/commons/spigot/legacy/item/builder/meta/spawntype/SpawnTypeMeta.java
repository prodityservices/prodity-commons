package io.prodity.commons.spigot.legacy.item.builder.meta.spawntype;

import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import io.prodity.commons.spigot.legacy.item.builder.construct.ItemConstruction;
import io.prodity.commons.spigot.legacy.item.builder.meta.AbstractItemBuilderMeta;
import java.util.function.Function;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

public abstract class SpawnTypeMeta extends AbstractItemBuilderMeta<EntityType> {

    public static final BuilderMetaKey<EntityType, ItemConstruction, MutableSpawnTypeMeta, ImmutableSpawnTypeMeta> KEY =
        BuilderMetaKey.<EntityType, ItemConstruction, MutableSpawnTypeMeta, ImmutableSpawnTypeMeta>builder()
            .mutableInstanceClass(MutableSpawnTypeMeta.class)
            .immutableInstanceClass(ImmutableSpawnTypeMeta.class)
            .immutableSupplier(SpawnTypeMeta::immutable)
            .immutableFunction(SpawnTypeMeta::immutable)
            .mutableSupplier(SpawnTypeMeta::mutable)
            .mutableFunction(SpawnTypeMeta::mutable)
            .build();

    public static boolean isApplicable(ItemStack itemStack) {
        return itemStack.getItemMeta() instanceof SpawnEggMeta;
    }

    public static ImmutableSpawnTypeMeta immutable() {
        return new ImmutableSpawnTypeMeta(null);
    }

    public static ImmutableSpawnTypeMeta immutable(EntityType value) {
        return new ImmutableSpawnTypeMeta(value);
    }

    public static ImmutableSpawnTypeMeta immutable(ItemStack itemStack) {
        return (ImmutableSpawnTypeMeta) SpawnTypeMeta
            .fromItemStack(itemStack, SpawnTypeMeta::immutable);
    }

    public static MutableSpawnTypeMeta mutable() {
        return new MutableSpawnTypeMeta(null);
    }

    public static MutableSpawnTypeMeta mutable(EntityType value) {
        return new MutableSpawnTypeMeta(value);
    }

    public static MutableSpawnTypeMeta mutable(ItemStack itemStack) {
        return (MutableSpawnTypeMeta) SpawnTypeMeta
            .fromItemStack(itemStack, SpawnTypeMeta::mutable);
    }

    private static SpawnTypeMeta fromItemStack(ItemStack itemStack, Function<EntityType, SpawnTypeMeta> instanceCreator) {
        if (!SpawnTypeMeta.isApplicable(itemStack)) {
            throw new IllegalArgumentException("specified ItemStack is not applicable");
        }
        final EntityType entityType = ((SpawnEggMeta) itemStack.getItemMeta()).getSpawnedType();
        return instanceCreator.apply(entityType);
    }

    protected SpawnTypeMeta() {
        super(SpawnTypeMeta.KEY);
    }

    @Override
    public boolean canApply(ItemConstruction construction) {
        return construction.getItemMeta() instanceof SpawnEggMeta;
    }

    @Override
    protected void applyInternally(ItemConstruction construction, EntityType value) {
        ((SpawnEggMeta) construction.getItemMeta()).setSpawnedType(value);
    }

    @Override
    public MutableSpawnTypeMeta toMutable() {
        return (MutableSpawnTypeMeta) super.toMutable();
    }

    @Override
    public ImmutableSpawnTypeMeta toImmutable() {
        return (ImmutableSpawnTypeMeta) super.toImmutable();
    }

}
