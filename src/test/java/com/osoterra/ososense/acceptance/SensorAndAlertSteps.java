package com.osoterra.ososense.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.http.HttpHeaders;

/**
 * Steps of US24 (link a sensor) and US31 (salinity alert).
 */
public class SensorAndAlertSteps {

    private static final String EDGE_KEY = "local-edge-key";

    private final ApiClient api;
    private String token;
    private Integer plotId;
    private Integer deviceId;
    private String batch;

    public SensorAndAlertSteps(ApiClient api) {
        this.api = api;
    }

    @Given("a farmer with a plot of table grape")
    public void aFarmerWithAPlotOfTableGrape() throws Exception {
        String email = "farmer-" + UUID.randomUUID() + "@example.com";
        api.postJson("/api/v1/auth/signup", """
                {"email":"%s","password":"Secreto123","firstName":"Rosa","lastName":"Quispe","role":"FARMER"}"""
                .formatted(email), null);
        token = JsonPath.read(api.postJson("/api/v1/auth/signin", """
                {"email":"%s","password":"Secreto123"}""".formatted(email), null).getContentAsString(), "$.token");

        String crops = api.getJson("/api/v1/crops", null).getContentAsString();
        List<Integer> grape = JsonPath.read(crops, "$[?(@.commonName == 'Uva')].id");
        Integer farmId = JsonPath.read(api.postJson("/api/v1/farms", """
                {"name":"Santa Rosa","department":"Lima","province":"Huaral","district":"Aucallama"}""", token)
                .getContentAsString(), "$.id");
        plotId = JsonPath.read(api.postJson("/api/v1/plots", """
                {"farmId":%d,"name":"Lote Norte","areaHectares":2.5,"latitude":-11.5,"longitude":-77.2}"""
                .formatted(farmId), token).getContentAsString(), "$.id");
        assertThat(api.postJson("/api/v1/plots/" + plotId + "/crop", """
                {"cropId":%d}""".formatted(grape.get(0)), token).getStatus()).isEqualTo(200);
    }

    @When("the farmer registers the device with activation code {string}")
    public void theFarmerRegistersTheDeviceWithActivationCode(String activationCode) throws Exception {
        registerDevice(activationCode);
    }

    @And("attaches it to the plot")
    public void attachesItToThePlot() throws Exception {
        assertThat(api.postJson("/api/v1/devices/" + deviceId + "/attachment", """
                {"plotId":%d}""".formatted(plotId), token).getStatus()).isEqualTo(200);
    }

    @Then("the device status is {string} and it belongs to the plot")
    public void theDeviceStatusIsAndItBelongsToThePlot(String status) throws Exception {
        String device = api.getJson("/api/v1/devices/" + deviceId, token).getContentAsString();
        assertThat(JsonPath.<String>read(device, "$.status")).isEqualTo(status);
        assertThat(JsonPath.<Integer>read(device, "$.plotId")).isEqualTo(plotId);
    }

    @Given("the plot has an active device")
    public void thePlotHasAnActiveDevice() throws Exception {
        registerDevice("OSO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        attachesItToThePlot();
    }

    @When("the Edge Service sends a batch with a compensated conductivity of {double} dS\\/m")
    public void theEdgeServiceSendsABatchWithACompensatedConductivityOf(double conductivity) {
        batch = String.format(Locale.ROOT, """
                {"deviceId":%d,"readings":[{"rawConductivityDsM":%.2f,"compensatedConductivityDsM":%.2f,
                 "compensationFactor":0.95,"moisturePercentage":31.5,"temperatureCelsius":27.0,
                 "capturedAt":"2026-09-30T08:00:00"}]}""", deviceId, conductivity / 0.95, conductivity);
    }

    @And("the request carries a valid {string}")
    public void theRequestCarriesAValid(String header) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add(header, EDGE_KEY);
        api.postJson("/api/v1/soil-readings/batches", batch, null, headers);
    }

    @And("the request carries no {string}")
    public void theRequestCarriesNo(String header) throws Exception {
        api.postJson("/api/v1/soil-readings/batches", batch, null);
    }

    @Then("the batch is synchronized")
    public void theBatchIsSynchronized() throws Exception {
        assertThat(api.lastResponse().getStatus()).isEqualTo(201);
        assertThat(JsonPath.<Integer>read(api.lastResponse().getContentAsString(), "$.acceptedCount")).isEqualTo(1);
    }

    @And("an open salinity alert with severity {string} exists for the plot")
    public void anOpenSalinityAlertWithSeverityExistsForThePlot(String severity) throws Exception {
        String alerts = api.getJson("/api/v1/salinity-alerts?plotId=" + plotId, token).getContentAsString();
        assertThat(JsonPath.<String>read(alerts, "$[0].severity")).isEqualTo(severity);
        assertThat(JsonPath.<String>read(alerts, "$[0].status")).isEqualTo("OPEN");
    }

    @And("the device was last seen at {string}")
    public void theDeviceWasLastSeenAt(String capturedAt) throws Exception {
        String device = api.getJson("/api/v1/devices/" + deviceId, token).getContentAsString();
        assertThat(JsonPath.<String>read(device, "$.lastSeenAt")).startsWith(capturedAt);
        assertThat(JsonPath.<String>read(device, "$.status")).isEqualTo("ACTIVE");
    }

    private void registerDevice(String activationCode) throws Exception {
        deviceId = JsonPath.read(api.postJson("/api/v1/devices", """
                {"activationCode":"%s"}""".formatted(activationCode), token).getContentAsString(), "$.id");
    }
}
