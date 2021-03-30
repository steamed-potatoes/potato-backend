package com.potato.controller.comment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.controller.ApiResponse;
import com.potato.service.comment.dto.request.AddBoardCommentRequest;
import com.potato.service.comment.dto.response.BoardCommentResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BoardCommentMockMvc {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    public BoardCommentMockMvc(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public ApiResponse<List<BoardCommentResponse>> retrieveBoardComments(Long organizationBoardId, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v2/board/comment/list")
            .param("organizationBoardId", String.valueOf(organizationBoardId));

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andExpect(status().is(expectedStatus))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

    public ApiResponse<String> addBoardComment(AddBoardCommentRequest request, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = post("/api/v2/board/comment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
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
