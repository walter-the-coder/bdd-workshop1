package com.bdd.workshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bdd.workshop.client.AuthorizationClient;
import com.bdd.workshop.exceptionHandling.CustomRuntimeException;
import com.bdd.workshop.type.OrganisationNumber;
import com.bdd.workshop.type.TaxpayerIdentificationNumber;

@Service
public class AuthorizationService {

    private final AuthorizationClient authorizationClient;
    private final Boolean securityEnabled;

    public AuthorizationService(
        AuthorizationClient authorizationClient,
        @Value("${security.enabled}") Boolean securityEnabled
    ) {
        this.authorizationClient = authorizationClient;
        this.securityEnabled = securityEnabled;
    }

    public void controlUserAccessToOrganisation(
        TaxpayerIdentificationNumber TaxpayerIdentificationNumber,
        OrganisationNumber organisationNumber
    ) {
        if (!securityEnabled) {
            return;
        }

        List<OrganisationNumber> usersOrganisations = authorizationClient.hasAccessToOrganisations(
            TaxpayerIdentificationNumber);
        if (!usersOrganisations.contains(organisationNumber)) {
            throw new CustomRuntimeException(
                "NOT_AUTHORIZED",
                "User with id " + TaxpayerIdentificationNumber + " is not authorized to access organisation " + organisationNumber,
                null,
                HttpStatus.UNAUTHORIZED
            );
        }
    }
}
