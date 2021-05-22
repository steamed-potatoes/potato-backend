package com.potato.api.controller.organization.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.api.controller.ApiResponse;
import com.potato.api.service.member.dto.response.MemberInfoResponse;
import com.potato.api.service.organization.dto.response.OrganizationInfoResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrganizationFollowerMockMvc {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    public OrganizationFollowerMockMvc(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public ApiResponse<String> followOrganization(String subDomain, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = post("/api/v1/organization/follow/" + subDomain)
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

    public ApiResponse<String> unFollowOrganization(String subDomain, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = delete("/api/v1/organization/follow/" + subDomain)
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

    public ApiResponse<List<MemberInfoResponse>> getOrganizationFollowMember(String subDomain, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v1/organization/follow/" + subDomain)
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

    public ApiResponse<List<OrganizationInfoResponse>> retrieveFollowingOrganization(String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v1/member/organization/follow")
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

}
