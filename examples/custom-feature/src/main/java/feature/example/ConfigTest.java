package feature.example;

import com.google.common.base.MoreObjects;
import io.prodity.commons.config.annotate.inject.Config;
import io.prodity.commons.config.annotate.inject.ConfigPath;
import io.prodity.commons.config.inject.ConfigInjector;
import io.prodity.commons.config.inject.except.ConfigInjectException;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.jvnet.hk2.annotations.Service;

@Service
public class ConfigTest {

    @Inject
    private ConfigInjector injector;

    @Inject
    private Logger logger;

    private ConfigTest() {
        
    }

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
    private class TestConfig {

        @ConfigPath("test.int")
        private int testInt;

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                .add("testInt", this.testInt)
                .toString();
        }

    }

}