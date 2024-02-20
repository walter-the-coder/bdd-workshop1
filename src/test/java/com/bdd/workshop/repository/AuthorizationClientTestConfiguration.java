package com.bdd.workshop.repository;

import com.bdd.workshop.client.AuthorizationClient;
import com.bdd.workshop.service.AuthorizationService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class AuthorizationClientTestConfiguration {
    public AuthorizationClient authorizationClient = Mockito.mock(AuthorizationClient.class);
    public AuthorizationService authorizationService = new AuthorizationService(true);
    @Bean
    @Primary
    public AuthorizationClient authorizationClient() {
        return authorizationClient;
    }
    @Bean
    @Primary
    public AuthorizationService authorizationService() {
        return authorizationService;
    }
}
