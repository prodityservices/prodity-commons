package io.prodity.commons.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Qualifier;
import org.glassfish.hk2.api.Metadata;

/**
 * Indicates that this service should only be loaded if a plugin exists
 * for every name in {@link #value()}.  The service itself has a hard
 * dependency on this plugins, but byWithType failing to load the service and
 * having alternative implementations of the contract, the plugin can
 * maintain a soft dependency.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface SoftDepend {

    @Metadata("SoftDepend")
    String value();

}