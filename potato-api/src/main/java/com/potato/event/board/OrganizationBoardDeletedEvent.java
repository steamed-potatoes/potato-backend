package com.potato.event.board;

import com.potato.domain.board.organization.OrganizationBoard;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationBoardDeletedEvent {

    private final OrganizationBoard organizationBoard;

    private final Long memberId;

    public static OrganizationBoardDeletedEvent of(OrganizationBoard organizationBoard, Long memberId) {
        return new OrganizationBoardDeletedEvent(organizationBoard, memberId);
    }

}
