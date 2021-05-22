package com.potato.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.api.controller.member.api.MemberMockMvc;
import com.potato.domain.domain.member.Member;
import com.potato.domain.domain.member.MemberRepository;
import com.potato.domain.domain.organization.Organization;
import com.potato.domain.domain.organization.OrganizationCategory;
import com.potato.domain.domain.organization.OrganizationCreator;
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

    protected Organization organization;

    protected void setup() throws Exception {
        memberMockMvc = new MemberMockMvc(mockMvc, objectMapper);
        String email = "potato@gmail.com";
        token = memberMockMvc.getMockMemberToken(email);
        testMember = memberRepository.findMemberByEmail(email);
        organization = OrganizationCreator.create("potato", "감자", "개발의 감을 잡자", "https://potato.com", OrganizationCategory.NON_APPROVED_CIRCLE);
    }

    protected void cleanup() {
        memberRepository.deleteAll();
    }

}
