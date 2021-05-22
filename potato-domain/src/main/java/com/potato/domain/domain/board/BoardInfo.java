package com.potato.domain.domain.board;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class BoardInfo {

    @Column(nullable = false)
    private String title;

    private String content;

    private BoardInfo(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static BoardInfo of(String title, String content) {
        return new BoardInfo(title, content);
    }

}
