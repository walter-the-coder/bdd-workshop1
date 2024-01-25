package com.bdd.workshop.type;

import com.fasterxml.jackson.annotation.JsonValue;

public record OrganisationNumber(
    @JsonValue String value
) {
    @Override
    public String toString() {
        return value;
    }
}
