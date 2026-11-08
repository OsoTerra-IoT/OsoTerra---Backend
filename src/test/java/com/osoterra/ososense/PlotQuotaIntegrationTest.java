package com.osoterra.ososense;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/**
 * The active subscription plan caps the owner's active plots, and the subscription reports
 * the real consumption. Without an active subscription there is no limit (current behaviour).
 */
@AutoConfigureMockMvc
class PlotQuotaIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void freePlanRejectsAPlotAboveItsLimitAndReportsRealConsumption() throws Exception {
        String token = signUpAndSignIn("quota.free@example.com");
        String subscription = body(mvc.perform(auth(json(post("/api/v1/subscriptions"), """
                {"subscriptionPlanId":%d}""".formatted(planId("Free"))), token)).andExpect(status().isCreated()));
        Integer subscriptionId = JsonPath.read(subscription, "$.id");
        Integer farmId = createFarm(token);

        mvc.perform(auth(get("/api/v1/subscriptions/" + subscriptionId), token))
                .andExpect(jsonPath("$.quotaTotal").value(2)).andExpect(jsonPath("$.quotaConsumed").value(0));
        Integer firstPlot = createPlot(token, farmId, "Uno", status().isCreated());
        createPlot(token, farmId, "Dos", status().isCreated());
        mvc.perform(auth(get("/api/v1/subscriptions/mine"), token))
                .andExpect(jsonPath("$[0].quotaConsumed").value(2));

        createPlot(token, farmId, "Tres", status().isConflict());
        mvc.perform(auth(get("/api/v1/subscriptions/" + subscriptionId), token))
                .andExpect(jsonPath("$.quotaConsumed").value(2));

        mvc.perform(auth(post("/api/v1/plots/" + firstPlot + "/deactivation"), token)).andExpect(status().isOk());
        mvc.perform(auth(get("/api/v1/subscriptions/" + subscriptionId), token))
                .andExpect(jsonPath("$.quotaConsumed").value(1));
        createPlot(token, farmId, "Tres", status().isCreated());
    }

    @Test
    void ownerWithoutAnActiveSubscriptionKeepsTheCurrentUnlimitedBehaviour() throws Exception {
        String token = signUpAndSignIn("quota.none@example.com");
        Integer farmId = createFarm(token);

        for (int i = 1; i <= 3; i++) {
            createPlot(token, farmId, "Parcela " + i, status().isCreated());
        }
    }

    private Integer planId(String name) throws Exception {
        List<Integer> ids = JsonPath.read(
                body(mvc.perform(get("/api/v1/subscription-plans")).andExpect(status().isOk())),
                "$[?(@.name == '" + name + "')].id");
        return ids.get(0);
    }

    private Integer createFarm(String token) throws Exception {
        String farm = body(mvc.perform(auth(json(post("/api/v1/farms"), """
                {"name":"Fundo Cuota","department":"Piura","province":"Piura","district":"Castilla"}"""), token))
                .andExpect(status().isCreated()));
        return JsonPath.read(farm, "$.id");
    }

    private Integer createPlot(
            String token, Integer farmId, String name, org.springframework.test.web.servlet.ResultMatcher expected)
            throws Exception {
        String response = body(mvc.perform(auth(json(post("/api/v1/plots"), """
                {"farmId":%d,"name":"%s","areaHectares":2.5,"latitude":-5.19,"longitude":-80.63}"""
                .formatted(farmId, name)), token)).andExpect(expected));
        return response.contains("\"id\"") ? JsonPath.read(response, "$.id") : null;
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
