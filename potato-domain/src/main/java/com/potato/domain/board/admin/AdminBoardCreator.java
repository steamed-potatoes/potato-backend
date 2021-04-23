package com.potato.domain.board.admin;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminBoardCreator {

    public static AdminBoard create(String title, Long memberId) {
        return AdminBoard.builder()
            .administratorId(memberId)
            .title(title)
            .startDateTime(LocalDateTime.of(2021, 3, 5, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 3, 7, 0, 0))
            .build();
    }

}
