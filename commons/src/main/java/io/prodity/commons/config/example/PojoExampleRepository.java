package io.prodity.commons.config.example;

import com.google.common.collect.Maps;
import io.prodity.commons.config.annotate.inject.Config;
import io.prodity.commons.config.annotate.inject.ConfigPath;
import io.prodity.commons.config.annotate.modify.Colorize;
import io.prodity.commons.repository.Repository;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import javax.inject.Named;

@Config(fileName = "pojos.yml")
@Named(PojoExampleRepository.NAME)
public class PojoExampleRepository implements Repository<String, PojoExample> {

    public static final String NAME = "pojo-repository";

    @ConfigPath("pojos")
    @Colorize
    private final Map<String, PojoExample> pojos = Maps.newHashMap();

    public PojoExampleRepository() {

    }

    @Override
    public boolean containsId(String key) {
        throw new UnsupportedOperationException("noop");
    }

    @Override
    public PojoExample get(String key) {
        throw new UnsupportedOperationException("noop");
    }

    @Override
    public Optional<PojoExample> getSafely(String key) {
        throw new UnsupportedOperationException("noop");
    }

    @Override
    public Collection<PojoExample> getAsCollection(Iterable<String> keys) {
        throw new UnsupportedOperationException("noop");
    }

    @Override
    public Map<String, PojoExample> getAsMap(Iterable<String> keys) {
        throw new UnsupportedOperationException("noop");
    }

}