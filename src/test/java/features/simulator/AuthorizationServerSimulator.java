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
import com.bdd.workshop.type.PersonId;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.http.Request;

import features.simulator.common.Simulator;

public final class AuthorizationServerSimulator extends Simulator {
    private static AuthorizationServerSimulator INSTANCE;

    public AuthorizationServerSimulator() {
        super("authorization-server", false);
    }

    public static AuthorizationServerSimulator getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new AuthorizationServerSimulator();
        }

        return INSTANCE;
    }

    @Override
    public Map<Pattern, Function<Request, ResponseDefinitionBuilder>> postMapping() {
        return Map.of(
            Pattern.compile("/authorization"),
            this::getAuthorizedOrganisations
        );
    }

    private final Map<PersonId, List<OrganisationNumber>> authorizedOrganisations =
        Collections.synchronizedMap(new HashMap<>());

    @Override
    public void reset() {
        authorizedOrganisations.clear();
    }

    public void addAuthorizedOrganisations(PersonId personId, List<OrganisationNumber> organisations) {
        authorizedOrganisations.put(personId, organisations);
    }

    public ResponseDefinitionBuilder getAuthorizedOrganisations(Request request) {
        PersonId personId = readJson(request, AuthorizationRequest.class).personId();
        AuthorizationResponse response = new AuthorizationResponse(
            authorizedOrganisations.get(personId)
        );

        return ResponseDefinitionBuilder.responseDefinition()
            .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .withStatus(HttpStatus.OK.value())
            .withBody(writeJson(response));
    }
}
