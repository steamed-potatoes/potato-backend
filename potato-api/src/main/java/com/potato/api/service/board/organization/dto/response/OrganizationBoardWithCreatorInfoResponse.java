package com.potato.api.service.board.organization.dto.response;

import com.potato.domain.domain.board.organization.OrganizationBoard;
import com.potato.domain.domain.member.Member;
import com.potato.domain.domain.organization.Organization;
import com.potato.api.service.common.dto.response.BaseTimeResponse;
import com.potato.api.service.member.dto.response.MemberInfoResponse;
import com.potato.api.service.organization.dto.response.OrganizationInfoResponse;
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

    private List<String> imageUrlList;

    private Boolean isLike;

    @Builder
    private OrganizationBoardWithCreatorInfoResponse(OrganizationBoardInfoResponse board, OrganizationInfoResponse organization,
                                                     MemberInfoResponse creator, List<String> hashTags, List<String> imageUrlList, boolean isLike) {
        this.board = board;
        this.organization = organization;
        this.creator = creator;
        this.hashTags = hashTags;
        this.imageUrlList = imageUrlList;
        this.isLike = isLike;
    }

    public static OrganizationBoardWithCreatorInfoResponse of(OrganizationBoard organizationBoard, Organization organization, Member creator, List<String> hashTags, List<String> imageUrlList, boolean isLike) {
        OrganizationBoardWithCreatorInfoResponse response = OrganizationBoardWithCreatorInfoResponse.builder()
            .board(OrganizationBoardInfoResponse.of(organizationBoard))
            .organization(OrganizationInfoResponse.of(organization))
            .creator(MemberInfoResponse.of(creator))
            .hashTags(hashTags)
            .imageUrlList(imageUrlList)
            .isLike(isLike)
            .build();
        response.setBaseTime(organizationBoard);
        return response;
    }

}
