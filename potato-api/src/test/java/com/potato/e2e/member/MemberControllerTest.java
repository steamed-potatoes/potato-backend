package com.potato.e2e.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.controller.member.MemberController;
import com.potato.e2e.AbstractControllerTest;
import com.potato.domain.member.Member;
import com.potato.domain.member.MemberRepository;
import com.potato.service.member.dto.request.CreateMemberRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class MemberControllerTest extends AbstractControllerTest {

    @Autowired
    private MemberController memberController;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    protected Object controller() {
        return memberController;
    }

    @AfterEach
    void cleanUp() {
        memberRepository.deleteAll();
    }

    @Test
    void 회원가입_API_호출시_200OK() throws Exception {
        // given
        String email = "will.seungho@gmail.com";
        String name = "강승호";
        Integer classNumber = 201610323;

        CreateMemberRequest request = CreateMemberRequest.testBuilder()
            .email(email)
            .name(name)
            .classNumber(201610323)
            .build();

        // when
        this.mockMvc.perform(post("/api/v1/member")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsBytes(request)))
            .andDo(print())
            .andExpect(status().isOk());

        // then
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(1);
    }

    @Test
    void 회원가입_API_이메일을_입력하지_않을경우_400_ERROR() throws Exception {
        // given
        String name = "강승호";

        CreateMemberRequest request = CreateMemberRequest.testBuilder()
            .name(name)
            .build();

        // when
        this.mockMvc.perform(post("/api/v1/member")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsBytes(request)))
            .andDo(print())
            .andExpect(status().isBadRequest());

        // then
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).isEmpty();
    }

}
