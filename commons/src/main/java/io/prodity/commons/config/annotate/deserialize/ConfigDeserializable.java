package io.prodity.commons.config.annotate.deserialize;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker interface for {@link java.lang.reflect.Type}s that have a no argument constructor and can be deserialized automaticaly.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigDeserializable {

}