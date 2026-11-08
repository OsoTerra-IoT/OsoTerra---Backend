package com.osoterra.ososense;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
 * Registering a laboratory calibration must leave its resulting factor on the sensor.
 */
@AutoConfigureMockMvc
class CalibrationFactorIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void registeringACalibrationUpdatesTheDeviceCalibrationFactor() throws Exception {
        String token = signUpAndSignIn("calib.owner@example.com");
        Integer deviceId = createDeviceOnPlot(token, "OSO-CAL-0001");
        mvc.perform(auth(get("/api/v1/devices/" + deviceId), token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.calibrationFactor").value(1.0));

        mvc.perform(auth(json(post("/api/v1/calibration-records"), """
                {"deviceId":%d,"labConductivityDsM":2.0,"samplingDate":"2026-09-30",
                 "laboratoryName":"Lab Agrario","deviceReadingAtSampling":2.5}""".formatted(deviceId)), token))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.resultingFactor").value(0.8));

        mvc.perform(auth(get("/api/v1/devices/" + deviceId), token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.calibrationFactor").value(0.8));
    }

    @Test
    void aRejectedCalibrationLeavesTheDeviceFactorUntouched() throws Exception {
        String token = signUpAndSignIn("calib.rejected@example.com");
        Integer deviceId = createDeviceOnPlot(token, "OSO-CAL-0002");

        mvc.perform(auth(json(post("/api/v1/calibration-records"), """
                {"deviceId":%d,"labConductivityDsM":2.0,"samplingDate":"2026-09-30",
                 "laboratoryName":"Lab Agrario","deviceReadingAtSampling":0}""".formatted(deviceId)), token))
                .andExpect(status().isBadRequest());

        mvc.perform(auth(get("/api/v1/devices/" + deviceId), token))
                .andExpect(jsonPath("$.calibrationFactor").value(1.0));
    }

    private Integer createDeviceOnPlot(String token, String activationCode) throws Exception {
        String farm = body(mvc.perform(auth(json(post("/api/v1/farms"), """
                {"name":"Fundo Calibracion","department":"Piura","province":"Piura","district":"Castilla"}"""), token))
                .andExpect(status().isCreated()));
        Integer farmId = JsonPath.read(farm, "$.id");
        String plot = body(mvc.perform(auth(json(post("/api/v1/plots"), """
                {"farmId":%d,"name":"Parcela","areaHectares":2.5,"latitude":-5.19,"longitude":-80.63}"""
                .formatted(farmId)), token)).andExpect(status().isCreated()));
        Integer plotId = JsonPath.read(plot, "$.id");
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
