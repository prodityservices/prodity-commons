package io.prodity.commons.spigot.legacy.builder.meta.resolve;

import io.prodity.commons.spigot.legacy.builder.construct.BuilderConstruction;
import io.prodity.commons.spigot.legacy.builder.meta.BuilderMeta;
import io.prodity.commons.spigot.legacy.pair.ImmutablePair;
import java.util.function.Function;
import java.util.function.Predicate;

public class ResolutionPair<T, C extends BuilderConstruction<T>> extends ImmutablePair<Predicate<T>, Function<T, BuilderMeta<?, C>>> {

    protected ResolutionPair(Predicate<T> key, Function<T, BuilderMeta<?, C>> value) {
        super(key, value);
    }

}
