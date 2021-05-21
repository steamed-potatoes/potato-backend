package com.potato.api.service.member;

import com.potato.api.service.member.dto.request.SignUpMemberRequest;
import com.potato.api.service.member.dto.request.UpdateMemberRequest;
import com.potato.domain.domain.member.Member;
import com.potato.domain.domain.member.MemberCreator;
import com.potato.domain.domain.member.MemberMajor;
import com.potato.domain.domain.member.MemberRepository;
import com.potato.common.exception.model.ConflictException;
import com.potato.common.exception.model.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.potato.api.service.member.MemberServiceTestUtils.assertMemberInfo;
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
    void 회원가입을_성공하면_DB_에_회원정보가_저장된다() {
        // given
        String email = "will.seungho@gmail.com";
        String name = "강승호";
        String profileUrl = "https://profile.com";
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
    void 회원가입시_이미_회원일_경우_CONFLICT_EXCEPTION_에러가_발생한다() {
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
    void 존재하지_않는_멤버의_회원정보를_불러오는_요청시_에러가_발생한다() {
        // when & then
        assertThatThrownBy(
            () -> memberService.getMemberInfo(999L)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 회원정보를_수정하면_DB_에_저장된_회원정보가_수정된다() {
        // given
        Member member = memberRepository.save(MemberCreator.create("user@gmail.com"));

        String name = "유순조";
        String profileUrl = "https://profile.com";
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
    void 존재하지_않는_유저를_수정하려_요청하면_에러가_발생한다() {
        UpdateMemberRequest updateMemberRequest = UpdateMemberRequest.testBuilder()
            .name("강승호")
            .major(MemberMajor.IT_ICT)
            .classNumber(201610302)
            .build();

        // when & then
        assertThatThrownBy(() -> memberService.updateMemberInfo(updateMemberRequest, 999L)).isInstanceOf(NotFoundException.class);
    }

}
