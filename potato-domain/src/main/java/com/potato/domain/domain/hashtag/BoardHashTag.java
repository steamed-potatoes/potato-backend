package com.potato.domain.domain.hashtag;

import com.potato.domain.domain.BaseTimeEntity;
import com.potato.domain.domain.board.BoardType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(indexes = {
    @Index(name = "idx_board_hash_tag_1", columnList = "boardId,type")
})
public class BoardHashTag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private BoardType type;

    @Column(nullable = false)
    private Long boardId;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false, length = 50)
    private String hashTag;

    private BoardHashTag(BoardType type, Long boardId, Long memberId, String hashTag) {
        this.type = type;
        this.boardId = boardId;
        this.memberId = memberId;
        this.hashTag = hashTag;
    }

    public static BoardHashTag newInstance(BoardType type, Long boardId, Long memberId, String hashTag) {
        return new BoardHashTag(type, boardId, memberId, hashTag);
    }

}
