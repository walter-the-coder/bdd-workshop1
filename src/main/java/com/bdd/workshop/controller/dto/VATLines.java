package com.bdd.workshop.controller.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VATLines {
    private List<VATLine> vatLines;

    public VATLines() {
        this.vatLines = new ArrayList<>();
    }

    public List<VATLine> getVatLines() {
        return vatLines;
    }

    public void setVatLines(List<VATLine> vatLines) {
        this.vatLines = vatLines;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        VATLines vatLines1 = (VATLines) object;
        return Objects.equals(vatLines, vatLines1.vatLines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vatLines);
    }

    @Override
    public String toString() {
        return "VATLines{" +
            "vatLines=" + vatLines +
            '}';
    }

    public static VATLines.Builder with() {
        return new VATLines.Builder();
    }

    public static final class Builder {
        private final VATLines dto = new VATLines();

        public VATLines.Builder withVATLines(List<VATLine> vatLines) {
            dto.vatLines = vatLines;
            return this;
        }

        public VATLines build() {
            return dto;
        }
    }
}
