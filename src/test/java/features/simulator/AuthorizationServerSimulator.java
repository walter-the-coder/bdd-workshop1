package features.simulator;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.bdd.workshop.client.dto.AuthorizationRequest;
import com.bdd.workshop.client.dto.AuthorizationResponse;
import com.bdd.workshop.type.OrganisationNumber;
import com.bdd.workshop.type.TaxpayerIdentificationNumber;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.http.Request;

import features.simulator.common.Simulator;

public final class AuthorizationServerSimulator extends Simulator {
    private static AuthorizationServerSimulator INSTANCE;

    public AuthorizationServerSimulator() {
        super("authorization-server", false);
    }

    public static AuthorizationServerSimulator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AuthorizationServerSimulator();
        }

        return INSTANCE;
    }

    @Override
    public Map<Pattern, Function<Request, ResponseDefinitionBuilder>> postMapping() {
        Map<Pattern, Function<Request, ResponseDefinitionBuilder>> postMappings = new HashMap<>();
        postMappings.put(Pattern.compile("/authorization"), this::getAuthorizedOrganisations);
        return postMappings;
    }

    private final Map<TaxpayerIdentificationNumber, List<OrganisationNumber>> authorizedOrganisations =
        Collections.synchronizedMap(new HashMap<>());

    @Override
    public void reset() {
        authorizedOrganisations.clear();
    }

    public void addAuthorizedOrganisations(TaxpayerIdentificationNumber TaxpayerIdentificationNumber,
        List<OrganisationNumber> organisations) {
        authorizedOrganisations.put(TaxpayerIdentificationNumber, organisations);
    }

    public ResponseDefinitionBuilder getAuthorizedOrganisations(Request request) {
        TaxpayerIdentificationNumber TaxpayerIdentificationNumber =
            readJson(request, AuthorizationRequest.class).getPersonId();
        AuthorizationResponse response = new AuthorizationResponse(
            authorizedOrganisations.get(TaxpayerIdentificationNumber)
        );

        return ResponseDefinitionBuilder.responseDefinition()
            .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .withStatus(HttpStatus.OK.value())
            .withBody(writeJson(response));
    }
}
