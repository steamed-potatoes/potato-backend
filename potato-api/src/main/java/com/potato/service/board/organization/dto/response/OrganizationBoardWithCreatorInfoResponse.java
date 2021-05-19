package com.potato.service.board.organization.dto.response;

import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.member.Member;
import com.potato.domain.organization.Organization;
import com.potato.service.common.dto.response.BaseTimeResponse;
import com.potato.service.member.dto.response.MemberInfoResponse;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationBoardWithCreatorInfoResponse extends BaseTimeResponse {

    private OrganizationBoardInfoResponse board;

    private OrganizationInfoResponse organization;

    private MemberInfoResponse creator;

    private List<String> hashTags;

    @Builder
    private OrganizationBoardWithCreatorInfoResponse(OrganizationBoardInfoResponse board, OrganizationInfoResponse organization, MemberInfoResponse creator, List<String> hashTags) {
        this.board = board;
        this.organization = organization;
        this.creator = creator;
        this.hashTags = hashTags;
    }

    public static OrganizationBoardWithCreatorInfoResponse of(OrganizationBoard organizationBoard, Organization organization, Member creator, List<String> hashTags, List<String> imageUrlList) {
        OrganizationBoardWithCreatorInfoResponse response = OrganizationBoardWithCreatorInfoResponse.builder()
            .board(OrganizationBoardInfoResponse.of(organizationBoard, imageUrlList))
            .organization(OrganizationInfoResponse.of(organization))
            .creator(MemberInfoResponse.of(creator))
            .hashTags(hashTags)
            .build();
        response.setBaseTime(organizationBoard);
        return response;
    }

}
