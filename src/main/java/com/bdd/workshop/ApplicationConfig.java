package com.bdd.workshop;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import com.bdd.workshop.client.AuthorizationClient;
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

    @Primary
    @Bean("mainDatasource")
    @ConfigurationProperties("spring.datasource.main")
    public DataSource mainDatasource(
        @Value("${spring.datasource.main.embedded:false}") Boolean embedded
    ) {
        if (embedded) {
            return new EmbeddedDatabaseBuilder()
                .setType(H2)
                .build();
        } else {
            return DataSourceBuilder.create().type(HikariDataSource.class).build();
        }
    }

    @Bean("mainDatasourceNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate mainDatasourceNamedParameterJdbcTemplate(
        @Qualifier("mainDatasource") DataSource mainDatasource
    ) {
        return new NamedParameterJdbcTemplate(mainDatasource);
    }

    @Bean("loginDatasource")
    @ConfigurationProperties("spring.datasource.login")
    public DataSource loginDatasource(
        @Value("${spring.datasource.login.embedded:false}") Boolean embedded
    ) {
        if (embedded) {
            return new EmbeddedDatabaseBuilder()
                .setType(H2)
                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
                .build();
        } else {
            return DataSourceBuilder.create().type(HikariDataSource.class).build();
        }
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
