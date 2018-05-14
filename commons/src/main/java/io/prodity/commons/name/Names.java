package io.prodity.commons.name;

import com.google.common.base.Preconditions;
import java.util.IdentityHashMap;
import java.util.Map;

public enum Names {

    ;

    private static final Map<Class<?>, String> NAMES = new IdentityHashMap<>();

    public static String getName(Class<?> type) {
        Preconditions.checkNotNull(type, "type");
        return Names.NAMES.computeIfAbsent(type, Names::computeName);
    }

    private static String computeName(Class<?> type) {
        final Name name = type.getAnnotation(Name.class);

        if (name == null) {
            return type.getSimpleName();
        }

        return name.value();
    }

}