package io.prodity.commons.config.load.element;

import com.google.common.base.Preconditions;
import io.prodity.commons.reflect.NamedAnnotatedElement;
import java.lang.reflect.Field;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.reflect.FieldUtils;

public class ConfigFieldElement extends ConfigElement {

    public ConfigFieldElement(@Nonnull Field field) {
        super(NamedAnnotatedElement.fromField(field));
    }

    @Nonnull
    public Field getField() {
        return (Field) this.getElement().getHandle();
    }

    public void set(@Nonnull Object object, Object value) throws IllegalAccessException {
        Preconditions.checkNotNull(object, "object");

        final Field field = this.getField();

        FieldUtils.removeFinalModifier(field, true);
        field.setAccessible(true);
        field.set(object, value);
    }

}