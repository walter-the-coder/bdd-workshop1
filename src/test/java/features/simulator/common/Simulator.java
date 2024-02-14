package features.simulator.common;

import static com.bdd.workshop.ApplicationConfig.DEFAULT_OBJECT_MAPPER;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.RequestMethod;

public abstract class Simulator {
    private static final Logger LOGGER = LoggerFactory.getLogger(Simulator.class);

    public Map<Pattern, Function<Request, ResponseDefinitionBuilder>> getMapping() {
        return new HashMap<>();
    }

    public Map<Pattern, Function<Request, ResponseDefinitionBuilder>> postMapping() {
        return new HashMap<>();
    }

    public Map<Pattern, Function<Request, ResponseDefinitionBuilder>> putMapping() {
        return new HashMap<>();
    }

    public Map<Pattern, Function<Request, ResponseDefinitionBuilder>> deleteMapping() {
        return new HashMap<>();
    }

    protected static ObjectMapper objectMapper = DEFAULT_OBJECT_MAPPER;

    protected WireMockServer wireMockServer;

    protected String name;

    protected boolean verbose;
    protected boolean simulateDowntime = false;

    public boolean isSimulatedDowntime() {
        return simulateDowntime;
    }

    public void setSimulateDowntime(boolean simulateDowntime) {
        this.simulateDowntime = simulateDowntime;
    }

    public Simulator(String name, Boolean verbose) {
        this.name = name;
        this.verbose = verbose;
        long startupTime = System.currentTimeMillis();
        if (SimulatorFactory.simulatorInstances.contains(this)) {
            throw new RuntimeException("Simulator already exists");
        }

        Map<RequestMethod, Map<Pattern, Function<Request, ResponseDefinitionBuilder>>> restMapping = new HashMap<>();
        restMapping.put(RequestMethod.GET, getMapping());
        restMapping.put(RequestMethod.POST, postMapping());
        restMapping.put(RequestMethod.PUT, putMapping());
        restMapping.put(RequestMethod.DELETE, deleteMapping());

        Map<RequestMethod, Map<Pattern, Function<Request, ResponseDefinitionBuilder>>> mappedMappings = new HashMap<>();

        for (Map.Entry<RequestMethod, Map<Pattern, Function<Request, ResponseDefinitionBuilder>>> entry :
            restMapping.entrySet()) {
            RequestMethod method = entry.getKey();
            Map<Pattern, Function<Request, ResponseDefinitionBuilder>> mappings = entry.getValue();
            Map<Pattern, Function<Request, ResponseDefinitionBuilder>> mapped = new HashMap<>();

            for (Map.Entry<Pattern, Function<Request, ResponseDefinitionBuilder>> mapping : mappings.entrySet()) {
                String patternString = mapping.getKey().pattern();
                if (patternString.contains("?")) {
                    throw new RuntimeException(
                        "Invalid regex in URL matcher. Don't use query params in pattern matchers!");
                }

                Pattern pattern = Pattern.compile(patternString);
                mapped.put(pattern, mapping.getValue());
            }

            mappedMappings.put(method, mapped);
        }

        FunctionMapResponseTransformer transformer =
            new FunctionMapResponseTransformer(this, mappedMappings);

        wireMockServer = new WireMockServer(
            WireMockConfiguration.wireMockConfig()
                .dynamicPort()
                .extensions(transformer)
                .stubRequestLoggingDisabled(true)
        );

        setRequestListener();

        wireMockServer.start();

        wireMockServer.stubFor(
            WireMock.any(WireMock.anyUrl())
                .willReturn(WireMock.aResponse().withTransformers(transformer.getName()))
        );

        SimulatorFactory.simulatorInstances.add(this);

        LOGGER.info("Simulator '{}' startup tid={}ms", name, System.currentTimeMillis() - startupTime);
    }

    public int getPort() {
        return wireMockServer.port();
    }

    public String getUrl() {
        return "http://localhost:" + getPort();
    }

    public void shutdown() {
        simulateDowntime = true;
    }

    public boolean isRunning() {
        return !simulateDowntime;
    }

    public abstract void reset();

    private void setRequestListener() {
        wireMockServer.addMockServiceRequestListener((request, response) -> {
            if (verbose) {
                String logMessage = String.format("[%s] Got request: %s %s - sending status %d",
                    name, request.getMethod(), request.getUrl(), response.getStatus());
                LOGGER.debug(logMessage);

                String reqBody = request.getBodyAsString();
                if (reqBody != null) {
                    logMessage = String.format("[%s] Request body: %s", name, reqBody);
                    LOGGER.debug(logMessage);
                }
            }
        });
    }

    protected static <T> T readJson(Request request, Class<T> clazz) {
        try {
            return objectMapper.readValue(request.getBody(), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static String writeJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class SimulatorFactory {
        private static List<Simulator> simulatorInstances = Collections.synchronizedList(new ArrayList<>());

        public static String getAllSimulatorNames() {
            return simulatorInstances.stream()
                .map(simulator -> simulator.name)
                .collect(Collectors.joining(","));
        }

        public static void shutdownAll() {
            simulatorInstances.stream()
                .filter(simulator -> !simulator.wireMockServer.isRunning())
                .forEach(simulator -> {
                    simulator.shutdown();
                    simulator.setSimulateDowntime(true);
                });
        }

        public static void resetAll() {
            simulatorInstances.forEach(Simulator::reset);
        }

        public static void simulateDowntime(String name) {
            Simulator simulator = findSimulator(name);
            if (!simulator.simulateDowntime) {
                throw new RuntimeException("simulatorinstace with name " + name + " is already down");
            }
            simulator.setSimulateDowntime(true);
        }

        public static void maintainUptime(String name) {
            Simulator simulator = findSimulator(name);
            if (simulator == null) {
                throw new RuntimeException("simulatorinstace with name " + name + " is already running");
            }

            simulator.setSimulateDowntime(false);
        }

        @SuppressWarnings("unchecked")
        public static <T extends Simulator> T findSimulator(String name) {
            return (T) simulatorInstances.stream().filter(simulator -> simulator.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                    "No simulatorinstance with the name $name is running. Active simulators:"
                        + SimulatorFactory.getAllSimulatorNames()));

        }
    }
}
