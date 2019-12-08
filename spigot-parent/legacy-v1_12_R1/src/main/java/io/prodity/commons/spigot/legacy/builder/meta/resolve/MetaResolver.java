package io.prodity.commons.spigot.legacy.builder.meta.resolve;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.prodity.commons.spigot.legacy.builder.construct.BuilderConstruction;
import io.prodity.commons.spigot.legacy.builder.meta.BuilderMeta;
import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.NonNull;

public class MetaResolver<T, C extends BuilderConstruction<T>> {

    @Getter
    private final List<ResolutionPair<T, C>> resolvers;

    public MetaResolver() {
        this.resolvers = Lists.newArrayList();
    }

    public MetaResolver(@NonNull MetaResolver inheritFrom) {
        this.resolvers = Lists.newArrayList(inheritFrom.resolvers);
    }

    public MetaResolver addResolver(@NonNull Function<T, BuilderMeta<?, C>> resolver) {
        this.addResolver(x -> true, resolver);
        return this;
    }

    public MetaResolver addResolver(@NonNull Predicate<T> predicate, @NonNull Function<T, BuilderMeta<?, C>> resolver) {
        final ResolutionPair pair = new ResolutionPair(predicate, resolver);
        this.resolvers.add(pair);
        return this;
    }

    public Map<BuilderMetaKey<?, C, ?, ?>, BuilderMeta<?, C>> resolve(@NonNull T object) {

        final Map<BuilderMetaKey<?, C, ?, ?>, BuilderMeta<?, C>> map = Maps.newHashMap();

        this.resolvers.stream()
            .filter(resolver -> resolver.getKey().test(object))
            .map(resolver -> resolver.getValue().apply(object))
            .filter(Objects::nonNull)
            .forEach(resolved -> map.put(resolved.getKey(), resolved));

        return map;
    }

    @Override
    public MetaResolver clone() {
        return new MetaResolver(this);
    }

}