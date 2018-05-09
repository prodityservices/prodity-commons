package io.prodity.commons.config.load.value;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.prodity.commons.config.except.ConfigLoadException;
import io.prodity.commons.config.load.ConfigLoadContext;
import io.prodity.commons.config.load.element.ConfigElement;
import io.prodity.commons.message.color.Colorizer;
import io.prodity.commons.repository.Repository;
import io.prodity.commons.repository.registry.RepositoryRegistry;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import ninja.leaping.configurate.ConfigurationNode;

@Singleton
public class ConfigValueResolver {

    private static final String LITERAL_PERIOD_REGEX = Pattern.quote(".");

    @Inject
    private RepositoryRegistry repositoryRegistry;

    @Inject
    private Colorizer colorizer;

    @Nonnull
    public Optional<Object> resolveValue(@Nonnull ConfigLoadContext<?> context, @Nonnull ConfigElement element,
        @Nonnull String... currentPath)
        throws InstantiationException, IllegalAccessException, ConfigLoadException {
        Preconditions.checkNotNull(context, "context");
        Preconditions.checkNotNull(context.getMasterNode(), "config#getMasterNode");
        Preconditions.checkNotNull(element, "element");

        final String[] path = new String[currentPath.length + 1];
        for (int i = 0; i < currentPath.length; i++) {
            final String pathNode = currentPath[i];
            if (pathNode == null) {
                throw new IllegalArgumentException("an element of  the specified currentPath=" + currentPath.toString() + " is null");
            }
            path[i] = pathNode;
        }
        path[path.length - 1] = element.getPath();

        final ConfigurationNode masterNode = context.getMasterNode();
        final ConfigurationNode targetNode = masterNode.getNode(path);

        if (!targetNode.isVirtual()) {

        }

        Object object = null;
        if (objectOptional.isPresent()) {
            object = objectOptional.get();
        } else if (element.getDefaultSupplier().isPresent()) {
            object = element.loadDefault();
        } else if (!annotation.required()) {
            throw new ConfigLoadException(context, "path=" + path + " does not exist, has no default value, and is required");
        }

        if (element.getRepositoryName().isPresent()) {
            final String repositoryName = element.getRepositoryName().get();
            final Repository<Object, ?> repository = this.repositoryRegistry.getRepository(repositoryName);
            if (repository == null) {
                throw new ConfigLoadException(context, "repository with name=" + repositoryName + " could not be located");
            }

            if (!repository.containsId(object)) {
                throw new ConfigLoadException(context, "repository=" + repositoryName + " does not contain id=" + object);
            }

            object = repository.get(object);
        }

        if (element.isColorized()) {
            //TODO colorize object if possible.
        }

    }

    @Nonnull
    private Optional<Object> getMapValueFromPath(@Nonnull Map<?, ?> rawValues, String path) throws IllegalArgumentException {
        Preconditions.checkNotNull(rawValues, "rawValues");

        final String[] keys = path.split(ConfigValueResolver.LITERAL_PERIOD_REGEX);

        Map<?, ?> currentMap = rawValues;
        for (int index = 0; index < keys.length; index++) {

            final String key = keys[index];

            if (!currentMap.containsKey(key)) {
                return Optional.empty();
            }

            final Object value = currentMap.get(key);
            if (index == keys.length - 1) {
                return Optional.of(value);
            }

            if (!(value instanceof Map)) {
                final String[] currentKeys = Arrays.copyOf(keys, index);
                final String currentPath = String.join(".", currentKeys);
                throw new IllegalArgumentException("currentPath=" + currentPath + " is not a Map, occurred while loading path=" + path);
            }

            currentMap = (Map<?, ?>) value;
        }

        return Optional.empty();
    }

    private Object loadObjectFromRepository(Repository repository, Object keyObject) {
        if (keyObject instanceof Iterable) {
            final Iterable<?> keyIterable = (Iterable<?>) keyObject;
            return repository.getAsCollection(keyIterable);
        } else if (keyObject instanceof Map) {
            final Iterable<?> keyIterable = ((Map) keyObject).keySet();
            return repository.getAsMap(keyIterable);
        } else {

        }
    }

    private Object colorizeObject(Object object) {
        if (object instanceof String) {
            return this.colorizer.translateColors((String) object);
        } else if (object instanceof List) {
            final List<Object> colorizedObjects = Lists.newArrayList();
            for (Object element : (List) object) {
                final Object newElement = this.colorizeObject(element);
                colorizedObjects.add(newElement);
            }
            return colorizedObjects;
        } else if (object instanceof Map) {
            final Map<Object, Object> colorizedObjects = Maps.newHashMap();
            for (Entry<?, ?> entry : ((Map<?, ?>) object).entrySet()) {
                final Object key = entry.getKey();
                final Object newValue = this.colorizeObject(entry.getValue());
                colorizedObjects.put(key, newValue);
            }
            return colorizedObjects;
        } else {
            return object;
        }
    }

}