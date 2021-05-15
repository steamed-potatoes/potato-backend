package com.potato.service.board.organization.dto.response;

import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardCategory;
import com.potato.service.common.dto.response.BaseTimeResponse;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationBoardInfoResponse extends BaseTimeResponse {

    private Long id;

    private String subDomain;

    private String title;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private String content;

    private String imageUrl;

    private int likesCount;

    private OrganizationBoardCategory type;

    @Builder
    private OrganizationBoardInfoResponse(Long id, String subDomain, String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String content, String imageUrl, int likesCount, OrganizationBoardCategory type) {
        this.id = id;
        this.subDomain = subDomain;
        this.title = title;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.content = content;
        this.imageUrl = imageUrl;
        this.likesCount = likesCount;
        this.type = type;
    }

    public static OrganizationBoardInfoResponse of(OrganizationBoard organizationBoard) {
        OrganizationBoardInfoResponse organizationBoardInfoResponse = OrganizationBoardInfoResponse.builder()
            .id(organizationBoard.getId())
            .subDomain(organizationBoard.getSubDomain())
            .title(organizationBoard.getTitle())
            .content(organizationBoard.getContent())
            .startDateTime(organizationBoard.getStartDateTime())
            .endDateTime(organizationBoard.getEndDateTime())
            .imageUrl(organizationBoard.getImageUrl())
            .likesCount(organizationBoard.getLikesCount())
            .type(organizationBoard.getCategory())
            .build();
        organizationBoardInfoResponse.setBaseTime(organizationBoard);
        return organizationBoardInfoResponse;
    }

}
