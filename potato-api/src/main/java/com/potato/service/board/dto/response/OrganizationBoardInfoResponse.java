package com.potato.service.board.dto.response;

import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationBoardInfoResponse {

    private final Long id;

    private final String subDomain;

    private final String title;

    private final LocalDateTime startDateTime;

    private final LocalDateTime endDateTime;

    private final String content;

    private final String imageUrl;

    private final OrganizationBoardType type;

    public static OrganizationBoardInfoResponse of(OrganizationBoard organizationBoard) {
        return new OrganizationBoardInfoResponse(organizationBoard.getId(), organizationBoard.getSubDomain(), organizationBoard.getTitle(),
            organizationBoard.getStartDateTime(), organizationBoard.getEndDateTime(), organizationBoard.getContent(),
            organizationBoard.getImageUrl(), organizationBoard.getType());
    }

}
