package com.potato.service.member;

import com.potato.domain.member.*;
import com.potato.service.member.dto.request.CreateMemberRequest;
import com.potato.service.member.dto.request.UpdateMemberRequest;
import com.potato.service.member.dto.response.MemberInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MemberServiceTest {

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

        CreateMemberRequest request = CreateMemberRequest.testBuilder()
            .email(email)
            .name(name)
            .profileUrl(profileUrl)
            .major(major)
            .build();

        // when
        memberService.createMember(request);

        // then
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(1);
        assertMemberInfo(memberList.get(0), email, name, profileUrl, major);
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
        assertThatThrownBy(() -> {
            memberService.createMember(request);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    private void assertMemberInfo(Member member, String email, String name, String profileUrl, MemberMajor major) {
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getName()).isEqualTo(name);
        assertThat(member.getProfileUrl()).isEqualTo(profileUrl);
        assertThat(member.getMajor()).isEqualTo(major);
    }

    @Test
    void 회원_정보를_불러온다() {
        // given
        String email = "will.seungho@gmail.com";
        String name = "강승호";
        String profileUrl = "http://profile.com";
        MemberMajor major = MemberMajor.IT_ICT;

        Member member = memberRepository.save(MemberCreator.create(email, name, profileUrl, major));

        // when
        MemberInfoResponse response = memberService.getMemberInfo(member.getId());

        // then
        assertThatMemberInfoResponse(response, email, name, profileUrl, major);
    }

    @Test
    void 존재하지_않는_멤버의_회원정보를_불러오면_에러가_발생한다() {
        // when & then
        assertThatThrownBy(() -> {
            memberService.getMemberInfo(999L);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    private void assertThatMemberInfoResponse(MemberInfoResponse response, String email, String name, String profileUrl, MemberMajor major) {
        assertThat(response.getEmail()).isEqualTo(email);
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getProfileUrl()).isEqualTo(profileUrl);
        assertThat(response.getMajor()).isEqualTo(major);
    }

    @Test
    void 회원정보를_수정한다() {
        // given
        Member member = memberRepository.save(MemberCreator.create(
            "123@gmail.com", "sunjo", "profile.com", MemberMajor.IT_ICT));

        String name = "유순조";
        String profileUrl = "http://profile.com";
        MemberMajor major = MemberMajor.IT_ICT;

        UpdateMemberRequest updateMemberRequest = UpdateMemberRequest.testBuilder()
            .name(name)
            .profileUrl(profileUrl)
            .major(major)
            .build();

        // when
        memberService.updateMemberInfo(updateMemberRequest, member.getId());

        // then
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(1);
        assertUpdateMemberInfo(memberList.get(0), name, profileUrl, major);
    }

    private void assertUpdateMemberInfo(Member member, String name, String profileUrl, MemberMajor major) {
        assertThat(member.getName()).isEqualTo(name);
        assertThat(member.getProfileUrl()).isEqualTo(profileUrl);
        assertThat(member.getMajor()).isEqualTo(major);
    }

    @Test
    void 특정_회원을_조회한다() {
        //given
        String email = "will.seungho@gmail.com";
        String name = "강승호";
        String profileUrl = "http://profile.com";
        MemberMajor major = MemberMajor.IT_ICT;

        Member member = memberRepository.save(MemberCreator.create(email, name, profileUrl, major));

        //when
        MemberInfoResponse response = memberService.getMemberOne(member.getId());

        //then
        List<Member> memberList = memberRepository.findAll();
        assertThatMemberInfoResponse(response, email, name, profileUrl, major);
    }

}
