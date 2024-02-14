package com.bdd.workshop.client.dto;

import java.util.Objects;

import com.bdd.workshop.type.PersonId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizationRequest {
    private PersonId personId;

    public AuthorizationRequest() {
        personId = null;
    }

    public AuthorizationRequest(PersonId personId) {
        this.personId = personId;
    }

    public PersonId getPersonId() {
        return personId;
    }

    public void setPersonId(PersonId personId) {
        this.personId = personId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        AuthorizationRequest that = (AuthorizationRequest) object;
        return Objects.equals(personId, that.personId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId);
    }

    @Override
    public String toString() {
        return "AuthorizationRequest{" +
            "personId=" + personId +
            '}';
    }
}
