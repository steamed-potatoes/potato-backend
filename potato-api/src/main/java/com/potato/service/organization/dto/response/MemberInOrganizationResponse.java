package com.potato.service.organization.dto.response;

import com.potato.domain.member.Member;
import com.potato.domain.member.MemberMajor;
import com.potato.domain.organization.OrganizationRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberInOrganizationResponse {

    private final Long id;

    private final String email;

    private final String name;

    private final MemberMajor major;

    private final String profileUrl;

    private final OrganizationRole role;

    static MemberInOrganizationResponse of(Member member, OrganizationRole role) {
        return new MemberInOrganizationResponse(member.getId(), member.getEmail(), member.getName(), member.getMajor(), member.getProfileUrl(), role);
    }

}
