package com.potato.admin.controller.organization.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.admin.controller.AbstractMockMvc;
import com.potato.admin.controller.ApiResponse;
import com.potato.admin.service.organization.dto.request.UpdateCategoryRequest;
import com.potato.admin.service.organization.dto.response.OrganizationInfoResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrganizationMockMvc extends AbstractMockMvc {

    public OrganizationMockMvc(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    public ApiResponse<List<OrganizationInfoResponse>> retrieveOrganization(String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/admin/v1/organization/list")
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(token));

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andExpect(status().is(expectedStatus))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

    public ApiResponse<OrganizationInfoResponse> updateCategory(String subDomain, UpdateCategoryRequest request, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = put("/admin/v1/organization/" + subDomain + "/category")
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(token))
            .content(objectMapper.writeValueAsString(request));


        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andExpect(status().is(expectedStatus))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

}
