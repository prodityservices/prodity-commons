package io.prodity.commons.spigot.legacy.item.builder.meta.itemflags;

import io.prodity.commons.spigot.legacy.builder.util.BuilderSet;
import io.prodity.commons.spigot.legacy.builder.util.SimpleBuilderSet;
import io.prodity.commons.spigot.legacy.item.builder.meta.MutableItemBuilderMeta;
import java.util.Collection;
import java.util.Set;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.bukkit.inventory.ItemFlag;

public class MutableItemFlagsMeta extends ItemFlagsMeta implements MutableItemBuilderMeta<Set<ItemFlag>>,
    BuilderSet<ItemFlag, MutableItemFlagsMeta> {

    @Getter
    @Delegate(types = {SetDelegate.class, BuilderSetDelegate.class})
    private BuilderSet<ItemFlag, MutableItemFlagsMeta> value;

    public MutableItemFlagsMeta(Collection<ItemFlag> value) {
        this.setValueAsCollection(value);
    }

    public MutableItemFlagsMeta setValueAsCollection(Collection<ItemFlag> value) {
        this.value = value == null ? new SimpleBuilderSet<>() : new SimpleBuilderSet<>(value);
        return this;
    }

    @Override
    public MutableItemFlagsMeta setValue(Set<ItemFlag> value) {
        this.value = value == null ? new SimpleBuilderSet<>() : new SimpleBuilderSet<>(value);
        return this;
    }

    private interface BuilderSetDelegate extends BuilderSet<ItemFlag, MutableItemFlagsMeta> {

    }

}