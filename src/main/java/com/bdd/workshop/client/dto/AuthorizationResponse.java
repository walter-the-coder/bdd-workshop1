package com.bdd.workshop.client.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.bdd.workshop.type.OrganisationNumber;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizationResponse {
    private List<OrganisationNumber> organisations;

    public AuthorizationResponse() {
        this.organisations = new ArrayList<>();
    }

    public AuthorizationResponse(List<OrganisationNumber> organisations) {
        if (organisations == null) {
            this.organisations = new ArrayList<>();
        } else {
            this.organisations = organisations;
        }
    }

    public List<OrganisationNumber> getOrganisations() {
        return organisations;
    }

    public void setOrganisations(List<OrganisationNumber> organisations) {
        this.organisations = organisations;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        AuthorizationResponse that = (AuthorizationResponse) object;
        return Objects.equals(organisations, that.organisations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organisations);
    }

    @Override
    public String toString() {
        return "AuthorizationResponse{" +
            "organisations=" + organisations +
            '}';
    }
}
