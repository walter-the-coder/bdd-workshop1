package com.bdd.workshop.repository;

import com.bdd.workshop.client.AuthorizationClient;
import com.bdd.workshop.exceptionHandling.CustomRuntimeException;
import com.bdd.workshop.service.AuthorizationService;
import com.bdd.workshop.type.OrganisationNumber;
import com.bdd.workshop.type.TaxpayerIdentificationNumber;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AuthorizationServiceTest {
    private AuthorizationClient authorizationClient = Mockito.mock(AuthorizationClient.class);

    @Test
    public void should_succeed_when_right_TIN_and_organization_number_is_used() {
        // GIVEN
        OrganisationNumber organisationNumber = new OrganisationNumber("987654321");
        TaxpayerIdentificationNumber taxpayerIdentificationNumber = new TaxpayerIdentificationNumber("123456789");
        AuthorizationService authorizationService = new AuthorizationService(authorizationClient, true);

        Mockito.when(authorizationClient.hasAccessToOrganisations(
                taxpayerIdentificationNumber
        )).thenReturn(
                new ArrayList() {{
                    add(organisationNumber);
                }}
        );
        // WHEN
        assertDoesNotThrow(() -> {
            authorizationService.controlUserAccessToOrganisation(
                    taxpayerIdentificationNumber,
                    organisationNumber);
        });
        
    }


    @Test()
    public void should_fail_when_organization_number_dont_match() {
        //GIVEN
        OrganisationNumber organisationNumber = new OrganisationNumber("987654321");
        TaxpayerIdentificationNumber taxpayerIdentificationNumber = new TaxpayerIdentificationNumber("1234567890");
        AuthorizationService authorizationService = new AuthorizationService(authorizationClient, true);

        Mockito.when(authorizationClient.hasAccessToOrganisations(taxpayerIdentificationNumber))
                .thenReturn(
                        new ArrayList() {{
                            add(new OrganisationNumber("999"));
                        }}
                );
        //THEN
        assertThrows(CustomRuntimeException.class, () -> {
            authorizationService.controlUserAccessToOrganisation(
                    taxpayerIdentificationNumber,
                    organisationNumber
            );
        });
        assert (true);
    }
}
