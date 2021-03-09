package com.potato.service.organization.dto.response;

import com.potato.domain.member.Member;
import com.potato.domain.member.MemberMajor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationFollowMemberResponse {

    private final Long id;

    private final String email;

    private final String name;

    private final String profileUrl;

    private final MemberMajor major;

    public static OrganizationFollowMemberResponse of(Member member) {
        return new OrganizationFollowMemberResponse(member.getId(), member.getEmail(), member.getName(), member.getProfileUrl(), member.getMajor());
    }

}
