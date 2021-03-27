package com.potato.controller.organization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.controller.ApiResponse;
import com.potato.service.organization.dto.request.CreateOrganizationRequest;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import com.potato.service.organization.dto.response.OrganizationWithMembersInfoResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

class OrganizationMockMvc {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    public OrganizationMockMvc(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public ApiResponse<OrganizationInfoResponse> createOrganization(CreateOrganizationRequest request, String token) throws Exception {
        MockHttpServletRequestBuilder builder = post("/api/v1/organization")
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(token))
            .content(objectMapper.writeValueAsString(request));

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

    public ApiResponse<OrganizationWithMembersInfoResponse> getDetailOrganizationInfo(String subDomain) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v1/organization/" + subDomain);
        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

    public ApiResponse<List<OrganizationInfoResponse>> getOrganizations() throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v1/organization/list");
        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

    public ApiResponse<List<OrganizationInfoResponse>> getMyOrganizations(String token) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v1/organization/my")
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(token));

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

}
