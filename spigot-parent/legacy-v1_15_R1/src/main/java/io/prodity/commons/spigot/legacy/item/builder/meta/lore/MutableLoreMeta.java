package io.prodity.commons.spigot.legacy.item.builder.meta.lore;

import io.prodity.commons.spigot.legacy.builder.util.BuilderList;
import io.prodity.commons.spigot.legacy.builder.util.SimpleBuilderList;
import io.prodity.commons.spigot.legacy.item.builder.meta.MutableItemBuilderMeta;
import lombok.Getter;
import lombok.experimental.Delegate;

import java.util.Collection;
import java.util.List;

public class MutableLoreMeta extends LoreMeta implements MutableItemBuilderMeta<List<String>>, BuilderList<String, MutableLoreMeta> {

    @Getter
    @Delegate(types = {ListDelegate.class, BuilderListDelegate.class})
    private BuilderList<String, MutableLoreMeta> value;

    public MutableLoreMeta(Collection<String> value) {
        this.setValueAsCollection(value);
    }

    public MutableLoreMeta setValueAsCollection(Collection<String> value) {
        this.value = value == null ? new SimpleBuilderList<>() : new SimpleBuilderList<>(value);
        return this;
    }

    @Override
    public MutableLoreMeta setValue(List<String> value) {
        this.value = value == null ? new SimpleBuilderList<>() : new SimpleBuilderList<>(value);
        return this;
    }

    private interface BuilderListDelegate extends BuilderList<String, MutableLoreMeta> {

    }

}