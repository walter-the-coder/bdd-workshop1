package com.bdd.workshop.controller.dto;

import java.time.LocalDateTime;

import com.bdd.workshop.type.OrganisationNumber;
import com.bdd.workshop.type.PersonId;
import com.bdd.workshop.type.TaxCategory;
import com.bdd.workshop.type.TaxationPeriodType;

public record ReceptionDto(
    OrganisationNumber organisationNumber,
    PersonId submitterId,
    TaxCategory category,
    Integer year,
    TaxationPeriodType taxationPeriodType,
    LocalDateTime timeOfSubmission
) { }
