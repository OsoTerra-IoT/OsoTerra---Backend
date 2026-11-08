package com.osoterra.ososense;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import com.osoterra.ososense.support.IntegrationTest;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/**
 * A plot's latest alert keeps covering equal or milder readings while it is open or
 * acknowledged; only a higher severity, or resolving it, lets a new alert through.
 */
@AutoConfigureMockMvc
class AlertRepeatIntegrationTest extends IntegrationTest {

    private static final String EDGE_KEY = "local-edge-key";

    @Autowired
    private MockMvc mvc;

    private BigDecimal threshold;
    private int minute;

    @Test
    void anAcknowledgedAlertStillBlocksEqualOrMilderReadingsUntilItIsResolved() throws Exception {
        String token = signUpAndSignIn("alerts.ack@example.com");
        Integer deviceId = setUpPlotWithDevice(token, "OSO-ALR-0001");
        Integer plotId = plotOf(token, deviceId);

        send(deviceId, "2.0");
        Integer alertId = latestAlertId(token, plotId);
        mvc.perform(auth(post("/api/v1/salinity-alerts/" + alertId + "/acknowledgement"), token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ACKNOWLEDGED"));

        send(deviceId, "2.0");
        send(deviceId, "1.1");
        expectAlertCount(token, plotId, 1);

        mvc.perform(auth(json(post("/api/v1/salinity-alerts/" + alertId + "/corrective-actions"), """
                {"actionType":"LEACHING","executedAt":"2026-09-30","notes":"Riego de lavado"}"""), token))
                .andExpect(status().isCreated());
        send(deviceId, "2.0");
        expectAlertCount(token, plotId, 2);
    }

    @Test
    void aHigherSeverityRaisesANewAlertEvenWhileTheMilderOneIsAcknowledged() throws Exception {
        String token = signUpAndSignIn("alerts.escalate@example.com");
        Integer deviceId = setUpPlotWithDevice(token, "OSO-ALR-0002");
        Integer plotId = plotOf(token, deviceId);

        send(deviceId, "1.1");
        Integer watchAlert = latestAlertId(token, plotId);
        mvc.perform(auth(post("/api/v1/salinity-alerts/" + watchAlert + "/acknowledgement"), token))
                .andExpect(status().isOk());
        send(deviceId, "1.1");
        expectAlertCount(token, plotId, 1);

        send(deviceId, "2.0");
        expectAlertCount(token, plotId, 2);
        mvc.perform(auth(get("/api/v1/salinity-alerts").param("plotId", plotId.toString()), token))
                .andExpect(jsonPath("$[?(@.severity == 'CRITICAL')]").isNotEmpty());
    }

    /** Sends one reading whose compensated value is {@code ratio} times the crop threshold. */
    private void send(Integer deviceId, String ratio) throws Exception {
        BigDecimal value = threshold.multiply(new BigDecimal(ratio));
        String batch = """
                {"deviceId":%d,"readings":[{"rawConductivityDsM":%s,"compensatedConductivityDsM":%s,
                 "compensationFactor":1.0,"moisturePercentage":31.5,"temperatureCelsius":27.0,
                 "capturedAt":"2026-09-30T08:%02d:00"}]}""".formatted(deviceId, value, value, minute++);
        mvc.perform(json(post("/api/v1/soil-readings/batches"), batch).header("X-Edge-Api-Key", EDGE_KEY))
                .andExpect(status().isCreated());
    }

    private void expectAlertCount(String token, Integer plotId, int count) throws Exception {
        mvc.perform(auth(get("/api/v1/salinity-alerts").param("plotId", plotId.toString()), token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(count));
    }

    private Integer latestAlertId(String token, Integer plotId) throws Exception {
        String alerts = body(mvc.perform(auth(get("/api/v1/salinity-alerts").param("plotId", plotId.toString()), token)));
        List<Integer> ids = JsonPath.read(alerts, "$[*].id");
        return ids.get(ids.size() - 1);
    }

    private Integer plotOf(String token, Integer deviceId) throws Exception {
        return JsonPath.read(body(mvc.perform(auth(get("/api/v1/devices/" + deviceId), token))), "$.plotId");
    }

    private Integer setUpPlotWithDevice(String token, String activationCode) throws Exception {
        minute = 0;
        String crops = body(mvc.perform(get("/api/v1/crops")).andExpect(status().isOk()));
        List<Integer> cropIds = JsonPath.read(crops, "$[?(@.commonName == 'Uva')].id");
        List<Number> thresholds = JsonPath.read(crops, "$[?(@.commonName == 'Uva')].salinityThresholdDsM");
        threshold = new BigDecimal(thresholds.get(0).toString());

        String farm = body(mvc.perform(auth(json(post("/api/v1/farms"), """
                {"name":"Fundo Alertas","department":"Piura","province":"Piura","district":"Castilla"}"""), token))
                .andExpect(status().isCreated()));
        Integer farmId = JsonPath.read(farm, "$.id");
        String plot = body(mvc.perform(auth(json(post("/api/v1/plots"), """
                {"farmId":%d,"name":"Parcela","areaHectares":2.5,"latitude":-5.19,"longitude":-80.63}"""
                .formatted(farmId)), token)).andExpect(status().isCreated()));
        Integer plotId = JsonPath.read(plot, "$.id");
        mvc.perform(auth(json(post("/api/v1/plots/" + plotId + "/crop"), """
                {"cropId":%d}""".formatted(cropIds.get(0))), token)).andExpect(status().isOk());
        String device = body(mvc.perform(auth(json(post("/api/v1/devices"), """
                {"activationCode":"%s"}""".formatted(activationCode)), token)).andExpect(status().isCreated()));
        Integer deviceId = JsonPath.read(device, "$.id");
        mvc.perform(auth(json(post("/api/v1/devices/" + deviceId + "/attachment"), """
                {"plotId":%d}""".formatted(plotId)), token)).andExpect(status().isOk());
        return deviceId;
    }

    private String signUpAndSignIn(String email) throws Exception {
        mvc.perform(json(post("/api/v1/auth/signup"), """
                {"email":"%s","password":"Secreto123","firstName":"Ana","lastName":"Soto","role":"FARMER"}"""
                .formatted(email))).andExpect(status().isCreated());
        return JsonPath.read(body(mvc.perform(json(post("/api/v1/auth/signin"), """
                {"email":"%s","password":"Secreto123"}""".formatted(email)))), "$.token");
    }

    private static MockHttpServletRequestBuilder json(MockHttpServletRequestBuilder request, String content) {
        return request.contentType(MediaType.APPLICATION_JSON).content(content);
    }

    private static MockHttpServletRequestBuilder auth(MockHttpServletRequestBuilder request, String token) {
        return request.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }

    private static String body(ResultActions result) throws Exception {
        return result.andReturn().getResponse().getContentAsString();
    }
}
