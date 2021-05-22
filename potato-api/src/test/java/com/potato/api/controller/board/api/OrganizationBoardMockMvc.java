package com.potato.api.controller.board.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.api.controller.ApiResponse;
import com.potato.api.service.board.organization.dto.request.CreateOrganizationBoardRequest;
import com.potato.api.service.board.organization.dto.request.DeleteOrganizationBoardRequest;
import com.potato.api.service.board.organization.dto.request.LikeOrganizationBoardRequest;
import com.potato.api.service.board.organization.dto.request.UpdateOrganizationBoardRequest;
import com.potato.api.service.board.organization.dto.response.OrganizationBoardInfoResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrganizationBoardMockMvc {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    public OrganizationBoardMockMvc(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public ApiResponse<OrganizationBoardInfoResponse> createOrganizationBoard(String subDomain, CreateOrganizationBoardRequest request, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = post("/api/v2/organization/board/" + subDomain)
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

    public ApiResponse<OrganizationBoardInfoResponse> updateOrganizationBoard(String subDomain, UpdateOrganizationBoardRequest request, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = put("/api/v2/organization/board/" + subDomain)
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

    public ApiResponse<String> deleteOrganizationBoard(String subDomain, DeleteOrganizationBoardRequest request, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = delete("/api/v2/organization/board/" + subDomain)
            .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(token))
            .param("organizationBoardId", String.valueOf(request.getOrganizationBoardId()));

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andExpect(status().is(expectedStatus))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

    public ApiResponse<String> addOrganizationBoardLiked(LikeOrganizationBoardRequest request, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = post("/api/v2/organization/board/like")
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

    public ApiResponse<String> cancelOrganizationBoardLike(LikeOrganizationBoardRequest request, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = delete("/api/v2/organization/board/like")
            .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(token))
            .param("organizationBoardId", String.valueOf(request.getOrganizationBoardId()));

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
