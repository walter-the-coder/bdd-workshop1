package com.bdd.workshop.controller.dto;

import java.time.LocalDateTime;
import java.util.Objects;

import com.bdd.workshop.type.OrganisationNumber;
import com.bdd.workshop.type.TaxCategory;
import com.bdd.workshop.type.TaxationPeriodType;
import com.bdd.workshop.type.TaxpayerIdentificationNumber;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceptionDto {
    private OrganisationNumber organisationNumber;
    private TaxpayerIdentificationNumber submitterId;
    private TaxCategory category;
    private Integer year;
    private TaxationPeriodType taxationPeriodType;
    private LocalDateTime timeOfSubmission;
    private VATLines vatLines;

    public OrganisationNumber getOrganisationNumber() {
        return organisationNumber;
    }

    public void setOrganisationNumber(OrganisationNumber organisationNumber) {
        this.organisationNumber = organisationNumber;
    }

    public TaxpayerIdentificationNumber getSubmitterId() {
        return submitterId;
    }

    public void setSubmitterId(TaxpayerIdentificationNumber submitterId) {
        this.submitterId = submitterId;
    }

    public TaxCategory getCategory() {
        return category;
    }

    public void setCategory(TaxCategory category) {
        this.category = category;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public TaxationPeriodType getTaxationPeriodType() {
        return taxationPeriodType;
    }

    public void setTaxationPeriodType(TaxationPeriodType taxationPeriodType) {
        this.taxationPeriodType = taxationPeriodType;
    }

    public LocalDateTime getTimeOfSubmission() {
        return timeOfSubmission;
    }

    public void setTimeOfSubmission(LocalDateTime timeOfSubmission) {
        this.timeOfSubmission = timeOfSubmission;
    }

    public VATLines getVatLines() {
        return vatLines;
    }

    public void setVatLines(VATLines vatLines) {
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
        ReceptionDto that = (ReceptionDto) object;
        return Objects.equals(organisationNumber, that.organisationNumber) && Objects.equals(
            submitterId, that.submitterId) && category == that.category && Objects.equals(year, that.year)
            && taxationPeriodType == that.taxationPeriodType && Objects.equals(timeOfSubmission,
            that.timeOfSubmission) && Objects.equals(vatLines, that.vatLines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organisationNumber, submitterId, category, year, taxationPeriodType, timeOfSubmission,
            vatLines);
    }

    @Override
    public String toString() {
        return "ReceptionDto{" +
            "organisationNumber=" + organisationNumber +
            ", submitterId=" + submitterId +
            ", category=" + category +
            ", year=" + year +
            ", taxationPeriodType=" + taxationPeriodType +
            ", timeOfSubmission=" + timeOfSubmission +
            ", vatLines=" + vatLines +
            '}';
    }

    public static ReceptionDto.Builder with() {
        return new ReceptionDto.Builder();
    }

    public static final class Builder {
        private final ReceptionDto dto = new ReceptionDto();

        public Builder withOrganisationNumber(OrganisationNumber organisationNumber) {
            dto.organisationNumber = organisationNumber;
            return this;
        }

        public Builder withSubmitterId(TaxpayerIdentificationNumber submitterId) {
            dto.submitterId = submitterId;
            return this;
        }

        public Builder withCategory(TaxCategory category) {
            dto.category = category;
            return this;
        }

        public Builder withYear(Integer year) {
            dto.year = year;
            return this;
        }

        public Builder withTaxationPeriodType(TaxationPeriodType taxationPeriodType) {
            dto.taxationPeriodType = taxationPeriodType;
            return this;
        }

        public Builder withTimeOfSubmission(LocalDateTime timeOfSubmission) {
            dto.timeOfSubmission = timeOfSubmission;
            return this;
        }

        public Builder withVatLines(VATLines vatLines) {
            dto.vatLines = vatLines;
            return this;
        }

        public ReceptionDto build() {
            return dto;
        }
    }
}
