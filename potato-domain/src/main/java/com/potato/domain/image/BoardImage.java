package com.potato.domain.image;

import com.potato.domain.BaseTimeEntity;
import com.potato.domain.board.BoardType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class BoardImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Long boardId;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private BoardType type;

    public BoardImage(String imageUrl, Long boardId, BoardType type) {
        this.imageUrl = imageUrl;
        this.boardId = boardId;
        this.type = type;
    }

    public static BoardImage newInstance(Long boardId, String imageUrl, BoardType type) {
        return new BoardImage(imageUrl, boardId, type);
    }

}
