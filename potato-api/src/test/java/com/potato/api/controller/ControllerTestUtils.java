package com.potato.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.api.controller.member.MemberMockMvc;
import com.potato.domain.domain.member.Member;
import com.potato.domain.domain.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public abstract class ControllerTestUtils {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MemberRepository memberRepository;

    protected MemberMockMvc memberMockMvc;

    protected Member testMember;

    protected String token;

    protected void setup() throws Exception {
        memberMockMvc = new MemberMockMvc(mockMvc, objectMapper);
        String email = "potato@gmail.com";
        token = memberMockMvc.getMockMemberToken(email);
        testMember = memberRepository.findMemberByEmail(email);
    }

    protected void cleanup() {
        memberRepository.deleteAll();
    }

}
