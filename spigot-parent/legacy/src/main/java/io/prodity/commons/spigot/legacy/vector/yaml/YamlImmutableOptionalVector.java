package io.prodity.commons.spigot.legacy.vector.yaml;

import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.vector.optional.ImmutableOptionalVector;
import java.util.Optional;

public class YamlImmutableOptionalVector extends YamlOptionalVector<ImmutableOptionalVector> {

    public static YamlImmutableOptionalVector get() {
        return YamlTypeCache.getType(YamlImmutableOptionalVector.class);
    }

    public YamlImmutableOptionalVector() {
        super(ImmutableOptionalVector.class);
    }

    @Override
    protected ImmutableOptionalVector createVector(Optional<Double> x, Optional<Double> y, Optional<Double> z) {
        return ImmutableOptionalVector.of(x, y, z);
    }

}