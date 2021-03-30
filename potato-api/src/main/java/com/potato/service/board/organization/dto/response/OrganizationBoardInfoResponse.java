package com.potato.service.board.organization.dto.response;

import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardType;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationBoardInfoResponse {

    private Long id;

    private String subDomain;

    private String title;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private String content;

    private String imageUrl;

    private int likesCount;

    private OrganizationBoardType type;

    public static OrganizationBoardInfoResponse of(OrganizationBoard organizationBoard) {
        return new OrganizationBoardInfoResponse(organizationBoard.getId(), organizationBoard.getSubDomain(), organizationBoard.getTitle(),
            organizationBoard.getStartDateTime(), organizationBoard.getEndDateTime(), organizationBoard.getContent(),
            organizationBoard.getImageUrl(), organizationBoard.getLikesCount(), organizationBoard.getType());
    }

}
