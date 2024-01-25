package com.bdd.workshop.type;

import com.fasterxml.jackson.annotation.JsonValue;

public record PersonId(
    @JsonValue String value
) {
    @Override
    public String toString() {
        return value;
    }
}
