package com.bdd.workshop.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bdd.workshop.controller.dto.ReceptionDto;
import com.bdd.workshop.type.TaxationPeriodType;

@Service
public class InputValidationService {

    public Map<String, String> validate(ReceptionDto data) {
        Map<String, String> validationErrors = new HashMap<>();

        if (data.year() != 2024) {
            validationErrors.put("year", "Invalid year: " + data.year() + ". "
                + "We only allow submissions for year 2024 at the moment");
        }

        if (data.taxationPeriodType() != TaxationPeriodType.JAN_FEB) {
            validationErrors.put("taxationPeriodType", "Invalid taxation period type: " + data.taxationPeriodType()
                + ". We only allow submissions of the taxation period january-february at the moment.");
        }

        return validationErrors;
    }
}
