package io.prodity.commons.config.inject.element.attribute;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.util.Optional;
import javax.annotation.Nullable;

public class ElementAttributeValue<V> {

    private final ElementAttribute<V> attribute;
    private final V value;

    public ElementAttributeValue(ElementAttribute<V> attribute, @Nullable V value) {
        Preconditions.checkNotNull(attribute, "attribute");
        this.attribute = attribute;
        this.value = value;
    }

    public ElementAttribute<V> getAttribute() {
        return this.attribute;
    }

    public ElementAttributeKey<V> getAttributeKey() {
        return this.attribute.getKey();
    }

    public boolean hasValue() {
        return this.value != null;
    }

    public Optional<V> getValue() {
        return Optional.ofNullable(this.value);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("attribute", this.attribute)
            .add("value", this.value)
            .toString();
    }
    
}