package com.bdd.workshop.controller.dto;

import java.util.Objects;

import com.bdd.workshop.type.OrganisationNumber;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceptionResponse {
    private String message;

    public ReceptionResponse() {
        message = null;
    }

    public ReceptionResponse(OrganisationNumber organisationNumber) {
        message =
            "The data for organisation number " + organisationNumber.toString() + " was submitted successfully.";
    }

    public ReceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        ReceptionResponse response = (ReceptionResponse) object;
        return Objects.equals(message, response.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }

    @Override
    public String toString() {
        return "ReceptionResponse{" +
            "message='" + message + '\'' +
            '}';
    }
}
