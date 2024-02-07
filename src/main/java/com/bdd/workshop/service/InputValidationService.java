package com.bdd.workshop.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bdd.workshop.controller.dto.ReceptionDto;
import com.bdd.workshop.controller.dto.VATLine;
import com.bdd.workshop.controller.dto.VATLines;
import com.bdd.workshop.controller.dto.ValidationResponse;
import com.bdd.workshop.exceptionHandling.CustomRuntimeException;
import com.bdd.workshop.type.TaxationPeriodType;

@Service
public class InputValidationService {

    public ValidationResponse validate(ReceptionDto data) {
        Map<String, String> validationErrors = new HashMap<>();

        validateYear(data.year(), validationErrors);

        validateTaxationPeriodType(data.taxationPeriodType(), validationErrors);

        validateVATLines(data.vatLines(), validationErrors);

        return new ValidationResponse(validationErrors);
    }

    public void validateOrThrowError(ReceptionDto data) {
        ValidationResponse validationResponse = validate(data);

        if (!validationResponse.validationErrors().isEmpty()) {
            String errorMessage = StringUtils.joinWith(":", validationResponse.validationErrors().values())
                .replace("[", "")
                .replace("]", "");

            throw new CustomRuntimeException(
                "BAD_REQUEST",
                errorMessage,
                null,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    public void validateYear(Integer year, Map<String, String> validationErrors) {
        if (year != 2024) {
            validationErrors.put("year", "Invalid year: " + year + ". "
                + "We only allow submissions for year 2024 at the moment");
        }
    }

    public void validateTaxationPeriodType(
        TaxationPeriodType taxationPeriodType,
        Map<String, String> validationErrors
    ) {
        if (taxationPeriodType != TaxationPeriodType.JAN_FEB) {
            validationErrors.put("taxationPeriodType", "Invalid taxation period type: " + taxationPeriodType
                + ". We only allow submissions of the taxation period january-february at the moment.");
        }
    }

    public void validateVATLines(VATLines vatLines, Map<String, String> validationErrors) {
        for (VATLine vatLine : vatLines.vatLines()) {
            validateVATLine(vatLine, validationErrors);
        }
    }

    public void validateVATLine(VATLine vatLine, Map<String, String> validationErrors) {
        switch (vatLine.vatCode()) {
        case VAT_CODE_1, VAT_CODE_2, VAT_CODE_8, VAT_CODE_9, VAT_CODE_10 -> {
            if (vatLine.amount() > 0) {
                validationErrors.put("vatLine",
                    "Invalid VAT amount for VAT code " + vatLine.vatCode()
                        + ". The amount should be zero or negative!");
            }
        }
        case VAT_CODE_3, VAT_CODE_4, VAT_CODE_5, VAT_CODE_6, VAT_CODE_7 -> {
            if (vatLine.amount() < 0) {
                validationErrors.put("vatLine",
                    "Invalid VAT amount for VAT code " + vatLine.vatCode()
                        + ". The amount should be zero or positive!");
            }
        }
        }
    }
}
