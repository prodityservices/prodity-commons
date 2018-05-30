package io.prodity.commons.spigot.gui.scheme.mapping;

import io.prodity.commons.lazy.Lazy;
import io.prodity.commons.lazy.SimpleLazy;
import io.prodity.commons.spigot.gui.GuiItem;
import java.util.Optional;

class EmptySchemeMapping implements SchemeMapping {

    private static final Lazy<EmptySchemeMapping> INSTANCE = new SimpleLazy<>(EmptySchemeMapping::new);

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