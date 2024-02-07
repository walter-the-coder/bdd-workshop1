package features.stepdefs;

import java.util.Map;
import java.util.Objects;

import org.junit.Assert;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import com.bdd.workshop.controller.dto.ReceptionDto;
import com.bdd.workshop.controller.dto.ReceptionResponse;
import com.bdd.workshop.controller.dto.ValidationResponse;
import com.bdd.workshop.exceptionHandling.ErrorResponse;

import features.stepdefs.util.ReceptionDtoUtil;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ReceptionStepDef {
    private final RestClient restClient;
    private ResponseEntity<ValidationResponse> validationResponse;
    private ResponseEntity<ReceptionResponse> submitResponse;
    private HttpStatusCodeException exception;

    public ReceptionStepDef(
        RestClient restClient
    ) {
        this.restClient = restClient;
    }

    @When("the following data is validated")
    public void the_VAT_report_is_validated(Map<String, String> dataTable) {
        ReceptionDto vatReport = ReceptionDtoUtil.getReceptionDto(dataTable);
        try {
            validationResponse = restClient.post()
                .uri("/api/reception/validate")
                .body(vatReport)
                .retrieve()
                .toEntity(ValidationResponse.class);
        } catch (HttpStatusCodeException e) {
            exception = e;
        }
    }

    @When("the following data is submitted")
    public void the_VAT_report_is_submitted(Map<String, String> dataTable) {
        ReceptionDto vatReport = ReceptionDtoUtil.getReceptionDto(dataTable);
        try {
            submitResponse = restClient.post()
                .uri("/api/reception/submit")
                .body(vatReport)
                .retrieve()
                .toEntity(ReceptionResponse.class);
        } catch (HttpStatusCodeException e) {
            exception = e;
        }
    }

    @Then("the response should return with status code OK")
    public void the_response_should_be_ok() {
        Assert.assertNull(exception);
        Assert.assertTrue(submitResponse.getStatusCode().is2xxSuccessful());
    }

    @Then("the response should return OK with the following validation errors")
    public void the_response_should_return_with_the_following_validation_erros(Map<String, String> dataTable) {
        Assert.assertNull(exception);
        Assert.assertNotNull(validationResponse);
        Assert.assertTrue(validationResponse.getStatusCode().is2xxSuccessful());
        ValidationResponse body = validationResponse.getBody();
        Assert.assertNotNull(body);
        Map<String, String> validationErrors = body.validationErrors();
        Assert.assertEquals(dataTable, validationErrors);
    }

    @Then("the response should be the following error")
    public void the_response_should_be_the_following_error(Map<String, String> dataTable) {
        Assert.assertNull(submitResponse);
        Assert.assertEquals(Integer.parseInt(dataTable.get("statusCode")), exception.getStatusCode().value());
        ErrorResponse errorResponse = exception.getResponseBodyAs(ErrorResponse.class);
        Objects.requireNonNull(errorResponse);
        Assert.assertEquals(dataTable.get("errorCode"), errorResponse.getErrorCode());
        Assert.assertEquals(dataTable.get("errorMessage"), errorResponse.getErrorMessage());
    }
}
