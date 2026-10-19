package com.osoterra.ososense;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import com.osoterra.ososense.support.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/**
 * An advisor reaches a farmer's farms only through an accepted advisory link, and sees the
 * corrective action that resolved an alert.
 */
@AutoConfigureMockMvc
class AdvisorFlowIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void advisorReadsLinkedFarmerFarmsOnly() throws Exception {
        String farmer = signUpAndSignIn("carla@example.com", "FARMER", null);
        String advisor = signUpAndSignIn("diego@example.com", "ADVISOR", "CIP-123456");
        Integer farmerId = JsonPath.read(body(mvc.perform(auth(get("/api/v1/users/me"), farmer))), "$.id");

        String farm = body(mvc.perform(auth(json(post("/api/v1/farms"), """
                {"name":"Los Olivos","department":"Lima","province":"Huaura","district":"Huacho"}"""), farmer))
                .andExpect(status().isCreated()));
        Integer farmId = JsonPath.read(farm, "$.id");
        mvc.perform(auth(json(put("/api/v1/farms/" + farmId), """
                {"name":"Tomado","department":"Lima","province":"Huaura","district":"Huacho"}"""), advisor))
                .andExpect(status().isForbidden());
        mvc.perform(auth(get("/api/v1/farms").param("ownerId", farmerId.toString()), advisor))
                .andExpect(status().isForbidden());

        String link = body(mvc.perform(auth(json(post("/api/v1/advisory-links"), """
                {"farmerId":%d}""".formatted(farmerId)), advisor)).andExpect(status().isCreated()));
        Integer linkId = JsonPath.read(link, "$.id");
        mvc.perform(auth(post("/api/v1/advisory-links/" + linkId + "/acceptance"), farmer))
                .andExpect(status().isOk());

        mvc.perform(auth(get("/api/v1/users/me/linked-accounts"), advisor)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("carla@example.com"));
        mvc.perform(auth(get("/api/v1/farms").param("ownerId", farmerId.toString()), advisor))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("Los Olivos"));
    }

    @Test
    void cropCatalogIncludesNorthCoastExportCrops() throws Exception {
        mvc.perform(get("/api/v1/crops")).andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.commonName == 'Palta')].salinityThresholdDsM").value(1.1))
                .andExpect(jsonPath("$[?(@.commonName == 'Arándano')]").isNotEmpty());
    }

    private String signUpAndSignIn(String email, String role, String license) throws Exception {
        String licenseJson = license == null ? "null" : "\"" + license + "\"";
        mvc.perform(json(post("/api/v1/auth/signup"), """
                {"email":"%s","password":"Secreto123","firstName":"Ana","lastName":"Soto","role":"%s",
                 "professionalLicenseNumber":%s}""".formatted(email, role, licenseJson)))
                .andExpect(status().isCreated());
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
