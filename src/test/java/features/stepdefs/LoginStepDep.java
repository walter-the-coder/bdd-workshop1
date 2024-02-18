package features.stepdefs;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.client.RestTemplate;

import com.bdd.workshop.type.TaxpayerIdentificationNumber;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;

public class LoginStepDep {
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    public static TaxpayerIdentificationNumber TIN;
    public static String password;

    public LoginStepDep(
        UserDetailsManager userDetailsManager,
        PasswordEncoder passwordEncoder
    ) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Before
    public void reset(Scenario scenario) {
        TIN = null;
        password = null;
    }

    @Given("taxpayer with TIN {string} is logged in")
    public void taxpayer_with_tin_is_logged_in(String string) {
        TIN = new TaxpayerIdentificationNumber(string);
        password = "password";

        UserDetails user = User.builder()
            .username(TIN.toString())
            .password(passwordEncoder.encode("password"))
            .roles("USER")
            .build();

        userDetailsManager.createUser(user);
    }

    public static RestTemplate restTemplate() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
            .rootUri("http://localhost:8080")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        if (TIN != null) {
            restTemplateBuilder = restTemplateBuilder.basicAuthentication(
                LoginStepDep.TIN.toString(),
                LoginStepDep.password
            );
        }

        return restTemplateBuilder.build();
    }
}
