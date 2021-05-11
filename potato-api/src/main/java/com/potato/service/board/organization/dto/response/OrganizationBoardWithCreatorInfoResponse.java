package com.potato.service.board.organization.dto.response;

import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.member.Member;
import com.potato.domain.organization.Organization;
import com.potato.service.member.dto.response.MemberInfoResponse;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationBoardWithCreatorInfoResponse {

    private OrganizationBoardInfoResponse board;

    private OrganizationInfoResponse organization;

    private MemberInfoResponse creator;

    private List<String> hashTags;

    public static OrganizationBoardWithCreatorInfoResponse of(OrganizationBoard organizationBoard, Organization organization, Member creator, List<String> hashTags) {
        return new OrganizationBoardWithCreatorInfoResponse(OrganizationBoardInfoResponse.of(organizationBoard),
            OrganizationInfoResponse.of(organization), MemberInfoResponse.of(creator), hashTags);
    }

}
