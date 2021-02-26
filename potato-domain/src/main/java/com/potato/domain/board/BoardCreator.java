package com.potato.domain.board;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCreator {

    public static Board create(String title) {
        return Board.builder()
            .visible(Visible.PUBLIC)
            .title(title)
            .content("content")
            .imageUrl("imageUrl")
            .memberId(1L)
            .organizationId(1L)
            .build();
    }

}
