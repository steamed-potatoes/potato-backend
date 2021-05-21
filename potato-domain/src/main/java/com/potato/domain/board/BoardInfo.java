package com.potato.domain.board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardInfo boardInfo = (BoardInfo) o;
        return Objects.equals(title, boardInfo.title) && Objects.equals(content, boardInfo.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content);
    }

}
