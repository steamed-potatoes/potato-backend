package com.potato.domain.domain.image;

import com.potato.domain.domain.BaseTimeEntity;
import com.potato.domain.domain.board.BoardType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BoardImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long boardId;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private BoardType type;

    @Column(nullable = false)
    private String imageUrl;

    private BoardImage(Long boardId, BoardType type, String imageUrl) {
        this.imageUrl = imageUrl;
        this.boardId = boardId;
        this.type = type;
    }

    public static BoardImage newInstance(Long boardId, BoardType type, String imageUrl) {
        return new BoardImage(boardId, type, imageUrl);
    }

    public static BoardImage testInstance(Long boardId, BoardType type, String imageUrl) {
        return new BoardImage(boardId, type, imageUrl);
    }


}
