package com.potato.service.boardv2.dto.response;

import com.potato.domain.boardV2.organization.OrganizationBoard;
import com.potato.domain.boardV2.organization.OrganizationBoardType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

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
