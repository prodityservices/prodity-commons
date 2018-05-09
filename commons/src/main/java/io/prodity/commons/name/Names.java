package io.prodity.commons.name;

import com.google.common.base.Preconditions;
import java.util.IdentityHashMap;
import java.util.Map;
import javax.annotation.Nonnull;

public enum Names {

    ;

    private static final Map<Class<?>, String> NAMES = new IdentityHashMap<>();

    @Nonnull
    public static String getName(@Nonnull Class<?> type) {
        Preconditions.checkNotNull(type, "type");
        return Names.NAMES.computeIfAbsent(type, Names::computeName);
    }

    @Nonnull
    private static String computeName(@Nonnull Class<?> type) {
        final Name name = type.getAnnotation(Name.class);

        if (name == null) {
            return type.getSimpleName();
        }

        return name.value();
    }

}