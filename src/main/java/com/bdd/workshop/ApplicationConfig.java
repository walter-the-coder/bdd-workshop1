package com.bdd.workshop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.bdd.workshop.client.AuthorizationClient;
import com.bdd.workshop.exceptionHandling.CustomWebExceptionHandler;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.blackbird.BlackbirdModule;

@Configuration
@Import(CustomWebExceptionHandler.class)
public class ApplicationConfig {

    @Bean
    public AuthorizationClient authorizationClient(
        @Value("${clients.authorization.url}") String baseUrl,
        ObjectMapper objectMapper
    ) {
        return new AuthorizationClient(baseUrl, objectMapper);
    }

    @Bean
    public ObjectMapper defaultObjectMapper() {
        return DEFAULT_OBJECT_MAPPER;
    }

    public static final ObjectMapper DEFAULT_OBJECT_MAPPER =
        JsonMapper.builder()
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .addModule(new BlackbirdModule())
            .build();
}
