package com.potato.controller.comment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.controller.ApiResponse;
import com.potato.service.comment.dto.response.BoardCommentResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class BoardCommentMockMvc {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    public BoardCommentMockMvc(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public ApiResponse<List<BoardCommentResponse>> retrieveBoardComments(Long organizationBoardId) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v2/board/comment/list")
            .param("organizationBoardId", String.valueOf(organizationBoardId));

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

}
