package com.potato.service.organization.dto.response;

import com.potato.domain.member.Member;
import com.potato.domain.organization.Organization;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationWithMembersInfoResponse {

    private final OrganizationInfoResponse organization;

    private final List<MemberInOrganizationResponse> members = new ArrayList<>();

    public static OrganizationWithMembersInfoResponse of(Organization organization, List<Member> memberList) {
        List<MemberInOrganizationResponse> memberInOrganizationResponses = memberList.stream()
            .map(member -> MemberInOrganizationResponse.of(member, organization.getRoleOfMember(member.getId())))
            .collect(Collectors.toList());
        OrganizationWithMembersInfoResponse response = new OrganizationWithMembersInfoResponse(OrganizationInfoResponse.of(organization));
        response.members.addAll(memberInOrganizationResponses);
        return response;
    }

}