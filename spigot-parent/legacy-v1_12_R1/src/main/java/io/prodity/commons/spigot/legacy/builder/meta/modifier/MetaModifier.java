package io.prodity.commons.spigot.legacy.builder.meta.modifier;

import java.util.function.Function;

@FunctionalInterface
public interface MetaModifier<V> extends Function<V, V> {

}