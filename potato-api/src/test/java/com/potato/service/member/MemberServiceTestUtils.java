package com.potato.service.member;

import com.potato.domain.member.Member;
import com.potato.domain.member.MemberMajor;
import com.potato.service.member.dto.response.MemberInfoResponse;

import static org.assertj.core.api.Assertions.assertThat;

public final class MemberServiceTestUtils {

    static void assertMemberInfo(Member member, String email, String name, String profileUrl, MemberMajor major, Integer classNumber) {
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getName()).isEqualTo(name);
        assertThat(member.getProfileUrl()).isEqualTo(profileUrl);
        assertThat(member.getMajor()).isEqualTo(major);
        assertThat(member.getClassNumber()).isEqualTo(classNumber);
    }

    public static void assertMemberInfoResponse(MemberInfoResponse response, String email, String name, String profileUrl, MemberMajor major, Integer classNumber) {
        assertThat(response.getEmail()).isEqualTo(email);
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getProfileUrl()).isEqualTo(profileUrl);
        assertThat(response.getMajor()).isEqualTo(major.getName());
        assertThat(response.getClassNumber()).isEqualTo(classNumber);
    }

    public static void assertMemberInfoResponse(MemberInfoResponse response, Long memberId, String email) {
        assertThat(response.getId()).isEqualTo(memberId);
        assertThat(response.getEmail()).isEqualTo(email);
    }

    public static void assertMemberInfoResponse(MemberInfoResponse response, Long memberId) {
        assertThat(response.getId()).isEqualTo(memberId);
    }

}
