package io.prodity.commons.spigot.legacy.item.builder.meta.itemflags;

import com.google.common.collect.ImmutableSet;
import io.prodity.commons.spigot.legacy.item.builder.meta.ImmutableItemBuilderMeta;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.bukkit.inventory.ItemFlag;

import java.util.Collection;
import java.util.Set;

public class ImmutableItemFlagsMeta extends ItemFlagsMeta implements ImmutableItemBuilderMeta<Set<ItemFlag>> {

    @Getter
    @Delegate(types = {SetDelegate.class})
    private final ImmutableSet<ItemFlag> value;

    protected ImmutableItemFlagsMeta(Collection<ItemFlag> value) {
        this.value = ImmutableSet.copyOf(value);
    }

}