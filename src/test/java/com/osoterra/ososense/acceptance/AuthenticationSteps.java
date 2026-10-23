package com.osoterra.ososense.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Map;

/**
 * Steps of TS01 Authentication endpoints.
 */
public class AuthenticationSteps {

    private static final String PASSWORD = "Secreto123";

    private final ApiClient api;
    private String email;

    public AuthenticationSteps(ApiClient api) {
        this.api = api;
    }

    @Given("a farmer account registered with {string}")
    public void aFarmerAccountRegisteredWith(String email) throws Exception {
        this.email = email;
        int status = api.postJson("/api/v1/auth/signup", """
                {"email":"%s","password":"%s","firstName":"Diego","lastName":"Ramos","role":"FARMER"}"""
                .formatted(email, PASSWORD), null).getStatus();
        // The account may already exist from an earlier scenario of the same run.
        assertThat(status).isIn(201, 409);
    }

    @When("the developer sends POST {string} with valid credentials")
    public void theDeveloperSendsPostWithValidCredentials(String path) throws Exception {
        api.postJson(path, credentials(PASSWORD), null);
    }

    @When("the developer sends POST {string} with a wrong password")
    public void theDeveloperSendsPostWithAWrongPassword(String path) throws Exception {
        api.postJson(path, credentials("Equivocada999"), null);
    }

    @Then("the body contains a token and its expiration time")
    public void theBodyContainsATokenAndItsExpirationTime() throws Exception {
        String body = api.lastResponse().getContentAsString();
        assertThat(JsonPath.<String>read(body, "$.token")).isNotBlank();
        assertThat(JsonPath.<String>read(body, "$.expiresAt")).isNotBlank();
    }

    @Then("the body contains an error message without internal details")
    public void theBodyContainsAnErrorMessageWithoutInternalDetails() throws Exception {
        String body = api.lastResponse().getContentAsString();
        Map<String, Object> error = JsonPath.read(body, "$");
        assertThat((String) error.get("message")).isNotBlank();
        assertThat(error).doesNotContainKeys("trace", "exception", "stackTrace");
        assertThat(body).doesNotContain("Exception");
    }

    private String credentials(String password) {
        return """
                {"email":"%s","password":"%s"}""".formatted(email, password);
    }
}
