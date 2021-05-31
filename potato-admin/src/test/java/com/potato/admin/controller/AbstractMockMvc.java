package com.potato.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;

public abstract class AbstractMockMvc {

    protected final MockMvc mockMvc;

    protected final ObjectMapper objectMapper;

    protected AbstractMockMvc(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

}
