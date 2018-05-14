package io.prodity.commons.config.example;

import io.prodity.commons.config.annotate.deserialize.Colorize;
import io.prodity.commons.config.annotate.deserialize.ConfigDefault;
import io.prodity.commons.config.annotate.deserialize.LoadFromRepository;
import io.prodity.commons.config.annotate.inject.Config;
import io.prodity.commons.config.annotate.inject.ConfigInject;
import io.prodity.commons.config.annotate.inject.ConfigPath;
import io.prodity.commons.config.annotate.inject.Required;
import io.prodity.commons.config.annotate.listen.PostConfigLoad;
import io.prodity.commons.config.annotate.listen.PreConfigLoad;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Config(fileName = "config.yml")
public class ExampleConfig {

    @ConfigPath(value = "test.string")
    @Required
    @ConfigDefault(ExampleConfig.TestDefaultSupplier.class)
    @Colorize
    private String testColorizedString;

    @ConfigPath("test.map")
    private Map<String, Integer> testMap;

    @ConfigPath("test.pojo")
    private PojoExample pojoExample;

    @ConfigPath("test.list.pojo")
    private List<PojoExample> pojoExampleList;

    @ConfigPath("test.map.pojo")
    @LoadFromRepository(PojoExampleRepository.NAME)
    private Map<String, PojoExample> pojoExampleMap;

    @ConfigPath("test.id.pojo")
    @Colorize //Colorize all strings in the PojoExample dependency tree
    @LoadFromRepository(PojoExampleRepository.NAME) //Assumes the config deserialize at the path is of the ID type PojoExample has
    private PojoExample pojoExampleById;

    @ConfigInject
    private void setPojoExampleById(@ConfigPath("test.somepath") PojoExample pojoExample) {
        //TODO handle object
    }

    @PreConfigLoad
    private void preLoad() {

    }

    @PostConfigLoad
    private void postLoad() {

    }

    private static class TestDefaultSupplier implements Supplier<String> {

        @Override
        public String get() {
            return "&bHello &4World";
        }

    }

}