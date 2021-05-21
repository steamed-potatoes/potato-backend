package com.potato.domain.domain.board.organization.repository.dto;

import com.potato.domain.domain.board.organization.OrganizationBoardCategory;
import com.potato.domain.domain.organization.OrganizationCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private OrganizationBoardCategory boardCategory;
    private LocalDateTime boardStartDateTime;
    private LocalDateTime boardEndDateTime;
    private LocalDateTime createdDateTime;
    private LocalDateTime lastModifiedDateTime;

    private final List<String> imageUrls = new ArrayList<>();

    public BoardWithOrganizationDto setImageUrls(List<String> imageUrls) {
        this.imageUrls.addAll(imageUrls);
        return this;
    }

}
