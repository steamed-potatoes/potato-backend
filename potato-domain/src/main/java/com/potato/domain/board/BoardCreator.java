package com.potato.domain.board;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Test Helper Class
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCreator {

    public static Board create(String subDomain, Long memberId, String title) {
        return Board.builder()
            .subDomain(subDomain)
            .memberId(memberId)
            .visible(Visible.PUBLIC)
            .title(title)
            .content("content")
            .imageUrl("imageUrl")
            .category(Category.RECRUIT)
            .build();
    }

    public static Board create(String subDomain, Long memberId, String title, String content, String imageUrl) {
        return Board.builder()
            .subDomain(subDomain)
            .memberId(memberId)
            .visible(Visible.PUBLIC)
            .title(title)
            .content(content)
            .imageUrl(imageUrl)
            .category(Category.RECRUIT)
            .build();
    }

    public static Board createPrivate(String subDomain, Long memberId, String title) {
        return Board.builder()
            .subDomain(subDomain)
            .memberId(memberId)
            .visible(Visible.PRIVATE)
            .title(title)
            .content("content")
            .imageUrl("imageUrl")
            .category(Category.RECRUIT)
            .build();
    }

}
