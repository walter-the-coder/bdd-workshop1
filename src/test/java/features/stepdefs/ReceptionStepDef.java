package features.stepdefs;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import org.junit.Assert;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import com.bdd.workshop.controller.dto.ReceptionDto;
import com.bdd.workshop.exceptionHandling.ErrorResponse;
import features.stepdefs.util.ReceptionDtoUtil;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ReceptionStepDef {
    private final RestClient restClient;
    private ResponseEntity<Void> response;
    private HttpStatusCodeException exception;

    public ReceptionStepDef(
        RestClient restClient
    ) {
        this.restClient = restClient;
    }

    @When("the following data is submitted")
    public void the_following_data_is_received(Map<String, String> dataTable) {
        ReceptionDto data = ReceptionDtoUtil.getReceptionDto(dataTable);
        try {
            response = restClient.post()
                .uri("/api/reception/")
                .body(data)
                .retrieve()
                .toBodilessEntity();
        } catch (HttpStatusCodeException e) {
            exception = e;
        }
    }

    @Then("the response should return with status code OK")
    public void the_response_should_be_ok() {
        Assert.assertNull(exception);
        Assert.assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Then("the response should be the following error")
    public void the_response_should_be_the_following_error(Map<String, String> dataTable) throws IOException {
        Assert.assertNull(response);
        Assert.assertEquals(Integer.parseInt(dataTable.get("statusCode")), exception.getStatusCode().value());
        ErrorResponse errorResponse = exception.getResponseBodyAs(ErrorResponse.class);
        Objects.requireNonNull(errorResponse);
        Assert.assertEquals(dataTable.get("errorCode"), errorResponse.getErrorCode());
        Assert.assertEquals(dataTable.get("errorMessage"), errorResponse.getErrorMessage());
    }
}
