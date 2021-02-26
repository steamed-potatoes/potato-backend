package com.potato.service.organization.dto.response;

import com.potato.domain.member.Member;
import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationDetailInfoResponse {

    private final Long id;

    private final String subDomain;

    private final String name;

    private final String description;

    private final String profileUrl;

    private final int membersCount;

    private final OrganizationCategory category;

    private final List<MemberInOrganizationResponse> members = new ArrayList<>();

    public static OrganizationDetailInfoResponse of(Organization organization, List<Member> memberList) {
        List<MemberInOrganizationResponse> memberInOrganizationResponses = memberList.stream()
            .map(member -> MemberInOrganizationResponse.of(member, organization.getRoleOfMember(member.getId())))
            .collect(Collectors.toList());
        OrganizationDetailInfoResponse response = new OrganizationDetailInfoResponse(organization.getId(), organization.getSubDomain(),
            organization.getName(), organization.getDescription(), organization.getProfileUrl(), organization.getMembersCount(),
            organization.getCategory());
        response.members.addAll(memberInOrganizationResponses);
        return response;
    }

}
