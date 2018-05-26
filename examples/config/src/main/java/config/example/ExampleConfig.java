package config.example;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import config.example.HandledConfigObject.ImmutableHandledConfigObject;
import config.example.HandledConfigObject.MutableHandledConfigObject;
import io.prodity.commons.color.Color;
import io.prodity.commons.config.annotate.deserialize.Colorize;
import io.prodity.commons.config.annotate.deserialize.ConfigDefault;
import io.prodity.commons.config.annotate.deserialize.LoadFromRepository;
import io.prodity.commons.config.annotate.inject.Config;
import io.prodity.commons.config.annotate.inject.ConfigInject;
import io.prodity.commons.config.annotate.inject.ConfigPath;
import io.prodity.commons.config.annotate.inject.Required;
import io.prodity.commons.config.annotate.listen.PostConfigInject;
import io.prodity.commons.config.annotate.listen.PreConfigInject;
import io.prodity.commons.config.inject.ConfigInjectionContext;
import io.prodity.commons.config.inject.deserialize.ElementDeserializers;
import io.prodity.commons.config.inject.deserialize.registry.ElementDeserializerRegistry;
import io.prodity.commons.spigot.inject.TimeUnit;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

// @Config declares that this class can be injected by a ConfigInjector.
// - fileName is the name of the file.
// - internalPath can prefix the path of the file in the jar if you wish for it to be in a specific directory.
// - fileDirectory can prefix the path of the file when it is created in the plugin's directory
//
// Classes annotated with @Config are automatically loaded/injected when they are required as a dependency in another class.
//
// Finally *never* annotate a config with @Service as that will prevent it from being properly injected.
@Config(fileName = "config.yml")
@Singleton
public class ExampleConfig {

    private static final class BroadcastUnitDefault implements Supplier<TimeUnit> {

        @Override
        public TimeUnit get() {
            return TimeUnit.SECONDS;
        }

    }

    private static final class BroadcastDurationDefault implements Supplier<Long> {

        @Override
        public Long get() {
            return 180L;
        }

    }

    // A custom constructor can be specified that allows the injection of config values as parameters.
    // The constructor must be annotated with @ConfigInject, and only 1 constructor per class can have this annotation.
    //
    // The only pitfall of this is that HK2 dependency injection will not work as that is done after the instance is created.
    // This will also be instantiated prior to @PreConfigInject methods being invoked.
    //
    // For this example I will leave this constructor empty, which has the same behavior as not defining a constructor.
    @ConfigInject
    private ExampleConfig() {

    }

    @Inject
    private Logger logger;

    // @ConfigInject should be applied to all fields that are to be injected by the config injector, that
    // do not have @ConfigPath present. @ConfigPath automatically assumes the field is to be injected.
    //
    // @ConfigPath specifies the path of the field/parameter in the config file
    //   - When no annotation is specified, the field name is used
    //   - Be sure to add the -parameters compiler argument to retain parameter names for the above case.
    //
    // @LoadFromRepository will load the element(s) by their IDs from the specified Repository class.
    // A name for the Repository can also be specified, but is optional.
    //   - Therefore, the IDs of the element(s) are specified in the config, and the values are retrieved from the
    //     repository by their configured IDS
    //   - The IDs can only be defined in the config as a single node or list format
    //   - The loaded values can then be applied to a single Object, List, or Map
    //
    // @Required specifies that the value is 100% required to be present in the config.yml. If it is not
    // present an exception will be thrown.
    //
    // When @Required is used with @LoadFromRepository, if any of the keys are not present in the Repository, an exception will be thrown.
    //
    // In this example, Color is an interface so it can not be directly injected without special handling.
    // Since Color is in commons, the default ElementDeserializerRegistry maps Color out to one of it's implementations
    // that is ImmutableColor. More information on how you can map your own interface types to their implementations can be seen below.
    //
    // As for collections, Set, List, ImmutableSet, ImmutableList, Arrays, & a few others have support built in by default.
    @ConfigPath("warm-colors")
    @LoadFromRepository(ColorRepository.class)
    @Required
    private ImmutableList<Color> warmColors;

    // If using a Map type when using @LoadFromRepository, the keys will be the IDs of the values loaded from the Repository.
    @ConfigPath("warm-colors")
    @LoadFromRepository(ColorRepository.class)
    @Required
    private ImmutableMap<String, Color> warmColorsAsMap;

    @ConfigPath("main-color")
    @LoadFromRepository(ColorRepository.class)
    @Required
    private Color mainColor;

    // @Colorize specifies that elements in the following cases should be colorized
    // 1) String types
    // 2) String types that are elements of lists, sets, & their guava immutable implementations
    // 3) String types that are values of maps & their guava immutable implementations
    @ConfigPath("messages")
    @Colorize
    @Required
    private ImmutableMap<String, String> messages;

    // @ConfigDefault requires a Supplier class that will be instantiated and have the value supplied as the default
    // value for this element if no value could be loaded from the config.
    //  - Type safety can not be enforced at compile time, but it is enforced at run time, so be sure to use this wisely.
    //  - @ConfigDefault conflicts with @Required
    //
    // TimeUnit is an Enumeration, all of which have built in support.
    @ConfigPath("broadcast.unit")
    @ConfigDefault(BroadcastUnitDefault.class)
    private TimeUnit broadcastUnit;

    @ConfigPath("broadcast.duration")
    @ConfigDefault(BroadcastDurationDefault.class)
    private long broadcastDuration;

    // See #preInject
    @ConfigPath("handled-example.immutable")
    @Required
    private MutableHandledConfigObject mutableHandledExample;

    // See #preInject
    @ConfigPath("handled-example.mutable")
    @Required
    private ImmutableHandledConfigObject immutableHandledExample;

    // See #preInject
    //
    // When using a Collection for a configuration section that is defined in the map form,
    // only the values will be added to the Collection.
    @ConfigPath("handled-example")
    @Required
    private Set<HandledConfigObject> handledExamples;

    //Annotations @PreConfigInject and @PostConfigInject can be used on methods in the following cases:
    // 1) A class annotated with @Config that is directly injected by a ConfigInjector
    // 2) A class annotated with @ConfigDeserializable that is resolved by a ConfigInjector as a dependency
    //    of the class being injected
    //
    //  @PreConfigInject is called before HK2 dependency injection & config value injection is done
    //  @PostConfigInject is called after both injection phases are complete
    //
    // The only possible parameters are no parameters or the ConfigInjectionContext that can be used
    // for registering your own ElementDeserializers & more.
    @PreConfigInject
    private void preInject(ConfigInjectionContext context) {
        context.getLogger().info("Injecting ExampleConfig");

        // The ElementDeserializerRegistry can be used to add your own ElementDeserializers that manually handle
        // the deserialization of the type(s) you specify.
        final ElementDeserializerRegistry registry = context.getDeserializerRegistry();

        final int priorityToUse = ElementDeserializers.MEDIUM_PRIORITY;

        final MutableHandledConfigObject.Deserializer mutableDeserializer = new MutableHandledConfigObject.Deserializer(priorityToUse);
        registry.register(mutableDeserializer);

        final ImmutableHandledConfigObject.Deserializer immutableDeserializer = new ImmutableHandledConfigObject.Deserializer(
            priorityToUse);
        registry.register(immutableDeserializer);

        // Now both MutableHandledConfigObject and ImmutableHandledConfigObject have their custom ElementDeserializers registered so that
        // they can be deserialized properly.
        // However, an issue now is that fields with the type HandledConfigObject can not be deserialized.
        // Therefore we can map the Immutable implementation to HandledConfigObject

        registry.mapType(HandledConfigObject.class)
            .withPriority(ElementDeserializers.LOW_PRIORITY)
            .to(ImmutableHandledConfigObject.class);

        // Values can also be mapped, instead of mapping the type.
        // So if we wanted to handle Timestamps by deserializing a Date, and then creating a Timestamp object with that
        // date's time long, the following would be done.
        registry.mapValueOf(Timestamp.class)
            .from(Date.class)
            .by((date) -> new Timestamp(date.getTime()));
    }

    @PostConfigInject
    private void postInject() {
        this.logger.info("ExampleConfig successfully injected.");
        this.logger.info("========");
        this.logger.info(this.toString());
        this.logger.info("========");
    }

    public ImmutableList<Color> getWarmColors() {
        return this.warmColors;
    }

    public ImmutableMap<String, String> getMessages() {
        return this.messages;
    }

    public long getBroadcastDuration() {
        return this.broadcastDuration;
    }

    public TimeUnit getBroadcastUnit() {
        return this.broadcastUnit;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("warmColors", "[" + String.join(", ", this.warmColors.stream().map(Object::toString).collect(Collectors.toList())) + "]")
            .add("warmColorsAsMap", "{" + String.join(", ",
                this.warmColorsAsMap.entrySet().stream().map((entry) -> entry.getKey() + ": " + entry.getValue())
                    .collect(Collectors.toList())) + "}")
            .add("mainColor", this.mainColor)
            .add("messages", this.messages)
            .add("broadcastUnit", this.broadcastUnit)
            .add("broadcastDuration", this.broadcastDuration)
            .toString();
    }

}