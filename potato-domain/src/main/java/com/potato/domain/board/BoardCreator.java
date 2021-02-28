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
            .category(Category.RECRUIT)
            .memberId(1L)
            .organizationId(1L)
            .build();
    }

    public static Board create(String title, String content, String imageUrl) {
        return Board.builder()
            .visible(Visible.PUBLIC)
            .title(title)
            .content(content)
            .imageUrl(imageUrl)
            .category(Category.RECRUIT)
            .memberId(1L)
            .organizationId(1L)
            .build();
    }

    public static Board createPrivate(String title) {
        return Board.builder()
            .visible(Visible.PRIVATE)
            .title(title)
            .content("content")
            .imageUrl("imageUrl")
            .category(Category.RECRUIT)
            .memberId(1L)
            .organizationId(1L)
            .build();
    }

}
