package com.potato.api.controller.schedule;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.api.controller.ApiResponse;
import com.potato.api.service.schedule.dto.request.ScheduleRequest;
import com.potato.api.service.schedule.dto.response.ScheduleResponse;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class ScheduleMockMvc {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    public ScheduleMockMvc(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public ApiResponse<ScheduleResponse> retrieveScheduleBetween(ScheduleRequest request) throws Exception {
        MultiValueMap<String, String> scheduleRequest = new LinkedMultiValueMap<>();
        scheduleRequest.add("startDate", String.valueOf(request.getStartDate()));
        scheduleRequest.add("endDate", String.valueOf(request.getEndDate()));
        MockHttpServletRequestBuilder builder = get("/api/v1/schedule")
            .params(scheduleRequest)
            .contentType(MediaType.APPLICATION_JSON);

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

}
