package com.osoterra.ososense.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.Then;

/**
 * Steps shared by every feature.
 */
public class CommonSteps {

    private final ApiClient api;

    public CommonSteps(ApiClient api) {
        this.api = api;
    }

    @Then("the response status is {int}")
    public void theResponseStatusIs(int status) {
        assertThat(api.lastResponse().getStatus()).isEqualTo(status);
    }
}
