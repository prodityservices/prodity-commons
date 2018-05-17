package feature.example;

import com.google.common.base.MoreObjects;
import io.prodity.commons.color.Color;
import io.prodity.commons.color.MutableColor;
import io.prodity.commons.config.annotate.inject.Config;
import io.prodity.commons.config.annotate.inject.ConfigPath;
import io.prodity.commons.config.inject.ConfigInjector;
import io.prodity.commons.config.inject.except.ConfigInjectException;
import io.prodity.commons.inject.Eager;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.jvnet.hk2.annotations.Service;

@Service
public class ConfigTest implements Eager {

    @Inject
    private ConfigInjector injector;

    @Inject
    private Logger logger;

    @PostConstruct
    public void testInjector() {
        this.logger.info("INJECTING CONFIG.");
        try {
            final TestConfig testConfig = this.injector.inject(TestConfig.class);
            this.logger.info(testConfig.toString());
        } catch (ConfigInjectException e) {
            e.printStackTrace();
        }
    }

    @Config(fileName = "config.yml")
    private static class TestConfig {

        @ConfigPath("test.int")
        private int testInt;

        @ConfigPath("test.int2")
        private AtomicInteger testInt2;

        @ConfigPath("test.long")
        private long testLong;

        @ConfigPath("test.color")
        private Color testColor;

        @ConfigPath("test.mutablecolor")
        private MutableColor testMutableColor;

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                .add("testInt", this.testInt)
                .add("testInt2", this.testInt2)
                .add("testLong", this.testLong)
                .add("testColor", this.testColor)
                .add("testMutableColor", this.testMutableColor)
                .toString();
        }

    }

}