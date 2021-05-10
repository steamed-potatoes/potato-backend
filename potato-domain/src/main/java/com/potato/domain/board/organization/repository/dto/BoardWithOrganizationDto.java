package com.potato.domain.board.organization.repository.dto;

import com.potato.domain.board.organization.OrganizationBoardCategory;
import com.potato.domain.organization.OrganizationCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardWithOrganizationDto {

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
    private String boardImageUrl;
    private OrganizationBoardCategory boardCategory;
    private LocalDateTime boardStartDateTime;
    private LocalDateTime boardEndDateTime;
    private LocalDateTime createdDateTime;
    private LocalDateTime lastModifiedDateTime;

}
