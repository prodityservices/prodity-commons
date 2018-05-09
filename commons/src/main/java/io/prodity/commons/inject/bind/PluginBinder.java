package io.prodity.commons.inject.bind;

import io.prodity.commons.inject.Export;
import io.prodity.commons.plugin.ProdityPlugin;
import java.lang.reflect.Type;
import javax.inject.Singleton;
import org.apache.commons.lang3.Validate;
import org.glassfish.hk2.api.DescriptorVisibility;
import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.Binder;

/**
 * This class provides easy mechanisms for creating plugin-local bindings. All bindings default
 * to {@link DescriptorVisibility#LOCAL} all will not be available outside
 * of the plugin.  To implement, override {@link #configure()}.  Unlike HK2, the default scope
 * is {@link Singleton}.
 * <p />
 * Note that compared to Guice, the operation order is reversed. In Guice you bind a contract(interface)
 * to an instance (implementation). In HK2, you bind a service(implementation) to all contracts(interfaces)
 * that it satisfies.
 */
public abstract class PluginBinder implements Binder {

    private final ProdityPlugin plugin;
    private DynamicConfiguration config;
    private AbstractBindingBuilder<?> current;

    protected PluginBinder(ProdityPlugin plugin) {
        this.plugin = plugin;
    }

    public final <T> BindingBuilder<T> bind(Class<T> serviceType) {
        return resetBuilder(AbstractBindingBuilder.create(serviceType, true));
    }

    public final <T> BindingBuilder<T> bind(TypeLiteral<T> serviceType) {
        return resetBuilder(AbstractBindingBuilder.create(serviceType, true));
    }

    public final <T> BindingBuilder<T> bind(Type type) {
        return resetBuilder(AbstractBindingBuilder.create(type, true));

    }

    public final <T> BindingBuilder<T> bind(T service) {
        return resetBuilder(AbstractBindingBuilder.create(service));
    }

    private <T> AbstractBindingBuilder<T> resetBuilder(AbstractBindingBuilder<T> newBuilder) {
        if (current != null) {
            current.complete(config, className -> {
                try {
                    return getClass().getClassLoader().loadClass(className);
                } catch (ClassNotFoundException e) {
                    throw new MultiException(e);
                }
            });
        }

        current = newBuilder;

        if (newBuilder != null) {
            newBuilder.withMetadata(Export.PLUGIN_META_KEY, plugin.getName());
            newBuilder.withVisibility(DescriptorVisibility.LOCAL);
            newBuilder.in(Singleton.class);
        }

        return newBuilder;
    }

    @Override
    public final void bind(DynamicConfiguration config) {
        Validate.isTrue(this.config == null, "Recursive configuration call detected.");
        Validate.notNull(config);

        this.config = config;
        try {
            configure();
        } finally {
            if (current != null) {
                resetBuilder(null);
            }
            this.config = null;
        }
    }

    protected abstract void configure();
}
