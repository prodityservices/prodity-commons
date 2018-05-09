package io.prodity.commons.repository.registry;

import io.prodity.commons.identity.Identifiable;
import io.prodity.commons.repository.Repository;
import java.util.Optional;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface RepositoryRegistry {

    <K, V extends Identifiable<K>> Repository<K, V> getRepository(String repositoryName);

    Optional<Repository<?, ?>> register(Repository<?, ?> repository);

}