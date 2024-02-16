package com.bdd.workshop.controller.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VATLine {
    private VATCode vatCode;
    private double amount;

    public VATLine() {
        this.vatCode = null;
        this.amount = 0.0;
    }

    public VATCode getVatCode() {
        return vatCode;
    }

    public void setVatCode(VATCode vatCode) {
        this.vatCode = vatCode;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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
            "vatCode=" + vatCode +
            ", amount=" + amount +
            '}';
    }

    public static VATLine.Builder with() {
        return new VATLine.Builder();
    }

    public static final class Builder {
        private final VATLine dto = new VATLine();

        public VATLine.Builder withVATCode(VATCode vatCode) {
            dto.vatCode = vatCode;
            return this;
        }

        public VATLine.Builder withAmount(double amount) {
            dto.amount = amount;
            return this;
        }

        public VATLine build() {
            return dto;
        }
    }
}