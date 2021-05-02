package com.potato.service.member;

import com.potato.domain.member.*;
import com.potato.exception.model.ConflictException;
import com.potato.exception.model.NotFoundException;
import com.potato.service.member.dto.request.SignUpMemberRequest;
import com.potato.service.member.dto.request.UpdateMemberRequest;
import com.potato.service.member.dto.response.MemberInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @AfterEach
    void cleanUp() {
        memberRepository.deleteAll();
    }

    @Test
    void 회원가입을_진행한다() {
        // given
        String email = "will.seungho@gmail.com";
        String name = "강승호";
        String profileUrl = "http://profile.com";
        MemberMajor major = MemberMajor.IT_ICT;
        Integer classNumber = 201610323;

        SignUpMemberRequest request = SignUpMemberRequest.testBuilder()
            .email(email)
            .name(name)
            .profileUrl(profileUrl)
            .major(major)
            .classNumber(classNumber)
            .build();

        // when
        memberService.signUpMember(request);

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

        SignUpMemberRequest request = SignUpMemberRequest.testBuilder()
            .email(email)
            .name("강승호")
            .build();

        // when & then
        assertThatThrownBy(
            () -> memberService.signUpMember(request)
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

}
