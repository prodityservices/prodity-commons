package io.prodity.commons.repository.registry;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import io.prodity.commons.identity.Identifiable;
import io.prodity.commons.name.Names;
import io.prodity.commons.repository.Repository;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import org.jvnet.hk2.annotations.Service;

@Service
public class SimpleRepositoryRegistry implements RepositoryRegistry {

    private final Map<String, Repository<?, ?>> repositories;

    public SimpleRepositoryRegistry() {
        this.repositories = Maps.newConcurrentMap();
    }

    @Override
    public <K, V extends Identifiable<K>> Repository<K, V> getRepository(@Nullable String repositoryName) {
        if (repositoryName == null) {
            return null;
        }

        final Repository<?, ?> repository = this.repositories.get(repositoryName);
        return repository == null ? null : (Repository<K, V>) repository;
    }

    @Override
    public Optional<Repository<?, ?>> register(Repository<?, ?> repository) {
        Preconditions.checkNotNull(repository, "repository");
        final String name = Names.getName(repository.getClass());
        return Optional.ofNullable(this.repositories.put(name, repository));
    }

}