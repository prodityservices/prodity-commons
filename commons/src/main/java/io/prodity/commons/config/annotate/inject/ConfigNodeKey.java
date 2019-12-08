package io.prodity.commons.config.annotate.inject;

import io.prodity.commons.config.annotate.deserialize.ConfigDeserializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ninja.leaping.configurate.ConfigurationNode;

/**
 * Can be declared on elements in {@link ConfigDeserializable} objects instead of {@link ConfigPath}.<br>
 * Results in the injected value resulting in the key of the {@link ConfigurationNode} used in deserializing the object.
 */
@Target({
    ElementType.FIELD,
    ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigNodeKey {
}