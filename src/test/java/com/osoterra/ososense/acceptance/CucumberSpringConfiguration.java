package com.osoterra.ososense.acceptance;

import com.osoterra.ososense.support.IntegrationTest;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

/**
 * Boots the whole application against the embedded database for the Cucumber scenarios.
 */
@CucumberContextConfiguration
@AutoConfigureMockMvc
public class CucumberSpringConfiguration extends IntegrationTest {
}
