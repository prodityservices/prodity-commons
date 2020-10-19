package io.prodity.commons.spigot.legacy.gui.scheme.mapping;

import io.prodity.commons.spigot.legacy.gui.GuiItem;
import io.prodity.commons.spigot.legacy.lazy.LazyValue;
import io.prodity.commons.spigot.legacy.lazy.SimpleLazyValue;

import java.util.Optional;

class EmptySchemeMapping implements SchemeMapping {

    private static final LazyValue<EmptySchemeMapping> INSTANCE = new SimpleLazyValue<>(EmptySchemeMapping::new);

    protected static EmptySchemeMapping getInstance() {
        return EmptySchemeMapping.INSTANCE.get();
    }

    private EmptySchemeMapping() {

    }

    @Override
    public Optional<GuiItem> get(int key) {
        return Optional.empty();
    }

    @Override
    public boolean hasMappingFor(int key) {
        return false;
    }

    @Override
    public SchemeMapping clone() {
        return this;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof EmptySchemeMapping; // Treat as singleton
    }

}