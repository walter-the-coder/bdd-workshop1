package com.bdd.workshop.controller.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationResponse {
    private Map<String, String> validationErrors;

    public ValidationResponse() {
        validationErrors = new HashMap<String, String>();
    }

    public ValidationResponse(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public boolean isValid() {
        return validationErrors.isEmpty();
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        ValidationResponse that = (ValidationResponse) object;
        return Objects.equals(validationErrors, that.validationErrors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(validationErrors);
    }

    @Override
    public String toString() {
        return "ValidationResponse{" +
            "validationErrors=" + validationErrors +
            '}';
    }
}
