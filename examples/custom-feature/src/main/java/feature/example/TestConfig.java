package feature.example;

import com.google.common.base.MoreObjects;
import io.prodity.commons.config.annotate.inject.Config;
import io.prodity.commons.config.annotate.inject.ConfigPath;

@Config(fileName = "config.yml")
public class TestConfig {

    @ConfigPath("test.int")
    private int testInt;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("testInt", this.testInt)
            .toString();
    }
    
}