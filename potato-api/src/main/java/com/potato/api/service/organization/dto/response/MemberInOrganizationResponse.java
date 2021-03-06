package com.potato.api.service.organization.dto.response;

import com.potato.domain.domain.member.Member;
import com.potato.domain.domain.organization.OrganizationRole;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberInOrganizationResponse {

    private Long memberId;

    private String email;

    private String name;

    private String major;

    private String profileUrl;

    private OrganizationRole role;

    static MemberInOrganizationResponse of(Member member, OrganizationRole role) {
        return new MemberInOrganizationResponse(member.getId(), member.getEmail(), member.getName(),
            member.getMajorName(), member.getProfileUrl(), role);
    }

}
