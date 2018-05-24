package io.prodity.commons.inject.impl;

import io.prodity.commons.inject.Async;
import io.prodity.commons.plugin.ProdityPlugin;
import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.api.Rank;
import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.UnsatisfiedDependencyException;
import org.glassfish.hk2.utilities.InjecteeImpl;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

/**
 * Functions identically to ThreeThirtyResolver, with a few additional behaviors:
 * - Async<T> can be injected anywhere T can be injected, with a new AsyncImpl wrapper
 *   being created around it.
 * - Optional<T> can be injected anywhere T can be injected as an optional injection
 *   point.
 */
public class ProdityInjectionResolver implements InjectionResolver<Inject> {

    private final ServiceLocator locator;
    private final Platform platform;
    private final ProdityPlugin plugin;

    @Inject
    public ProdityInjectionResolver(ServiceLocator locator, Platform platform, ProdityPlugin plugin) {
        this.locator = locator;
        this.platform = platform;
        this.plugin = plugin;
    }

    @Override
    @Nullable
    public Object resolve(Injectee injectee, ServiceHandle<?> root) {
        if (injectee.getRequiredType() instanceof ParameterizedType) {
            ParameterizedType requiredType = (ParameterizedType) injectee.getRequiredType();
            if (Optional.class.isAssignableFrom((Class<?>) requiredType.getRawType())) {
                return this.optionalResolve(injectee, root);
            } else if (Async.class.isAssignableFrom((Class<?>) requiredType.getRawType())) {
                return this.asyncResolve(injectee, root);
            }
        }
        return this.regularResolve(injectee, root);

    }

    private Optional<?> optionalResolve(Injectee injectee, ServiceHandle<?> root) {
        InjecteeImpl nested = new InjecteeImpl(injectee);
        nested.setRequiredType(((ParameterizedType) nested.getRequiredType()).getActualTypeArguments()[0]);
        nested.setOptional(true);

        return Optional.ofNullable(this.resolve(nested, root));
    }

    @Nullable
    private Async<?> asyncResolve(Injectee injectee, ServiceHandle<?> root) {
        InjecteeImpl nested = new InjecteeImpl(injectee);
        nested.setRequiredType(((ParameterizedType) nested.getRequiredType()).getActualTypeArguments()[0]);

        Object inner = this.resolve(nested, root);
        if (inner == null) {
            return null;
        }
        return new AsyncImpl<>(inner, this.platform.getAsyncExecutor());
    }

    @Nullable
    private Object regularResolve(Injectee injectee, ServiceHandle<?> root) {
        ActiveDescriptor<?> ad = this.locator.getInjecteeDescriptor(injectee);

        if (ad == null) {
            if (injectee.isOptional()) return null;

            throw new MultiException(new UnsatisfiedDependencyException(injectee));
        }

        return this.locator.getService(ad, root, injectee);
    }

    @Override
    public boolean isConstructorParameterIndicator() {
        return false;
    }

    @Override
    public boolean isMethodParameterIndicator() {
        return false;
    }
}
