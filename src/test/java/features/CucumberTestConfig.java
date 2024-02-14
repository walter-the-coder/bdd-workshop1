package features;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.bdd.workshop.ApplicationConfig;

import features.simulator.AuthorizationServerSimulator;

@TestConfiguration
@Import(ApplicationConfig.class)
public class CucumberTestConfig {

    public CucumberTestConfig() {
        System.setProperty("AUTHORIZATION_URL", AuthorizationServerSimulator.getInstance().getUrl());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
            .rootUri("http://localhost:8080")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
}
