package io.prodity.commons.spigot.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Qualifier;
import org.glassfish.hk2.api.Metadata;

/**
 * Indicates that the annotated service should only be loaded
 * if the internal NMS version is contained within {@link #value()}.
 * Example: {@code @McVersion("1_8_R1")}.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface McVersion {

    @Metadata("McVersion")
    String value();
}
