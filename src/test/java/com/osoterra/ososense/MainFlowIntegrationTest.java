package com.osoterra.ososense;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import com.osoterra.ososense.support.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/**
 * Walks the main farmer journey through the public API: sign up, register a farm, a plot
 * and a device, ingest telemetry from the Edge Service and read the resulting alert.
 */
@AutoConfigureMockMvc
class MainFlowIntegrationTest extends IntegrationTest {

    private static final String EDGE_KEY = "local-edge-key";

    @Autowired
    private MockMvc mvc;

    @Test
    void farmerMonitorsAPlotFromSignUpToAlert() throws Exception {
        mvc.perform(get("/api/v1/subscription-plans")).andExpect(status().isOk());
        mvc.perform(get("/v3/api-docs")).andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/v1/farms/mine'].get.parameters").doesNotExist());
        mvc.perform(get("/api/v1/users/me")).andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());

        mvc.perform(json(post("/api/v1/auth/signup"), """
                {"email":"rosa@example.com","password":"Secreto123","firstName":"Rosa",
                 "lastName":"Quispe","role":"FARMER"}""")).andExpect(status().is2xxSuccessful());
        String session = body(mvc.perform(json(post("/api/v1/auth/signin"), """
                {"email":"rosa@example.com","password":"Secreto123"}""")).andExpect(status().isOk()));
        String token = JsonPath.read(session, "$.token");

        String crops = body(mvc.perform(get("/api/v1/crops")).andExpect(status().isOk()));
        List<Integer> grapeIds = JsonPath.read(crops, "$[?(@.commonName == 'Uva')].id");
        assertThat(grapeIds).hasSize(1);

        String farm = body(mvc.perform(auth(json(post("/api/v1/farms"), """
                {"name":"Santa Rosa","department":"Lima","province":"Huaral","district":"Aucallama"}"""), token))
                .andExpect(status().isCreated()));
        Integer farmId = JsonPath.read(farm, "$.id");

        String plot = body(mvc.perform(auth(json(post("/api/v1/plots"), """
                {"farmId":%d,"name":"Lote Norte","areaHectares":2.5,"latitude":-11.5,"longitude":-77.2}"""
                .formatted(farmId)), token)).andExpect(status().isCreated()));
        Integer plotId = JsonPath.read(plot, "$.id");
        mvc.perform(auth(json(put("/api/v1/plots/" + plotId), """
                {"name":"Lote Norte A","areaHectares":2.7,"latitude":-11.5,"longitude":-77.2}"""), token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Lote Norte A"));
        mvc.perform(auth(json(put("/api/v1/farms/" + farmId), """
                {"name":"Fundo Santa Rosa","department":"Lima","province":"Huaral","district":"Aucallama"}"""), token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Fundo Santa Rosa"));
        mvc.perform(auth(json(post("/api/v1/plots/" + plotId + "/crop"), """
                {"cropId":%d}""".formatted(grapeIds.getFirst())), token)).andExpect(status().isOk());

        String device = body(mvc.perform(auth(json(post("/api/v1/devices"), """
                {"activationCode":"OSO-0001"}"""), token)).andExpect(status().isCreated()));
        Integer deviceId = JsonPath.read(device, "$.id");
        mvc.perform(auth(json(post("/api/v1/devices/" + deviceId + "/attachment"), """
                {"plotId":%d}""".formatted(plotId)), token)).andExpect(status().isOk());
        mvc.perform(auth(get("/api/v1/devices").param("plotId", plotId.toString()), token))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(deviceId));

        String batch = """
                {"deviceId":%d,"readings":[{"rawConductivityDsM":2.5,"compensatedConductivityDsM":2.4,
                 "compensationFactor":0.95,"moisturePercentage":31.5,"temperatureCelsius":27.0,
                 "capturedAt":"2026-09-30T08:00:00"}]}""".formatted(deviceId);
        mvc.perform(json(post("/api/v1/soil-readings/batches"), batch)).andExpect(status().isUnauthorized());
        mvc.perform(json(post("/api/v1/soil-readings/batches"), batch).header("X-Edge-Api-Key", EDGE_KEY))
                .andExpect(status().isCreated());

        mvc.perform(auth(get("/api/v1/soil-readings").param("plotId", plotId.toString()), token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
        mvc.perform(auth(get("/api/v1/salinity-alerts").param("plotId", plotId.toString()), token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].severity").value("CRITICAL"))
                .andExpect(jsonPath("$[0].status").value("OPEN"));
        mvc.perform(auth(get("/api/v1/dashboard/plots/" + plotId), token)).andExpect(status().isOk())
                .andExpect(jsonPath("$.recentAlerts.length()").value(1));

        String alerts = body(mvc.perform(auth(get("/api/v1/salinity-alerts").param("plotId", plotId.toString()), token)));
        Integer alertId = JsonPath.read(alerts, "$[0].id");
        mvc.perform(auth(get("/api/v1/salinity-alerts/" + alertId + "/corrective-actions"), token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
        mvc.perform(auth(json(post("/api/v1/salinity-alerts/" + alertId + "/corrective-actions"), """
                {"actionType":"LEACHING","executedAt":"2026-09-30","notes":"Riego de lavado"}"""), token))
                .andExpect(status().isCreated());
        mvc.perform(auth(get("/api/v1/salinity-alerts/" + alertId + "/corrective-actions"), token))
                .andExpect(jsonPath("$[0].actionType").value("LEACHING"));
        mvc.perform(auth(get("/api/v1/salinity-alerts/" + alertId), token))
                .andExpect(jsonPath("$.status").value("RESOLVED"));
    }

    private static MockHttpServletRequestBuilder json(MockHttpServletRequestBuilder request, String content) {
        return request.contentType(MediaType.APPLICATION_JSON).content(content);
    }

    private static MockHttpServletRequestBuilder auth(MockHttpServletRequestBuilder request, String token) {
        return request.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }

    private static String body(org.springframework.test.web.servlet.ResultActions result) throws Exception {
        return result.andReturn().getResponse().getContentAsString();
    }
}
