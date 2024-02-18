package features;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

import com.bdd.workshop.ApplicationConfig;

import features.simulator.AuthorizationServerSimulator;

@TestConfiguration
@Import(ApplicationConfig.class)
public class CucumberTestConfig {
    public CucumberTestConfig() {
        System.setProperty("AUTHORIZATION_URL", AuthorizationServerSimulator.getInstance().getUrl());
    }

    @Autowired
    @Qualifier("loginDatasource")
    private DataSource loginDatasource;

    @PostConstruct
    public void migrateFlyway() {
        Flyway
            .configure()
            .dataSource(loginDatasource)
            .cleanDisabled(false)
            .locations("classpath:/login")
            .load()
            .migrate();
    }
}
