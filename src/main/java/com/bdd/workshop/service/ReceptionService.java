package com.bdd.workshop.service;

import java.util.Map;

import com.bdd.workshop.exceptionHandling.CustomRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
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
            String errorMessage = StringUtils.joinWith(":",validationErrors.values())
                    .replace("[","")
                    .replace("]","");
            throw new CustomRuntimeException(
                    "BAD_REQUEST",
                    errorMessage,
                    null,
                    HttpStatus.BAD_REQUEST
            );
        }

        transactionRepository.storeReceivedData(data);

        return new ReceptionResponse(data.organisationNumber());
    }
}
