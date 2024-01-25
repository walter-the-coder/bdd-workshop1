package com.bdd.workshop.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.bdd.workshop.controller.dto.ReceptionDto;
import com.bdd.workshop.controller.dto.ReceptionResponse;
import com.bdd.workshop.repository.TransactionRepository;

@Service
public class ReceptionService {

    private final AuthorizationService authorizationService;
    private final InputValidationService inputValidationService;
    private final TransactionRepository transactionRepository;

    public ReceptionService(
        AuthorizationService authorizationService,
        InputValidationService inputValidationService,
        TransactionRepository transactionRepository
    ) {
        this.authorizationService = authorizationService;
        this.inputValidationService = inputValidationService;
        this.transactionRepository = transactionRepository;
    }

    public ReceptionResponse handleReceivedData(ReceptionDto data) {
        authorizationService.controlUserAccessToOrganisation(data.submitterId(), data.organisationNumber());

        Map<String, String> validationErrors = inputValidationService.validate(data);
        if (!validationErrors.isEmpty()) {
            return new ReceptionResponse(validationErrors);
        }

        transactionRepository.storeReceivedData(data);

        return new ReceptionResponse(data.organisationNumber());
    }
}
