package io.prodity.commons.spigot.legacy.vector.yaml;

import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.vector.optional.MutableOptionalVector;
import java.util.Optional;

public class YamlMutableOptionalVector extends YamlOptionalVector<MutableOptionalVector> {

    public static YamlMutableOptionalVector get() {
        return YamlTypeCache.getType(YamlMutableOptionalVector.class);
    }

    public YamlMutableOptionalVector() {
        super(MutableOptionalVector.class);
    }

    @Override
    protected MutableOptionalVector createVector(Optional<Double> x, Optional<Double> y, Optional<Double> z) {
        return MutableOptionalVector.of(x, y, z);
    }

}