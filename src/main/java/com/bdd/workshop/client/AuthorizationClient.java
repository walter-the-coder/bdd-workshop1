package com.bdd.workshop.client;

import static com.bdd.workshop.util.JsonUtil.writeJson;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import com.bdd.workshop.client.dto.AuthorizationRequest;
import com.bdd.workshop.client.dto.AuthorizationResponse;
import com.bdd.workshop.type.OrganisationNumber;
import com.bdd.workshop.type.PersonId;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthorizationClient {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public AuthorizationClient(String baseUrl, ObjectMapper objectMapper) {
        this.restClient = RestClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

        this.objectMapper = objectMapper;
    }

    public List<OrganisationNumber> hasAccessToOrganisations(PersonId personId) {
        AuthorizationResponse response =
            restClient.post()
                .uri("/authorization")
                .contentType(MediaType.APPLICATION_JSON)
                .body(writeJson(objectMapper, new AuthorizationRequest(personId)))
                .retrieve()
                .toEntity(AuthorizationResponse.class)
                .getBody();

        return Objects.requireNonNull(response).organisations();
    }
}
