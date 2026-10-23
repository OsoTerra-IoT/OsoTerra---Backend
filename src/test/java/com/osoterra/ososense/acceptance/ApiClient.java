package com.osoterra.ososense.acceptance;

import io.cucumber.spring.ScenarioScope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Calls the public API through MockMvc and keeps the last response of the scenario.
 */
@Component
@ScenarioScope
public class ApiClient {

    private final MockMvc mvc;
    private MockHttpServletResponse lastResponse;

    public ApiClient(MockMvc mvc) {
        this.mvc = mvc;
    }

    public MockHttpServletResponse postJson(String path, String json, String token, HttpHeaders headers)
            throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(path)
                .contentType(MediaType.APPLICATION_JSON).content(json).headers(headers);
        return send(withAuth(request, token));
    }

    public MockHttpServletResponse postJson(String path, String json, String token) throws Exception {
        return postJson(path, json, token, new HttpHeaders());
    }

    public MockHttpServletResponse getJson(String path, String token) throws Exception {
        return send(withAuth(MockMvcRequestBuilders.get(path), token));
    }

    public MockHttpServletResponse lastResponse() {
        return lastResponse;
    }

    private MockHttpServletResponse send(MockHttpServletRequestBuilder request) throws Exception {
        lastResponse = mvc.perform(request).andReturn().getResponse();
        return lastResponse;
    }

    private static MockHttpServletRequestBuilder withAuth(MockHttpServletRequestBuilder request, String token) {
        return token == null ? request : request.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
