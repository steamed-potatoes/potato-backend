package com.potato.domain.board.organization;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationBoardCreator {

    public static OrganizationBoard create(String subDomain, Long memberId, String title, OrganizationBoardType type) {
        return OrganizationBoard.builder()
            .subDomain(subDomain)
            .memberId(memberId)
            .title(title)
            .type(type)
            .startDateTime(LocalDateTime.of(2021, 3, 5, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 3, 7, 0, 0))
            .build();
    }

    public static OrganizationBoard create(String subDomain, Long memberId, String title, LocalDateTime startDateTime, LocalDateTime endDateTime, OrganizationBoardType type) {
        return OrganizationBoard.builder()
            .subDomain(subDomain)
            .memberId(memberId)
            .title(title)
            .type(type)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .build();
    }

}
