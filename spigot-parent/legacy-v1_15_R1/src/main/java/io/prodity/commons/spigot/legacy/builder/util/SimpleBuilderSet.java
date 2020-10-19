package io.prodity.commons.spigot.legacy.builder.util;

import java.util.Collection;
import java.util.HashSet;

public class SimpleBuilderSet<E, SELF extends BuilderSet<E, SELF>> extends HashSet<E> implements
    BuilderSet<E, SELF> {

    public SimpleBuilderSet() {
        super();
    }

    public SimpleBuilderSet(Collection<E> collection) {
        super(collection);
    }

}