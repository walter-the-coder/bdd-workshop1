package features;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
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

@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty, json:target/cucumber-report.json")
@ConfigurationParameter(
    key = Constants.GLUE_PROPERTY_NAME,
    value = "features"
)
@CucumberContextConfiguration
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase(
    provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD
)
@ContextConfiguration(
    classes = { CucumberTestConfig.class }
)
@SpringBootTest(
    classes = { Main.class },
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    useMainMethod = SpringBootTest.UseMainMethod.ALWAYS
)
public class RunCucumberTest {
    @Before
    void runBeforeScenarioHook() {
        Simulator.SimulatorFactory.resetAll();
    }
}


