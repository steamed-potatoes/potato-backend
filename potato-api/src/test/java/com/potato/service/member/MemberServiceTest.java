package com.potato.service.member;

import com.potato.domain.member.Member;
import com.potato.domain.member.MemberCreator;
import com.potato.domain.member.MemberMajor;
import com.potato.domain.member.MemberRepository;
import com.potato.service.member.dto.request.CreateMemberRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

}
