package config.example;

import io.prodity.commons.color.ImmutableColor;
import io.prodity.commons.config.annotate.deserialize.ConfigDeserializable;
import io.prodity.commons.config.annotate.inject.ConfigInject;
import io.prodity.commons.config.annotate.inject.ConfigNodeKey;
import io.prodity.commons.config.annotate.inject.Required;
import io.prodity.commons.identity.Identifiable;

// Since we want this class to be automatically deserialized from configurations, it has to be annotated with @ConfigDeserializable
// Therefore it will require no special handling when being deserialized.
@ConfigDeserializable
public class IdentifiableColor extends ImmutableColor implements Identifiable<String> {

    // @ConfigNodeKey is an alternative to @ConfigPath, and will only work in a type annotated @ConfigDeserializable
    //   - (In this example it is placed on the parameter to be assigned to this field)
    // It declares that the annotated element is to have the key of the ConfigurationNode as the injected value,
    // with the ConfigurationNode being the node used when injecting an object annotated with @ConfigDeserializable
    //
    // The primary use case is for values of a Repository, as the ID is required for the Identifiable interface.
    private final String id;

    //For the "red, green, blue" parameters, since no path is specified their parameter names are used as the path.
    @ConfigInject
    public IdentifiableColor(@ConfigNodeKey String id, @Required int red, @Required int green, @Required int blue) {
        super(red, green, blue);
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

}