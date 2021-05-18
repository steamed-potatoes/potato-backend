package com.potato.domain.board;

import com.potato.domain.BaseTimeEntity;
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

    public BoardImage(String imageUrl, Long boardId) {
        this.imageUrl = imageUrl;
        this.boardId = boardId;
    }

    public static BoardImage newInstance(Long boardId, String imageUrl) {
        return new BoardImage(imageUrl, boardId);
    }

}
