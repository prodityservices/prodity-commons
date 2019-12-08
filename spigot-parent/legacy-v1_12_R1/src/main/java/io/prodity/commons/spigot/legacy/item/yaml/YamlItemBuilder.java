package io.prodity.commons.spigot.legacy.item.yaml;

import com.google.common.collect.Lists;
import io.prodity.commons.spigot.legacy.color.Color;
import io.prodity.commons.spigot.legacy.color.yaml.YamlImmutableColor;
import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.types.bukkit.YamlSection;
import io.prodity.commons.spigot.legacy.config.yaml.types.collection.YamlColorizedStringList;
import io.prodity.commons.spigot.legacy.config.yaml.types.enumeration.YamlEnum;
import io.prodity.commons.spigot.legacy.config.yaml.types.enumeration.YamlEnumList;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlBoolean;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlColorizedString;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlInt;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlString;
import io.prodity.commons.spigot.legacy.item.ItemData;
import io.prodity.commons.spigot.legacy.item.builder.ItemBuilder;
import io.prodity.commons.spigot.legacy.item.builder.meta.ItemBuilderMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.armorcolor.ArmorColorMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.displayname.DisplayNameMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.dyecolor.DyeColorMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.enchantments.EnchantmentsMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.itemflags.ItemFlagsMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.localizedname.LocalizedNameMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.lore.LoreMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.skullowner.SkullOwnerMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.skulltexture.SkullTextureMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.spawntype.SpawnTypeMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.unbreakable.UnbreakableMeta;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;

public abstract class YamlItemBuilder<T extends ItemBuilder> extends AbstractYamlType<T> {

    protected YamlItemBuilder(Class<T> clazz) {
        super(clazz, false);
    }

    @Override
    public T loadInternally(ConfigurationSection section, String path) throws YamlException {
        section = YamlSection.get().load(section, path);

        final Material material = YamlMaterial.get().load(section, "material");
        final int amount = YamlInt.get().loadOrDefault(section, "amount", 1);
        final int data = YamlInt.get().loadOrDefault(section, "data", 0);

        final ItemData itemData = ItemData.withDataAndAmount(material, data, amount);

        final List<ItemBuilderMeta<?>> metaList = Lists.newArrayList();

        //ArmorColor
        if (section.contains("armor-color")) {
            final Color color = YamlImmutableColor.get().load(section, "armor-color");
            metaList.add(ArmorColorMeta.immutable(color));
        }

        //DisplayName
        if (section.contains("display-name")) {
            final String name = YamlColorizedString.get().load(section, "display-name");
            metaList.add(DisplayNameMeta.immutable(name));
        }

        //DyeColor
        if (section.contains("dye-color")) {
            final DyeColor dyeColor = YamlEnum.get(DyeColor.class).load(section, "dye-color");
            metaList.add(DyeColorMeta.immutable(dyeColor));
        }

        //Enchantments
        if (section.contains("enchantments")) {
            final Map<Enchantment, Integer> enchantments = YamlEnchantMap.get().load(section, "enchantments");
            metaList.add(EnchantmentsMeta.immutable(enchantments));
        }

        //ItemFlags
        if (section.contains("item-flags")) {
            final List<ItemFlag> flags = YamlEnumList.get(ItemFlag.class).load(section, "item-flags");
            metaList.add(ItemFlagsMeta.immutable(flags));
        }

        //LocalizedName
        if (section.contains("localized-name")) {
            final String localizedName = YamlString.get().load(section, "localized-name");
            metaList.add(LocalizedNameMeta.immutable(localizedName));
        }

        //Lore
        if (section.contains("lore")) {
            final List<String> lore = YamlColorizedStringList.get().load(section, "lore");
            metaList.add(LoreMeta.immutable(lore));
        }

        //SkullTexture
        if (section.contains("skull-texture")) {
            final String texture = YamlString.get().load(section, "skull-texture");
            metaList.add(SkullTextureMeta.immutable(texture));
        }

        //SkullOwner
        if (section.contains("skull-owner")) {
            final String ownerName = YamlString.get().load(section, "skull-owner");
            metaList.add(SkullOwnerMeta.immutable(ownerName));
        }

        //SpawnType
        if (section.contains("spawn-type")) {
            final EntityType spawnType = YamlEnum.get(EntityType.class).load(section, "spawn-type");
            metaList.add(SpawnTypeMeta.immutable(spawnType));
        }

        //Unbreakable
        if (section.contains("unbreakable")) {
            final boolean unbreakable = YamlBoolean.get().load(section, "unbreakable");
            metaList.add(UnbreakableMeta.immutable(unbreakable));
        }

        return this.createBuilder(itemData, metaList);
    }

    protected abstract T createBuilder(ItemData data, Collection<ItemBuilderMeta<?>> meta);

}
