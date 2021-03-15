package com.potato.service.boardv2.dto.response;

import com.potato.domain.boardV2.organization.OrganizationBoard;
import com.potato.domain.member.Member;
import com.potato.domain.organization.Organization;
import com.potato.service.member.dto.response.MemberInfoResponse;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationBoardWithCreatorInfoResponse {

    private final OrganizationBoardInfoResponse board;

    private final OrganizationInfoResponse organization;

    private final MemberInfoResponse creator;

    public static OrganizationBoardWithCreatorInfoResponse of(OrganizationBoard organizationBoard, Organization organization, Member creator) {
        return new OrganizationBoardWithCreatorInfoResponse(OrganizationBoardInfoResponse.of(organizationBoard),
            OrganizationInfoResponse.of(organization), MemberInfoResponse.of(creator));
    }

}
