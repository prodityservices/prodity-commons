package io.prodity.commons.spigot.legacy.item.builder.meta;

import io.prodity.commons.spigot.legacy.builder.meta.resolve.MetaResolver;
import io.prodity.commons.spigot.legacy.item.builder.construct.ItemConstruction;
import io.prodity.commons.spigot.legacy.item.builder.meta.armorcolor.ArmorColorMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.displayname.DisplayNameMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.dyecolor.DyeColorMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.enchantments.EnchantmentsMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.itemflags.ItemFlagsMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.lore.LoreMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.skullowner.SkullOwnerMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.skulltexture.SkullTextureMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.unbreakable.UnbreakableMeta;
import org.bukkit.inventory.ItemStack;

public class ItemMetaResolver extends MetaResolver<ItemStack, ItemConstruction> {

    private static ItemMetaResolver defaultResolver;

    public static ItemMetaResolver getDefault() {
        if (ItemMetaResolver.defaultResolver == null) {
            final ItemMetaResolver resolvers = new ItemMetaResolver();

            //ArmorColor
            resolvers.addResolver(ArmorColorMeta::isApplicable, ArmorColorMeta::immutable);

            //DisplayName
            resolvers.addResolver(DisplayNameMeta::immutable);

            //DyeColor
            resolvers.addResolver(DyeColorMeta::isApplicable, DyeColorMeta::immutable);

            //Enchantments
            resolvers.addResolver(EnchantmentsMeta::immutable);

            //ItemFlags
            resolvers.addResolver(ItemFlagsMeta::immutable);

            //Lore
            resolvers.addResolver(LoreMeta::immutable);

            //SkullTexture
            resolvers.addResolver(SkullTextureMeta::isApplicable, SkullTextureMeta::immutable);

            //SkullOwner
            resolvers.addResolver(SkullOwnerMeta::isApplicable, SkullOwnerMeta::immutable);

            //Unbreakable
            resolvers.addResolver(UnbreakableMeta::immutable);

            ItemMetaResolver.defaultResolver = resolvers;
        }
        return ItemMetaResolver.defaultResolver;
    }

}