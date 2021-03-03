package com.potato;

import com.potato.domain.member.Member;
import com.potato.domain.member.MemberCreator;
import com.potato.domain.member.MemberRepository;
import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationCreator;
import com.potato.domain.organization.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class OrganizationMemberSetUpTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    protected Long memberId;
    protected String subDomain = "subDomain";

    @BeforeEach
    void setUp() {
        Member member = memberRepository.save(MemberCreator.create("will.seungho@gmail.com"));
        memberId = member.getId();
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);
    }

    protected void cleanup() {
        organizationRepository.deleteAll();
        memberRepository.deleteAll();
    }

}
