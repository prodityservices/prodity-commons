package io.prodity.commons.spigot.legacy.item.builder.meta.enchantments;

import com.google.common.collect.ImmutableMap;
import io.prodity.commons.spigot.legacy.item.builder.meta.ImmutableItemBuilderMeta;
import java.util.Map;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.bukkit.enchantments.Enchantment;

public class ImmutableEnchantmentsMeta extends EnchantmentsMeta implements ImmutableItemBuilderMeta<Map<Enchantment, Integer>> {

    @Getter
    @Delegate(types = {MapDelegate.class})
    private final ImmutableMap<Enchantment, Integer> value;

    protected ImmutableEnchantmentsMeta(Map<Enchantment, Integer> value) {
        this.value = ImmutableMap.copyOf(value);
    }

}
