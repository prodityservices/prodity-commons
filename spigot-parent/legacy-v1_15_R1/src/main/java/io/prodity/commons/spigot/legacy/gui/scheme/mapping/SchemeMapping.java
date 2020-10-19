package io.prodity.commons.spigot.legacy.gui.scheme.mapping;

import io.prodity.commons.spigot.legacy.gui.GuiItem;

import java.util.Optional;

public interface SchemeMapping extends Cloneable {

    static SchemeMapping empty() {
        return EmptySchemeMapping.getInstance();
    }

    Optional<GuiItem> get(int key);

    boolean hasMappingFor(int key);

    SchemeMapping clone();

}