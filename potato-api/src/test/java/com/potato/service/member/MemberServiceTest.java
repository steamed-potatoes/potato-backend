package com.potato.service.member;

import com.potato.domain.member.*;
import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationCreator;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.exception.ConflictException;
import com.potato.exception.NotFoundException;
import com.potato.service.member.dto.request.CreateMemberRequest;
import com.potato.service.member.dto.request.UpdateMemberRequest;
import com.potato.service.member.dto.response.MemberInfoResponse;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static com.potato.service.member.MemberServiceTestUtils.assertMemberInfo;
import static com.potato.service.member.MemberServiceTestUtils.assertMemberInfoResponse;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @AfterEach
    void cleanUp() {
        memberRepository.deleteAll();
        organizationRepository.deleteAll();
    }

    @Test
    void 회원가입을_진행한다() {
        // given
        String email = "will.seungho@gmail.com";
        String name = "강승호";
        String profileUrl = "http://profile.com";
        MemberMajor major = MemberMajor.IT_ICT;
        Integer classNumber = 201610323;

        CreateMemberRequest request = CreateMemberRequest.testBuilder()
            .email(email)
            .name(name)
            .profileUrl(profileUrl)
            .major(major)
            .classNumber(classNumber)
            .build();

        // when
        memberService.createMember(request);

        // then
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(1);
        assertMemberInfo(memberList.get(0), email, name, profileUrl, major, classNumber);
    }

    @Test
    void 이미_회원가입한_회원일_경우_에러가_발생한다() {
        // given
        String email = "will.seungho@gmail.com";
        memberRepository.save(MemberCreator.create(email));

        CreateMemberRequest request = CreateMemberRequest.testBuilder()
            .email(email)
            .name("강승호")
            .build();

        // when & then
        assertThatThrownBy(
            () -> memberService.createMember(request)
        ).isInstanceOf(ConflictException.class);
    }

    @Test
    void 회원_정보를_불러온다() {
        // given
        String email = "will.seungho@gmail.com";
        String name = "강승호";
        String profileUrl = "http://profile.com";
        MemberMajor major = MemberMajor.IT_ICT;
        Integer classNumber = 201610323;

        Member member = memberRepository.save(MemberCreator.create(email, name, profileUrl, major, classNumber));

        // when
        MemberInfoResponse response = memberService.getMemberInfo(member.getId());

        // then
        assertMemberInfoResponse(response, email, name, profileUrl, major, classNumber);
    }

    @Test
    void 존재하지_않는_멤버의_회원정보를_불러오면_에러가_발생한다() {
        // when & then
        assertThatThrownBy(
            () -> memberService.getMemberInfo(999L)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 회원정보를_수정한다() {
        // given
        Member member = memberRepository.save(MemberCreator.create(
            "123@gmail.com", "sunjo", "profile.com", MemberMajor.IT_ICT, 201610323));

        String name = "유순조";
        String profileUrl = "http://profile.com";
        MemberMajor major = MemberMajor.IT_ICT;
        Integer classNumber = 201610323;

        UpdateMemberRequest updateMemberRequest = UpdateMemberRequest.testBuilder()
            .name(name)
            .profileUrl(profileUrl)
            .major(major)
            .classNumber(classNumber)
            .build();

        // when
        memberService.updateMemberInfo(updateMemberRequest, member.getId());

        // then
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(1);
        assertMemberInfo(memberList.get(0), member.getEmail(), name, profileUrl, major, classNumber);
    }

    @Test
    void 멤버가_팔로우한_조직들을_가져온다() {
        //given
        Member member = MemberCreator.create("tnswh2023@naver.com");
        memberRepository.save(member);

        Organization organization1 = OrganizationCreator.create("감자1");
        Organization organization2 = OrganizationCreator.create("감자2");

        organization1.addFollow(member.getId());
        organization2.addFollow(member.getId());
        organizationRepository.saveAll(Arrays.asList(organization1, organization2));

        //when
        List<OrganizationInfoResponse> responses = memberService.getOrganizationFollower(member.getId());

        //then
        assertThat(responses.get(0).getSubDomain()).isEqualTo("감자1");
        assertThat(responses.get(1).getSubDomain()).isEqualTo("감자2");
    }

    @Test
    void 멤버가_팔로우한_조직이_없을_경우_빈배열을_반환한다() {
        //given
        Member member = MemberCreator.create("tnswh2023@naver.com");
        memberRepository.save(member);

        //when
        List<OrganizationInfoResponse> responses = memberService.getOrganizationFollower(member.getId());

        //then
        assertThat(responses).isEmpty();
    }

}
