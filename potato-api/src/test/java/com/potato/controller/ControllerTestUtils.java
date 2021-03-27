package com.potato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.controller.member.MemberMockMvc;
import com.potato.domain.member.Member;
import com.potato.domain.member.MemberCreator;
import com.potato.domain.member.MemberRepository;
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

    protected void setup() {
        testMember = memberRepository.save(MemberCreator.create("test.potato@gmail.com"));
        memberMockMvc = new MemberMockMvc(mockMvc, objectMapper);
    }

    protected void cleanup() {
        memberRepository.deleteAll();
    }

}
