package features.stepdefs;

import java.util.Map;

import org.junit.Assert;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class UserNameStepDef {
    private final RestTemplate restTemplate;
    private ResponseEntity<Boolean> respons;

    public UserNameStepDef(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @When("the user logs in with name")
    public void the_user_logs_in_with_name(Map<String, String> dataTable) {
        String firstname = dataTable.get("firstname");
        String lastname = dataTable.get("lastname");
        String fullname = firstname + " " + lastname;

        respons = restTemplate.getForEntity(
            "/api/reception/validateName/" + fullname,
            Boolean.class
        );
    }

    @Then("the server should confirm that the user exists")
    public void the_server_should_confirm_that_the_user_exists() {
        Assert.assertTrue(respons.getStatusCode().is2xxSuccessful());
        Assert.assertNotNull(respons.getBody());
        Assert.assertTrue(respons.getBody());
    }
}
