package features.simulator.common;

import static wiremock.org.apache.commons.lang3.StringUtils.substringBefore;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformerV2;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FunctionMapResponseTransformer implements ResponseDefinitionTransformerV2 {
    private static final Logger LOGGER = LoggerFactory.getLogger(FunctionMapResponseTransformer.class);
    private final Simulator simulator;
    private final Map<RequestMethod, Map<Pattern, Function<Request, ResponseDefinitionBuilder>>> restMapping;

    public FunctionMapResponseTransformer(
        Simulator simulator,
        Map<RequestMethod, Map<Pattern, Function<Request, ResponseDefinitionBuilder>>> restMapping
    ) {
        this.simulator = simulator;
        this.restMapping = restMapping;
    }

    @Override
    public boolean applyGlobally() {
        return false;
    }

    @Override
    public ResponseDefinition transform(ServeEvent event) {
        return transform(event.getRequest());
    }

    private ResponseDefinition transform(Request request) {
        if (simulator.isSimulatedDowntime()) {
            return ResponseDefinitionBuilder.responseDefinition()
                .withFault(Fault.CONNECTION_RESET_BY_PEER)
                .build();
        }

        String urlWithoutQueryParams = substringBefore(request.getUrl(), '?');

        Map<Pattern, Function<Request, ResponseDefinitionBuilder>> mapping = restMapping.get(request.getMethod());
        if (mapping == null) {
            mapping = Collections.emptyMap();
        }

        Map<Pattern, Function<Request, ResponseDefinitionBuilder>> finalMapping = mapping;
        Map.Entry<Pattern, Function<Request, ResponseDefinitionBuilder>> endpoint = mapping.entrySet().stream()
            .filter(entry -> entry.getKey().matcher(urlWithoutQueryParams).matches())
            .findFirst()
            .orElseGet(() -> finalMapping.entrySet().stream()
                .filter(entry -> entry.getKey().matcher(urlWithoutQueryParams + "/*").matches())
                .findFirst()
                .orElse(null));

        if (endpoint != null) {
            try {
                Function<Request, ResponseDefinitionBuilder> value = endpoint.getValue();
                return value.apply(request).build();
            } catch (Exception ex) {
                LOGGER.error("Exception {} thrown inside the simulator: {} ({})\n" +
                        "at endpoint {} - {} ({})",
                    ex.getClass().getName(),
                    ex.getClass().getName(),
                    ex.getMessage(),
                    request.getMethod(),
                    endpoint.getKey(),
                    request.getUrl());

                return ResponseDefinitionBuilder.responseDefinition()
                    .withHeader(CONTENT_TYPE_HEADER, "text/plain")
                    .withStatus(HTTP_STATUS_INTERNAL_SERVER_ERROR)
                    .withStatusMessage("Simulator error")
                    .withBody("Unhandeled exception in the simulator:\n" + ex)
                    .build();
            }
        }

        return ResponseDefinitionBuilder.responseDefinition()
            .withStatus(HTTP_STATUS_METHOD_NOT_ALLOWED)
            .withBody(
                "\nSimulator " + simulator.name + " did not find any endpoints for: " +
                    request.getMethod() + " " + request.getUrl() + "\n" +
                    "------------------------------------------------------\n" +
                    "REGISTERED MAPPINGS IN THE SIMULATOR:\n" +
                    restMapping.entrySet().stream()
                        .filter(entry -> !entry.getValue().isEmpty())
                        .flatMap(entry -> entry.getValue().keySet().stream()
                            .map(key -> entry.getKey() + " " + key + "\n"))
                        .collect(Collectors.joining()) +
                    "------------------------------------------------------\n"
            )
            .build();
    }

    @Override
    public String getName() {
        return simulator.name + "ResponseTransformer";
    }

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final Integer HTTP_STATUS_INTERNAL_SERVER_ERROR = 500;
    private static final Integer HTTP_STATUS_METHOD_NOT_ALLOWED = 405;
}
