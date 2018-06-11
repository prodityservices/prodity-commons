package io.prodity.commons.spigot.legacy.gui.scheme.mapping;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import io.prodity.commons.spigot.legacy.gui.GuiItem;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class SimpleSchemeMapping implements SchemeMapping {

    public static SchemeMapping of(Map<Integer, GuiItem> mapping) {
        return new SimpleSchemeMapping(mapping);
    }

    private final ImmutableMap<Integer, GuiItem> mapping;

    private SimpleSchemeMapping(Map<Integer, GuiItem> mapping) {
        Preconditions.checkNotNull(mapping, "mapping");
        this.mapping = ImmutableMap.copyOf(mapping);
    }

    @Override
    public Optional<GuiItem> get(int key) {
        return Optional.ofNullable(this.mapping.get(key));
    }

    @Override
    public boolean hasMappingFor(int key) {
        return this.mapping.containsKey(key);
    }

    @Override
    public SimpleSchemeMapping clone() {
        return new SimpleSchemeMapping(this.mapping);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof SimpleSchemeMapping)) {
            return false;
        }
        final SimpleSchemeMapping that = (SimpleSchemeMapping) object;
        return Objects.equals(this.mapping, that.mapping);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.mapping);
    }

}