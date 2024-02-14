package com.bdd.workshop.controller.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VATLine {
    private final VATCode vatCode;
    private final double amount;

    public VATLine() {
        this.vatCode = null;
        this.amount = 0.0;
    }

    public VATLine(Integer vatCode, double amount) {
        this.vatCode = VATCode.fromCode(vatCode);
        this.amount = amount;
    }

    public VATCode getVatCode() {
        return vatCode;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        VATLine vatLine = (VATLine) object;
        return Double.compare(amount, vatLine.amount) == 0 && vatCode == vatLine.vatCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vatCode, amount);
    }

    @Override
    public String toString() {
        return "VATLine{" +
            "vatCode='" + vatCode.name() + '\'' +
            ", amount=" + amount +
            '}';
    }
}