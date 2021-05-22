package com.potato.admin.controller.board;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.admin.controller.ApiResponse;
import com.potato.admin.service.board.dto.request.CreateAdminBoardRequest;
import com.potato.admin.service.board.dto.request.DeleteOrganizationBoardRequest;
import com.potato.admin.service.board.dto.request.UpdateAdminBoardRequest;
import com.potato.admin.service.board.dto.response.AdminBoardInfoResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminBoardMockMvc {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public AdminBoardMockMvc(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public ApiResponse<AdminBoardInfoResponse> createAdminBoard(CreateAdminBoardRequest request, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = post("/admin/v1/board/admin")
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

    public ApiResponse<AdminBoardInfoResponse> updateAdminBoard(UpdateAdminBoardRequest request, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = put("/admin/v1/board/admin")
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

    public ApiResponse<String> deleteOrganizationBoard(Long organizationBoardId, String subDomain, String token, int expectedStatus) throws Exception {
        MultiValueMap<String, String> deleteBoardRequest = new LinkedMultiValueMap<>();
        deleteBoardRequest.add("organizationBoardId", String.valueOf(organizationBoardId));
        MockHttpServletRequestBuilder builder = delete("/admin/v1/board/organization/" + subDomain)
            .params(deleteBoardRequest)
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
