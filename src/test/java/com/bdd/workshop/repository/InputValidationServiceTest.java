package com.bdd.workshop.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.bdd.workshop.controller.dto.ReceptionDto;
import com.bdd.workshop.controller.dto.VATLines;
import com.bdd.workshop.controller.dto.ValidationResponse;
import com.bdd.workshop.service.InputValidationService;
import com.bdd.workshop.type.OrganisationNumber;
import com.bdd.workshop.type.TaxCategory;
import com.bdd.workshop.type.TaxationPeriodType;
import com.bdd.workshop.type.TaxpayerIdentificationNumber;

public class InputValidationServiceTest {
    private InputValidationService inputValidationService = new InputValidationService();

    @Test
    void test_validate_valid_year() {
        ReceptionDto data = synthesizeWithYear(2024);
        ValidationResponse response = inputValidationService.validate(data);
        Assertions.assertTrue(response.getValidationErrors().isEmpty());
    }

    @Test
    void test_validate_invalid_year() {
        List<Integer> boundaryValues = new ArrayList<>();
        boundaryValues.add(2023);
        boundaryValues.add(2025);
        boundaryValues.add(null);

        boundaryValues.forEach(year -> {
            ReceptionDto data = synthesizeWithYear(year);
            ValidationResponse response = inputValidationService.validate(data);
            Assertions.assertEquals(1, response.getValidationErrors().size());
        });
    }

    private ReceptionDto synthesizeWithYear(Integer year) {
        return ReceptionDto.with()
            .withOrganisationNumber(new OrganisationNumber("123456789"))
            .withSubmitterId(new TaxpayerIdentificationNumber("01012012345"))
            .withCategory(TaxCategory.NORMAL)
            .withYear(year)
            .withTaxationPeriodType(TaxationPeriodType.JAN_FEB)
            .withTimeOfSubmission(LocalDate.of(2024, 1, 1).atStartOfDay())
            .withVatLines(VATLines.with().build())
            .build();
    }
}
