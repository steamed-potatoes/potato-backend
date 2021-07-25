package com.potato.api.service.board.organization.dto.response;

import com.potato.api.service.common.dto.response.BaseTimeResponse;
import com.potato.domain.domain.board.organization.OrganizationBoardCategory;
import com.potato.domain.domain.board.organization.repository.dto.BoardWithOrganizationDto;
import com.potato.domain.domain.organization.OrganizationCategory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationBoardWithOrganizationResponse extends BaseTimeResponse {

    private String orgSubDomain;
    private String orgName;
    private OrganizationCategory orgCategory;
    private String orgProfileUrl;
    private String orgDescription;
    private int orgMembersCount;
    private int orgFollowersCount;

    private Long boardId;
    private String boardTitle;
    private String boardContent;
    private OrganizationBoardCategory boardCategory;
    private LocalDateTime boardStartDateTime;
    private LocalDateTime boardEndDateTime;
    private final List<String> imageUrls = new ArrayList<>();

    @Builder
    public OrganizationBoardWithOrganizationResponse(String orgSubDomain, String orgName, OrganizationCategory orgCategory,
                                                     String orgProfileUrl, String orgDescription, int orgMembersCount, int orgFollowersCount,
                                                     Long boardId, String boardTitle, String boardContent, OrganizationBoardCategory boardCategory,
                                                     LocalDateTime boardStartDateTime, LocalDateTime boardEndDateTime) {
        this.orgSubDomain = orgSubDomain;
        this.orgName = orgName;
        this.orgCategory = orgCategory;
        this.orgProfileUrl = orgProfileUrl;
        this.orgDescription = orgDescription;
        this.orgMembersCount = orgMembersCount;
        this.orgFollowersCount = orgFollowersCount;
        this.boardId = boardId;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardCategory = boardCategory;
        this.boardStartDateTime = boardStartDateTime;
        this.boardEndDateTime = boardEndDateTime;
    }

    public static OrganizationBoardWithOrganizationResponse of(BoardWithOrganizationDto dto, List<String> imageUrls) {
        OrganizationBoardWithOrganizationResponse response = OrganizationBoardWithOrganizationResponse.builder()
            .orgSubDomain(dto.getOrgSubDomain())
            .orgName(dto.getOrgName())
            .orgCategory(dto.getOrgCategory())
            .orgProfileUrl(dto.getOrgProfileUrl())
            .orgDescription(dto.getOrgDescription())
            .orgMembersCount(dto.getOrgMembersCount())
            .orgFollowersCount(dto.getOrgFollowersCount())
            .boardId(dto.getBoardId())
            .boardTitle(dto.getBoardTitle())
            .boardContent(dto.getBoardContent())
            .boardCategory(dto.getBoardCategory())
            .boardStartDateTime(dto.getBoardStartDateTime())
            .boardEndDateTime(dto.getBoardEndDateTime())
            .build();
        response.setBaseTime(dto.getCreatedDateTime(), dto.getLastModifiedDateTime());
        return response;
    }

}
