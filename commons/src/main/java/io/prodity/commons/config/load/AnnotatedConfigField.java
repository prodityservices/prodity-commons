package io.prodity.commons.config.load;

import com.google.common.base.Preconditions;
import io.prodity.commons.config.annotate.ConfigValue;
import java.lang.reflect.Field;
import javax.annotation.Nonnull;

public class AnnotatedConfigField {

    private final ConfigValue annotation;
    private final Field field;

    public AnnotatedConfigField(@Nonnull ConfigValue annotation, @Nonnull Field field) {
        Preconditions.checkNotNull(annotation, "annotation");
        Preconditions.checkNotNull(field, "field");
        this.annotation = annotation;
        this.field = field;
    }

    @Nonnull
    public ConfigValue getAnnotation() {
        return this.annotation;
    }

    @Nonnull
    public Field getField() {
        return this.field;
    }

}