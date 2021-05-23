package com.potato.api.controller.member.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.api.controller.AbstractMockMvc;
import com.potato.api.controller.ApiResponse;
import com.potato.domain.domain.member.MemberMajor;
import com.potato.api.service.member.dto.request.SignUpMemberRequest;
import com.potato.api.service.member.dto.request.UpdateMemberRequest;
import com.potato.api.service.member.dto.response.MajorInfoResponse;
import com.potato.api.service.member.dto.response.MemberInfoResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberMockMvc extends AbstractMockMvc {

    public MemberMockMvc(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    public ApiResponse<String> signUpMember(SignUpMemberRequest request, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = post("/api/v1/member")
            .contentType(MediaType.APPLICATION_JSON)
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

    public ApiResponse<MemberInfoResponse> getMyMemberInfo(String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v1/member")
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

    public ApiResponse<MemberInfoResponse> updateMemberInfo(UpdateMemberRequest request, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = put("/api/v1/member")
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

    public ApiResponse<MemberInfoResponse> getMemberInfo(Long memberId, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v1/member/" + memberId);

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andExpect(status().is(expectedStatus))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

    public ApiResponse<List<MajorInfoResponse>> getMajors(int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v1/major/list");

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andExpect(status().is(expectedStatus))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

    public String getMockMemberToken() throws Exception {
        SignUpMemberRequest request = SignUpMemberRequest.testBuilder()
            .email("will.seungho@gmail.com")
            .name("강승호")
            .major(MemberMajor.IT_ICT)
            .classNumber(201610302)
            .profileUrl("https://profile.com")
            .build();
        return signUpMember(request, 200).getData();
    }

    public String getMockMemberToken(String email) throws Exception {
        SignUpMemberRequest request = SignUpMemberRequest.testBuilder()
            .email(email)
            .name("강승호")
            .major(MemberMajor.IT_ICT)
            .classNumber(201610302)
            .profileUrl("https://profile.com")
            .build();
        return signUpMember(request, 200).getData();
    }

}
