package features.stepdefs;

import java.util.List;
import java.util.stream.Collectors;

import com.bdd.workshop.type.OrganisationNumber;
import com.bdd.workshop.type.PersonId;

import features.simulator.AuthorizationServerSimulator;
import io.cucumber.java.en.Given;

public class AuthorizationStepDef {

    @Given("user with person id {string} is authorized for the following organisations")
    public void userWithPersonIdAndOrganisationNumberIsAuthorized(String personId, List<String> organisations) {
        AuthorizationServerSimulator.getInstance()
            .addAuthorizedOrganisations(
                new PersonId(personId),
                organisations.stream().map(OrganisationNumber::new).collect(Collectors.toList())
            );
    }

}
