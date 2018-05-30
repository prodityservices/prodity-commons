package io.prodity.commons.db;

import io.prodity.commons.inject.bind.PluginBinder;
import io.prodity.commons.plugin.ProdityPlugin;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Provider;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.JustInTimeInjectionResolver;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.SqlOperation;

public class DatabaseResolver implements JustInTimeInjectionResolver {

    private final Set<Type> seenBefore = ConcurrentHashMap.newKeySet();
    private final ProdityPlugin plugin;
    private final ServiceLocator locator;
    private final Provider<Jdbi> jdbi;

    @Inject
    public DatabaseResolver(ProdityPlugin plugin, ServiceLocator locator, Provider<Jdbi> jdbi) {
        this.plugin = plugin;
        this.locator = locator;
        this.jdbi = jdbi;
    }

    @Override
    public boolean justInTimeResolution(Injectee failedInjectionPoint) {
        if (failedInjectionPoint.getRequiredType() instanceof Class<?> && this.seenBefore.add(failedInjectionPoint.getRequiredType())) {
            Class<?> requiredType = ((Class) failedInjectionPoint.getRequiredType());
            if (requiredType.isInterface() && DatabaseResolver.looksLikeSqlObject(requiredType)) {
                return this.createOnDemand(requiredType);
            }
        }
        return false;
    }

    private boolean createOnDemand(Class<?> type) {
        Object instance = this.jdbi.get().onDemand(type);
        ServiceLocatorUtilities.bind(this.locator, new PluginBinder(this.plugin) {
            @Override
            protected void configure() {
                this.bind(instance).to(type);
            }
        });
        return true;
    }

    // Copied from JDBI
    private static boolean looksLikeSqlObject(Class<?> type) {
        if (SqlObject.class.isAssignableFrom(type)) {
            return true;
        }

        return Stream.of(type.getMethods())
            .flatMap(m -> Stream.of(m.getAnnotations()))
            .anyMatch(a -> a.annotationType().isAnnotationPresent(SqlOperation.class));
    }

}