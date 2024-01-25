package com.bdd.workshop.service;

import org.springframework.stereotype.Service;

import com.bdd.workshop.controller.dto.ReceptionDto;
import com.bdd.workshop.repository.TransactionRepository;

@Service
public class ReceptionService {

    private final AuthorizationService authorizationService;
    private final TransactionRepository transactionRepository;

    public ReceptionService(
        AuthorizationService authorizationService,
        TransactionRepository transactionRepository
    ) {
        this.authorizationService = authorizationService;
        this.transactionRepository = transactionRepository;
    }

    public void handleReceivedData(ReceptionDto data) {
        authorizationService.controlUserAccessToOrganisation(data.submitterId(), data.organisationNumber());

        transactionRepository.storeReceivedData(data);
    }
}
