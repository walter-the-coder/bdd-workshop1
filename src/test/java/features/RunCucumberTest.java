package features;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.bdd.workshop.Main;

import features.simulator.common.Simulator;
import io.cucumber.core.options.Constants;
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty, json:target/cucumber-report.json")
@ConfigurationParameter(
    key = Constants.GLUE_PROPERTY_NAME,
    value = "features"
)
@ExcludeTags("NotImplemented")
@CucumberContextConfiguration
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase(
    beanName = "mainDatasource",
    provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.EMBEDDED,
    type = AutoConfigureEmbeddedDatabase.DatabaseType.H2,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD
)
@ContextConfiguration(
    classes = { CucumberTestConfig.class }
)
@SpringBootTest(
    classes = { Main.class },
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
public class RunCucumberTest {
    @Before
    void runBeforeScenarioHook() {
        Simulator.SimulatorFactory.resetAll();
    }
}


