package io.prodity.commons.spigot.legacy.item.builder.meta.lore;

import com.google.common.collect.ImmutableList;
import io.prodity.commons.spigot.legacy.item.builder.meta.ImmutableItemBuilderMeta;
import lombok.Getter;
import lombok.experimental.Delegate;

import java.util.Collection;
import java.util.List;

public class ImmutableLoreMeta extends LoreMeta implements ImmutableItemBuilderMeta<List<String>> {

    @Getter
    @Delegate(types = {ListDelegate.class})
    private final ImmutableList<String> value;

    protected ImmutableLoreMeta(Collection<String> value) {
        this.value = ImmutableList.copyOf(value);
    }

}