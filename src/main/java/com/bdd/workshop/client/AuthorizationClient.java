package com.bdd.workshop.client;

import java.util.List;
import java.util.Objects;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.bdd.workshop.client.dto.AuthorizationRequest;
import com.bdd.workshop.client.dto.AuthorizationResponse;
import com.bdd.workshop.type.OrganisationNumber;
import com.bdd.workshop.type.TaxpayerIdentificationNumber;

public class AuthorizationClient {
    private final RestTemplate restTemplate;

    public AuthorizationClient(String baseUrl) {
        this.restTemplate = new RestTemplateBuilder()
            .rootUri(baseUrl)
            .build();
    }

    public List<OrganisationNumber> hasAccessToOrganisations(TaxpayerIdentificationNumber TaxpayerIdentificationNumber) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AuthorizationRequest> request = new HttpEntity<>(
            new AuthorizationRequest(TaxpayerIdentificationNumber),
            headers
        );

        AuthorizationResponse response =
            restTemplate
                .postForEntity(
                    "/authorization",
                    request,
                    AuthorizationResponse.class
                )
                .getBody();

        return Objects.requireNonNull(response).getOrganisations();
    }
}
