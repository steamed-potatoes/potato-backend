package com.potato.service.board.organization.dto.response;

import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardCategory;
import com.potato.service.common.dto.response.BaseTimeResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationBoardInfoResponseWithImage extends BaseTimeResponse {

    private Long id;

    private String subDomain;

    private String title;

    private String content;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private int likesCount;

    private OrganizationBoardCategory type;

    private List<String> imageUrlList = new ArrayList<>();

    @Builder
    private OrganizationBoardInfoResponseWithImage(Long id, String subDomain, String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String content, int likesCount, OrganizationBoardCategory type, List<String> imageUrlList) {
        this.id = id;
        this.subDomain = subDomain;
        this.title = title;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.content = content;
        this.likesCount = likesCount;
        this.type = type;
        this.imageUrlList = imageUrlList;
    }

    public static OrganizationBoardInfoResponseWithImage of(OrganizationBoard organizationBoard, List<String> imageUrlList) {
        OrganizationBoardInfoResponseWithImage organizationBoardInfoResponseWithImage = OrganizationBoardInfoResponseWithImage.builder()
            .id(organizationBoard.getId())
            .subDomain(organizationBoard.getSubDomain())
            .title(organizationBoard.getTitle())
            .content(organizationBoard.getContent())
            .startDateTime(organizationBoard.getStartDateTime())
            .endDateTime(organizationBoard.getEndDateTime())
            .likesCount(organizationBoard.getLikesCount())
            .type(organizationBoard.getCategory())
            .imageUrlList(imageUrlList)
            .build();
        organizationBoardInfoResponseWithImage.setBaseTime(organizationBoard);
        return organizationBoardInfoResponseWithImage;
    }

}
