package config.example;

import io.prodity.commons.color.ImmutableColor;
import io.prodity.commons.config.annotate.deserialize.ConfigDeserializable;
import io.prodity.commons.config.annotate.inject.ConfigInject;
import io.prodity.commons.config.annotate.inject.Required;
import io.prodity.commons.identity.Identifiable;

// Since we want this class to be automatically deserialized from configurations, it has to be annotated with @ConfigDeserializable
// Therefore it will require no special handling when being deserialized.
@ConfigDeserializable
public class IdentifiableColor extends ImmutableColor implements Identifiable<String> {

    private String id;

    @ConfigInject
    public IdentifiableColor(@Required int red, @Required int green, @Required int blue) {
        super(red, green, blue);
    }

    @Override
    public String getId() {
        return null;
    }

}