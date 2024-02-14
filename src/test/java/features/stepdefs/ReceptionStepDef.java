package features.stepdefs;

import java.util.Map;

import org.junit.Assert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.bdd.workshop.controller.dto.ReceptionDto;
import com.bdd.workshop.controller.dto.ReceptionResponse;
import com.bdd.workshop.controller.dto.ValidationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import features.stepdefs.util.ReceptionDtoUtil;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ReceptionStepDef {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private ResponseEntity<ValidationResponse> validationResponse;
    private ResponseEntity<ReceptionResponse> submitResponse;
    private HttpStatusCodeException exception;

    public ReceptionStepDef(
        RestTemplate restTemplate,
        ObjectMapper objectMapper
    ) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @When("the following data is validated")
    public void the_VAT_report_is_validated(Map<String, String> dataTable) {
        ReceptionDto vatReport = ReceptionDtoUtil.getReceptionDto(dataTable);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ReceptionDto> request = new HttpEntity<>(
                vatReport,
                headers
            );

            validationResponse = restTemplate.postForEntity(
                "/api/reception/validate",
                request,
                ValidationResponse.class
            );
        } catch (HttpStatusCodeException e) {
            exception = e;
        }
    }

    @When("the following data is submitted")
    public void the_VAT_report_is_submitted(Map<String, String> dataTable) {
        ReceptionDto vatReport = ReceptionDtoUtil.getReceptionDto(dataTable);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ReceptionDto> request = new HttpEntity<>(
                vatReport,
                headers
            );

            submitResponse = restTemplate.postForEntity(
                "/api/reception/submit",
                request,
                ReceptionResponse.class
            );
        } catch (HttpStatusCodeException e) {
            exception = e;
        }
    }

    @Then("the response should return with status code OK")
    public void the_response_should_be_ok() {
        Assert.assertNull(exception);
        Assert.assertTrue(submitResponse.getStatusCode().is2xxSuccessful());
    }

    @And("the reciept message should be {string}")
    public void the_reciept_message_should_be(String message) {
        Assert.assertNotNull(submitResponse);
        ReceptionResponse receptionResponse = submitResponse.getBody();
        Assert.assertNotNull(receptionResponse);
        String recieptMessage = receptionResponse.getMessage();
        Assert.assertNotNull(recieptMessage);
        Assert.assertEquals(message, recieptMessage);
    }

    @Then("the response should return OK with the following validation errors")
    public void the_response_should_return_with_the_following_validation_erros(Map<String, String> dataTable) {
        Assert.assertNull(exception);
        Assert.assertNotNull(validationResponse);
        Assert.assertTrue(validationResponse.getStatusCode().is2xxSuccessful());
        ValidationResponse body = validationResponse.getBody();
        Assert.assertNotNull(body);
        Map<String, String> validationErrors = body.getValidationErrors();
        Assert.assertEquals(dataTable, validationErrors);
    }

    @Then("the response should be the following error")
    public void the_response_should_be_the_following_error(Map<String, String> dataTable) {
        Assert.assertNull(submitResponse);
        Assert.assertEquals(Integer.parseInt(dataTable.get("statusCode")), exception.getStatusCode().value());
    }
}
