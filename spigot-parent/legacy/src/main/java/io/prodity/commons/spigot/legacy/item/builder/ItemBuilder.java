package io.prodity.commons.spigot.legacy.item.builder;

import com.google.common.collect.Multimap;
import io.prodity.commons.spigot.legacy.builder.Builder;
import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import io.prodity.commons.spigot.legacy.builder.meta.modifier.MetaModifier;
import io.prodity.commons.spigot.legacy.builder.meta.modifier.MetaModifierMap;
import io.prodity.commons.spigot.legacy.item.builder.construct.ItemConstruction;
import io.prodity.commons.spigot.legacy.item.builder.meta.displayname.DisplayNameMeta;
import io.prodity.commons.spigot.legacy.item.builder.meta.lore.LoreMeta;
import io.prodity.commons.spigot.legacy.message.replace.Replacer;
import io.prodity.commons.spigot.legacy.placeholder.PlaceholderHelper;
import java.util.List;
import java.util.function.Function;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ItemBuilder<SELF extends ItemBuilder<SELF>> extends Builder<ItemStack, ItemConstruction, SELF> {

    static MetaModifier<String> nameModifier(Player player, Replacer replacer) {
        return (string) -> replacer.replace(player, string);
    }

    static MetaModifier<String> nameModifier(Replacer replacer) {
        return (string) -> replacer.replace(string);
    }

    static MetaModifier<String> nameModifierPapi(Player player) {
        return (string) -> string == null ? null : PlaceholderHelper.setPlaceholders(player, string);
    }

    static MetaModifier<List<String>> loreModifierPapi(Player player) {
        return (list) -> list == null ? null : PlaceholderHelper.setPlaceholders(player, list);
    }

    static MetaModifier<List<String>> loreModifier(Player player, Replacer replacer) {
        return (list) -> replacer.replaceList(player, list);
    }

    static MetaModifier<List<String>> loreModifier(Replacer replacer) {
        return (list) -> replacer.replaceList(list);
    }

    Material getMaterial();

    int getData();

    int getAmount();

    /**
     * for internal use only
     */
    @Deprecated
    ItemStack construct(Function<SELF, ItemConstruction> constructionFunction,
        Multimap<BuilderMetaKey<?, ItemConstruction, ?, ?>, MetaModifier<?>> additionalModifiers);

    default ItemStack papiBuild(Player player) {
        return this.construct((builder) -> ItemConstruction.start(builder), MetaModifierMap.<ItemConstruction>create()
            .builderPut(DisplayNameMeta.KEY, ItemBuilder.nameModifierPapi(player))
            .builderPut(LoreMeta.KEY, ItemBuilder.loreModifierPapi(player)));
    }

    default ItemStack papiBuild(Player player, Replacer replacer) {
        final MetaModifierMap<ItemConstruction> metaModifiers = MetaModifierMap.<ItemConstruction>create()
            .builderPut(DisplayNameMeta.KEY, ItemBuilder.nameModifier(player, replacer))
            .builderPut(LoreMeta.KEY, ItemBuilder.loreModifier(player, replacer));

        if (replacer.isPapi()) {
            metaModifiers
                .builderPut(DisplayNameMeta.KEY, ItemBuilder.nameModifierPapi(player))
                .builderPut(LoreMeta.KEY, ItemBuilder.loreModifierPapi(player));
        }

        return this.construct((builder) -> ItemConstruction.start(builder), metaModifiers);
    }

    default ItemStack build(Replacer replacer) {
        return this.construct((builder) -> ItemConstruction.start(builder), MetaModifierMap.<ItemConstruction>create()
            .builderPut(DisplayNameMeta.KEY, ItemBuilder.nameModifier(replacer))
            .builderPut(LoreMeta.KEY, ItemBuilder.loreModifier(replacer)));
    }

    default ItemStack build(Player player, Replacer replacer) {
        return this.construct((builder) -> ItemConstruction.start(builder), MetaModifierMap.<ItemConstruction>create()
            .builderPut(DisplayNameMeta.KEY, ItemBuilder.nameModifier(player, replacer))
            .builderPut(LoreMeta.KEY, ItemBuilder.loreModifier(player, replacer)));
    }

    @Override
    ImmutableItemBuilder toImmutable();

    @Override
    MutableItemBuilder toMutable();

}