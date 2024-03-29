package features.stepdefs;

import org.junit.Assert;
import org.springframework.http.ResponseEntity;

import com.bdd.workshop.controller.dto.RetrievingDto;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;

public class RetrieveingStepDef {
    @When("we should be able to get the following unprocessed data from the application")
    public void getFollowingUnprocessedData(DataTable dataTable) {
        ResponseEntity<RetrievingDto> unprocessedData = LoginStepDep.restTemplate()
            .getForEntity(
                "/api/retrieving/unprocessed",
                RetrievingDto.class
            );

        Assert.assertTrue(unprocessedData.getStatusCode().is2xxSuccessful());
        // TODO: Workshop Case 4: Implement the test asserts here.
        // You should use a loop to ensure that all list elements are controlled.
        Assert.fail();
    }
}
