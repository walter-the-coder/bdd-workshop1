package com.bdd.workshop.client.dto;

import java.util.Objects;

import com.bdd.workshop.type.TaxpayerIdentificationNumber;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizationRequest {
    private TaxpayerIdentificationNumber TaxpayerIdentificationNumber;

    public AuthorizationRequest() {
        TaxpayerIdentificationNumber = null;
    }

    public AuthorizationRequest(TaxpayerIdentificationNumber TaxpayerIdentificationNumber) {
        this.TaxpayerIdentificationNumber = TaxpayerIdentificationNumber;
    }

    public TaxpayerIdentificationNumber getPersonId() {
        return TaxpayerIdentificationNumber;
    }

    public void setPersonId(TaxpayerIdentificationNumber TaxpayerIdentificationNumber) {
        this.TaxpayerIdentificationNumber = TaxpayerIdentificationNumber;
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
        return Objects.equals(TaxpayerIdentificationNumber, that.TaxpayerIdentificationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(TaxpayerIdentificationNumber);
    }

    @Override
    public String toString() {
        return "AuthorizationRequest{" +
            "personId=" + TaxpayerIdentificationNumber +
            '}';
    }
}
