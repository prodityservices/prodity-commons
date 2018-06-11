package io.prodity.commons.spigot.legacy.item.builder.meta.enchantments;

import io.prodity.commons.spigot.legacy.builder.util.BuilderMap;
import io.prodity.commons.spigot.legacy.builder.util.SimpleBuilderMap;
import io.prodity.commons.spigot.legacy.item.builder.meta.MutableItemBuilderMeta;
import java.util.Map;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.bukkit.enchantments.Enchantment;

public class MutableEnchantmentsMeta extends EnchantmentsMeta implements MutableItemBuilderMeta<Map<Enchantment, Integer>>,
    BuilderMap<Enchantment, Integer, MutableEnchantmentsMeta> {

    @Getter
    @Delegate(types = {MapDelegate.class, BuilderMapDelegate.class})
    private BuilderMap<Enchantment, Integer, MutableEnchantmentsMeta> value;

    protected MutableEnchantmentsMeta(Map<Enchantment, Integer> value) {
        this.value = new SimpleBuilderMap(value);
    }

    @Override
    public MutableEnchantmentsMeta setValue(Map<Enchantment, Integer> value) {
        this.value = value == null ? new SimpleBuilderMap() : new SimpleBuilderMap(value);
        return this;
    }

    protected interface BuilderMapDelegate extends BuilderMap<Enchantment, Integer, MutableEnchantmentsMeta> {

    }

}
