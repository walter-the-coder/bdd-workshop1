package com.bdd.workshop;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bdd.workshop.client.AuthorizationClient;
import com.bdd.workshop.exceptionHandling.CustomRuntimeException;
import com.bdd.workshop.exceptionHandling.CustomWebExceptionHandler;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@Import(CustomWebExceptionHandler.class)
public class ApplicationConfig {

    @Bean
    public AuthorizationClient authorizationClient(
        @Value("${clients.authorization.url}") String baseUrl
    ) {
        return new AuthorizationClient(baseUrl);
    }

    @Bean("mainDatasource")
    @ConfigurationProperties("spring.datasource.main")
    public HikariDataSource mainDatasource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean("mainDatasourceJdbcTemplate")
    public JdbcTemplate mainDatasourceJdbcTemplate(
        @Qualifier("mainDatasource") DataSource dsMain
    ) {
        try {
            return new JdbcTemplate(dsMain.getConnection());
        } catch (SQLException e) {
            throw new CustomRuntimeException(
                "MAIN_DATASOURCE_FAILED",
                "Could not get connection for main datasource",
                e,
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Bean("mainDatasourceNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate mainDatasourceNamedParameterJdbcTemplate(
        @Qualifier("mainDatasource") DataSource dsMain
    ) {
        return new NamedParameterJdbcTemplate(dsMain);
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
            .build();
}
