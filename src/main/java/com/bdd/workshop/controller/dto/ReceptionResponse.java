package com.bdd.workshop.controller.dto;

import java.util.HashMap;
import java.util.Map;

import com.bdd.workshop.type.OrganisationNumber;

public record ReceptionResponse(
    String message,
    Map<String, String> validationErrors
) {
    public ReceptionResponse {
        if (validationErrors == null) {
            validationErrors = new HashMap<>();
        }
    }

    public ReceptionResponse(OrganisationNumber organisationNumber) {
        this(
            "The data for organisation number " + organisationNumber.toString() + " was submitted successfully.",
            new HashMap<>()
        );
    }

    public ReceptionResponse(Map<String, String> validationErrors) {
        this(
            "The data did not validate.",
            validationErrors
        );
    }

    public boolean isValid() {
        return validationErrors().isEmpty();
    }
}
