package com.potato.admin.controller.member;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.admin.controller.ApiResponse;
import com.potato.admin.service.board.dto.request.CreateAdminBoardRequest;
import com.potato.admin.service.board.dto.response.AdminBoardInfoResponse;
import com.potato.admin.service.member.dto.response.MemberInfoResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberMockMvc {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public MemberMockMvc(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public ApiResponse<List<MemberInfoResponse>> retrieveMember(String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/admin/v1/member/list")
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
