package io.prodity.commons.spigot.legacy.builder.util;

import java.util.ArrayList;
import java.util.Collection;

public class SimpleBuilderList<E, SELF extends BuilderList<E, SELF>> extends ArrayList<E> implements
    BuilderList<E, SELF> {

    public SimpleBuilderList() {
        super();
    }

    public SimpleBuilderList(Collection<E> collection) {
        super(collection);
    }

}