package features;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import com.bdd.workshop.ApplicationConfig;

import features.simulator.AuthorizationServerSimulator;

@TestConfiguration
@Import(ApplicationConfig.class)
public class CucumberTestConfig {

    public CucumberTestConfig() {
        System.setProperty("AUTHORIZATION_URL", AuthorizationServerSimulator.getInstance().getUrl());
    }

    @Bean
    RestClient restClient() {
        return RestClient.builder()
            .baseUrl("http://localhost:8080")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
}
