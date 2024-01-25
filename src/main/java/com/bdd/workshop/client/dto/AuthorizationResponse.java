package com.bdd.workshop.client.dto;

import java.util.ArrayList;
import java.util.List;

import com.bdd.workshop.type.OrganisationNumber;

public record AuthorizationResponse(
    List<OrganisationNumber> organisations
) {
    public AuthorizationResponse {
        if (organisations == null) {
            organisations = new ArrayList<>();
        }
    }
}
