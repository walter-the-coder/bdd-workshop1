package com.bdd.workshop.controller.dto;

import java.util.HashMap;
import java.util.Map;

import com.bdd.workshop.type.OrganisationNumber;

public record ReceptionResponse(
    String message
) {
    public ReceptionResponse(OrganisationNumber organisationNumber) {
        this("The data for organisation number " + organisationNumber.toString() + " was submitted successfully.");
    }
}
