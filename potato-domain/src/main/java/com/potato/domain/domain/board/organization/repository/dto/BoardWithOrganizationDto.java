package com.potato.domain.domain.board.organization.repository.dto;

import com.potato.domain.domain.board.organization.OrganizationBoardCategory;
import com.potato.domain.domain.organization.OrganizationCategory;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardWithOrganizationDto {

    private final String orgSubDomain;
    private final String orgName;
    private final OrganizationCategory orgCategory;
    private final String orgProfileUrl;
    private final String orgDescription;
    private final int orgMembersCount;
    private final int orgFollowersCount;

    private final Long boardId;
    private final String boardTitle;
    private final String boardContent;
    private final OrganizationBoardCategory boardCategory;
    private final LocalDateTime boardStartDateTime;
    private final LocalDateTime boardEndDateTime;
    private final LocalDateTime createdDateTime;
    private final LocalDateTime lastModifiedDateTime;

    @QueryProjection
    public BoardWithOrganizationDto(String orgSubDomain, String orgName, OrganizationCategory orgCategory, String orgProfileUrl,
                                    String orgDescription, int orgMembersCount, int orgFollowersCount,
                                    Long boardId, String boardTitle, String boardContent, OrganizationBoardCategory boardCategory,
                                    LocalDateTime boardStartDateTime, LocalDateTime boardEndDateTime,
                                    LocalDateTime createdDateTime, LocalDateTime lastModifiedDateTime) {
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
        this.createdDateTime = createdDateTime;
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

}
