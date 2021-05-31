package com.potato.admin.controller.admin.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.admin.controller.AbstractMockMvc;
import com.potato.admin.controller.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdministratorMockMvc extends AbstractMockMvc {

    public AdministratorMockMvc(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    public ApiResponse<String> getSession(int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/test-session")
            .contentType(MediaType.APPLICATION_JSON);

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andExpect(status().is(expectedStatus))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

    public String getMockAdminToken() throws Exception {
        return getSession(200).getData();
    }

}
