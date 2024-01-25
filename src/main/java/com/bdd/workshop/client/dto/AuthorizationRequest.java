package com.bdd.workshop.client.dto;

import com.bdd.workshop.type.PersonId;

public record AuthorizationRequest(
    PersonId personId
) { }
