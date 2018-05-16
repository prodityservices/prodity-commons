package io.prodity.commons.config.annotate.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.FIELD,
    ElementType.PARAMETER
})
public @interface Required {

    class Default {

        public static final boolean DEFAULT_VALUE = true;

    }

    boolean value() default Required.Default.DEFAULT_VALUE;

}