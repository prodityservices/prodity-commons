package io.prodity.commons.config.annotate.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Required {

    boolean value() default Required.Default.DEFAULT_VALUE;

    class Default {

        public static final boolean DEFAULT_VALUE = true;

    }

}