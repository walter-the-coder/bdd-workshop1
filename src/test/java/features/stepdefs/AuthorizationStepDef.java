package features.stepdefs;

import java.util.List;
import java.util.stream.Collectors;

import com.bdd.workshop.type.OrganisationNumber;
import com.bdd.workshop.type.TaxpayerIdentificationNumber;

import features.simulator.AuthorizationServerSimulator;
import io.cucumber.java.en.Given;

public class AuthorizationStepDef {

    @Given("user with TIN {string} is authorized for the following organisations")
    public void userWithPersonIdAndOrganisationNumberIsAuthorized(String taxpayerIdentificationNumber,
        List<String> organisations) {
        AuthorizationServerSimulator.getInstance()
            .addAuthorizedOrganisations(
                new TaxpayerIdentificationNumber(taxpayerIdentificationNumber),
                organisations.stream().map(OrganisationNumber::new).collect(Collectors.toList())
            );
    }

}
