package com.potato.api.helper.member;

import com.potato.api.service.member.dto.response.MajorInfoResponse;
import com.potato.domain.domain.member.Member;
import com.potato.domain.domain.member.MemberMajor;
import com.potato.api.service.member.dto.response.MemberInfoResponse;

import static org.assertj.core.api.Assertions.assertThat;

public final class MemberTestHelper {

    public static void assertMemberInfo(Member member, String email, String name, String profileUrl, MemberMajor major, Integer classNumber) {
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
        assertThat(response.getClassNumber()).isEqualTo(classNumber);
        assertMajorInfoResponse(response.getMajor(), major);
    }

    private static void assertMajorInfoResponse(MajorInfoResponse majorInfoResponse, MemberMajor major) {
        assertThat(majorInfoResponse.getMajorCode()).isEqualTo(major);
        assertThat(majorInfoResponse.getMajor()).isEqualTo(major.getName());
        assertThat(majorInfoResponse.getDepartment()).isEqualTo(major.getDepartment());
    }

    public static void assertMemberInfoResponse(MemberInfoResponse response, Long memberId, String email) {
        assertThat(response.getId()).isEqualTo(memberId);
        assertThat(response.getEmail()).isEqualTo(email);
    }

}
