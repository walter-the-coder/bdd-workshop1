package com.bdd.workshop.controller.dto;

import java.util.HashMap;
import java.util.Map;

public record ValidationResponse(
    Map<String, String> validationErrors
) {
    public ValidationResponse {
        if (validationErrors == null) {
            validationErrors = new HashMap<>();
        }
    }

    public boolean isValid() {
        return validationErrors().isEmpty();
    }
}
