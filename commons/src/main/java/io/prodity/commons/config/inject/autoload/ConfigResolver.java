package io.prodity.commons.config.inject.autoload;

import com.google.common.collect.Sets;
import io.prodity.commons.config.annotate.inject.Config;
import io.prodity.commons.config.inject.ConfigInjector;
import io.prodity.commons.config.inject.except.ConfigInjectException;
import io.prodity.commons.inject.bind.PluginBinder;
import io.prodity.commons.plugin.ProdityPlugin;
import java.lang.reflect.Type;
import java.util.Set;
import javax.inject.Inject;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.JustInTimeInjectionResolver;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

public class ConfigResolver implements JustInTimeInjectionResolver {

    private final Set<Type> seenBefore = Sets.newConcurrentHashSet();

    @Inject
    private ServiceLocator serviceLocator;

    @Inject
    private ConfigInjector configInjector;

    @Inject
    private ProdityPlugin plugin;

    @Override
    public boolean justInTimeResolution(Injectee failedInjectionPoint) {
        final Type requiredType = failedInjectionPoint.getRequiredType();
        if (requiredType instanceof Class<?> && this.seenBefore.add(requiredType)) {
            final Class<?> requiredClass = (Class<?>) requiredType;
            if (requiredClass.isAnnotationPresent(Config.class)) {
                try {
                    return this.createOnDemand(requiredClass);
                } catch (ConfigInjectException exception) {
                    exception.printStackTrace();
                }
            }
        }

        return false;
    }

    private boolean createOnDemand(Class<?> type) throws ConfigInjectException {
        final Object instance = this.configInjector.inject(type);
        ServiceLocatorUtilities.bind(this.serviceLocator, new PluginBinder(this.plugin) {
            @Override
            protected void configure() {
                this.bind(instance).to(type);
            }
        });
        return true;
    }

}