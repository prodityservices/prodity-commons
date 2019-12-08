package io.prodity.commons.config.inject.element;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import io.prodity.commons.config.annotate.inject.ConfigInject;
import io.prodity.commons.config.annotate.inject.ConfigNodeKey;
import io.prodity.commons.config.annotate.inject.ConfigNodeValue;
import io.prodity.commons.config.annotate.inject.ConfigPath;
import io.prodity.commons.reflect.element.NamedAnnotatedElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.SimpleConfigurationNode;

/**
 * Strategies defining how the values of {@link ConfigElement}s should be resolved.
 */
public enum ElementInjectionStrategy {

    /**
     * The value is resolved from the key of a {@link ConfigurationNode}.
     */
    NODE_KEY((element) -> {
        return element.isAnnotationPresent(ConfigNodeKey.class);
    }, (element, node) -> {
        return SimpleConfigurationNode.root().setValue(node.getKey());
    }, ConfigNodeValue.class, ConfigPath.class),

    /**
     * The value is resolved from the value of a {@link ConfigurationNode}.
     */
    NODE_VALUE((element) -> {
        return element.isAnnotationPresent(ConfigNodeValue.class);
    }, (element, node) -> {
        return SimpleConfigurationNode.root().setValue(node.getValue());
    }, ConfigNodeKey.class, ConfigPath.class),

    /**
     * The value is resolved from a path of a {@link ConfigurationNode}.
     */
    NODE_PATH((element) -> {
        if (element.isAnnotationPresent(ConfigPath.class)) {
            return true;
        }
        return element.getHandle() instanceof Parameter || element.isAnnotationPresent(ConfigInject.class);
    }, (element, node) -> {
        return node.getNode(element.getPath().toArray());
    }, ConfigNodeKey.class, ConfigNodeValue.class);

    public static ElementInjectionStrategy resolveStrategy(NamedAnnotatedElement element) throws IllegalStateException {
        Preconditions.checkNotNull(element, "element");
        for (ElementInjectionStrategy strategy : ElementInjectionStrategy.values()) {
            if (!strategy.hasStrategy(element)) {
                continue;
            }
            strategy.verifyNoConflicting(element);
            return strategy;
        }
        throw new IllegalStateException("element=" + element.getName() + " has no valid ElementInjectionStrategy");
    }

    private final Predicate<NamedAnnotatedElement> elementPredicate;
    private final BiFunction<ConfigElement<?>, ConfigurationNode, ConfigurationNode> nodeResolver;
    private final ImmutableSet<Class<? extends Annotation>> conflictingAnnotations;

    ElementInjectionStrategy(Predicate<NamedAnnotatedElement> elementPredicate,
        BiFunction<ConfigElement<?>, ConfigurationNode, ConfigurationNode> nodeResolver,
        Class<? extends Annotation>... conflictingAnnotations) {
        Preconditions.checkNotNull(elementPredicate, "elementPredicate");
        Preconditions.checkNotNull(nodeResolver, "nodeResolver");
        this.elementPredicate = elementPredicate;
        this.nodeResolver = nodeResolver;
        this.conflictingAnnotations = ImmutableSet.copyOf(conflictingAnnotations);
    }

    public boolean hasStrategy(NamedAnnotatedElement element) {
        Preconditions.checkNotNull(element, "element");
        return this.elementPredicate.test(element);
    }

    public void verifyNoConflicting(NamedAnnotatedElement element) throws IllegalStateException {
        Preconditions.checkNotNull(element, "element");
        for (Class<? extends Annotation> annotation : this.conflictingAnnotations) {
            if (element.isAnnotationPresent(annotation)) {
                throw new IllegalStateException(
                    "element=" + element + " has conflicting annotation=" + annotation.getName() + " of strategy=" + this.name());
            }
        }
    }

    public ConfigurationNode resolveNode(ConfigElement<?> element, ConfigurationNode node) {
        Preconditions.checkNotNull(element, "element");
        Preconditions.checkNotNull(node, "node");
        return this.nodeResolver.apply(element, node);
    }

}