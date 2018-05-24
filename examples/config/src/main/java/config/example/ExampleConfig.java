package config.example;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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
import io.prodity.commons.spigot.inject.TimeUnit;
import java.util.function.Supplier;
import java.util.logging.Logger;
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

    // @ConfigPath specifies the path of the field/parameter in the config file
    //   - When no annotation is specified, the field name is used
    //   - Be sure to add the -parameters compiler argument to retain parameter names for the above case.
    //
    // @LoadFromRepository will load the element(s) by their IDs from the specified Repository name.
    // Therefore, the IDs of the element(s) are specified in the config.
    //
    // In this example, Color is an interface so it can not be directly injected without special handling.
    // Since Color is in commons, the default ElementDeserializerRegistry maps Color out to one of it's implementations
    // that is ImmutableColor. More information on how you can map your own interface types to their implementations can be seen below.
    //
    // As for collections, Set, List, ImmutableSet, ImmutableList, Arrays, & a few others have support built in by default.
    @ConfigPath("warm-colors")
    @LoadFromRepository(ColorRepository.NAME)
    private ImmutableList<Color> warmColors;

    // @Colorize specifies that elements in the following cases should be colorized
    // 1) String types
    // 2) String types that are elements of lists, sets, & their guava immutable implementations
    // 3) String types that are values of maps & their guava immutable implementations
    //
    // @Required specifies that the value is 100% required to be present in the config.yml. If it is not
    // present an exception will be thrown.
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

    @Inject
    private Logger logger;

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
    }

    @PostConfigInject
    private void postInject() {
        this.logger.info("config loaded.");
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

}