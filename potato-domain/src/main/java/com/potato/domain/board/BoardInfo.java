package com.potato.domain.board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class BoardInfo {

    @Column(nullable = false)
    private String title;

    private String content;

    private String imageUrl;

    private BoardInfo(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public static BoardInfo of(String title, String content, String imageUrl) {
        return new BoardInfo(title, content, imageUrl);
    }

}
