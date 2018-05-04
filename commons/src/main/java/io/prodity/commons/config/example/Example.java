package io.prodity.commons.config.example;

import io.prodity.commons.config.annotate.Colorize;
import io.prodity.commons.config.annotate.Config;
import io.prodity.commons.config.annotate.ConfigValue;
import java.util.Map;

@Config(fileName = "config.yml")
public class Example {

    @ConfigValue(path = "test.string")
    @Colorize
    private String testColorizedString;

    @ConfigValue(path = "test.map")
    private Map<String, Integer> testMap;

}