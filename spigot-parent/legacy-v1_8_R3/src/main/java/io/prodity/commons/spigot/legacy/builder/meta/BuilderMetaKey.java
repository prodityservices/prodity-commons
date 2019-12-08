package io.prodity.commons.spigot.legacy.builder.meta;

import io.prodity.commons.spigot.legacy.builder.construct.BuilderConstruction;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;

@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BuilderMetaKey<V, C extends BuilderConstruction<?>, M extends MutableBuilderMeta<V, C>, I extends ImmutableBuilderMeta<V, C>> {

    private Class<M> mutableInstanceClass;
    private Class<I> immutableInstanceClass;

    private Supplier<M> mutableSupplier;
    private Function<V, M> mutableFunction;

    private Supplier<I> immutableSupplier;
    private Function<V, I> immutableFunction;

    @Singular("conflicts")
    private Set<BuilderMetaKey<?, C, ?, ?>> conflicting;

    protected BuilderMetaKey() {
    }

    public M createMutable() {
        return this.mutableSupplier.get();
    }

    public M createMutable(V value) {
        return this.mutableFunction.apply(value);
    }

    public I createImmutable() {
        return this.immutableSupplier.get();
    }

    public I createImmutable(V value) {
        return this.immutableFunction.apply(value);
    }

    public M toMutable(BuilderMeta<V, C> meta) {
        if (this.mutableInstanceClass.isInstance(meta)) {
            return this.mutableInstanceClass.cast(meta);
        }
        return this.createMutable(meta.getValue());
    }

    public I toImmutable(BuilderMeta<V, C> meta) {
        if (this.immutableInstanceClass.isInstance(meta)) {
            return this.immutableInstanceClass.cast(meta);
        }
        return this.createImmutable(meta.getValue());
    }

}