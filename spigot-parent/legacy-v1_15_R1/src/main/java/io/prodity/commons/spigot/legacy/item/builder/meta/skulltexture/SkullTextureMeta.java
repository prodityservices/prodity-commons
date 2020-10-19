package io.prodity.commons.spigot.legacy.item.builder.meta.skulltexture;

import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import io.prodity.commons.spigot.legacy.item.ItemUtil;
import io.prodity.commons.spigot.legacy.item.builder.construct.ItemConstruction;
import io.prodity.commons.spigot.legacy.item.builder.meta.AbstractItemBuilderMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Optional;
import java.util.function.Function;

public abstract class SkullTextureMeta extends AbstractItemBuilderMeta<String> {

    public static final BuilderMetaKey<String, ItemConstruction, MutableSkullTextureMeta, ImmutableSkullTextureMeta> KEY =
        BuilderMetaKey.<String, ItemConstruction, MutableSkullTextureMeta, ImmutableSkullTextureMeta>builder()
            .mutableInstanceClass(MutableSkullTextureMeta.class)
            .immutableInstanceClass(ImmutableSkullTextureMeta.class)
            .immutableSupplier(SkullTextureMeta::immutable)
            .immutableFunction(SkullTextureMeta::immutable)
            .mutableSupplier(SkullTextureMeta::mutable)
            .mutableFunction(SkullTextureMeta::mutable)
            .build();

    public static boolean isApplicable(ItemStack itemStack) {
        return itemStack.getItemMeta() instanceof SkullMeta;
    }

    public static ImmutableSkullTextureMeta immutable() {
        return new ImmutableSkullTextureMeta(null);
    }

    public static ImmutableSkullTextureMeta immutable(String value) {
        return new ImmutableSkullTextureMeta(value);
    }

    public static ImmutableSkullTextureMeta immutable(ItemStack itemStack) {
        return (ImmutableSkullTextureMeta) SkullTextureMeta
            .fromItemStack(itemStack, SkullTextureMeta::immutable);
    }

    public static MutableSkullTextureMeta mutable() {
        return new MutableSkullTextureMeta(null);
    }

    public static MutableSkullTextureMeta mutable(String value) {
        return new MutableSkullTextureMeta(value);
    }

    public static MutableSkullTextureMeta mutable(ItemStack itemStack) {
        return (MutableSkullTextureMeta) SkullTextureMeta
            .fromItemStack(itemStack, SkullTextureMeta::mutable);
    }

    private static SkullTextureMeta fromItemStack(ItemStack itemStack, Function<String, SkullTextureMeta> instanceCreator) {
        if (!SkullTextureMeta.isApplicable(itemStack)) {
            throw new IllegalArgumentException("specified ItemStack is not applicable");
        }
        final Optional<String> texture = ItemUtil.getSkullTexture((SkullMeta) itemStack.getItemMeta());
        return instanceCreator.apply(texture.orElse(null));
    }

    protected SkullTextureMeta() {
        super(SkullTextureMeta.KEY);
    }

    @Override
    public boolean canApply(ItemConstruction construction) {
        return construction.getItemMeta() instanceof SkullMeta;
    }

    @Override
    protected void applyInternally(ItemConstruction construction, String value) {
        if (value == null) {
            return;
        }
        ItemUtil.applySkullTexture((SkullMeta) construction.getItemMeta(), value);
    }

    @Override
    public MutableSkullTextureMeta toMutable() {
        return (MutableSkullTextureMeta) super.toMutable();
    }

    @Override
    public ImmutableSkullTextureMeta toImmutable() {
        return (ImmutableSkullTextureMeta) super.toImmutable();
    }

}