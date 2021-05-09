package com.potato.domain.comment;

import com.potato.domain.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class BoardCommentLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_comment_id")
    private BoardComment boardComment;

    private Long memberId;

    public BoardCommentLike(BoardComment boardComment, Long memberId) {
        this.boardComment = boardComment;
        this.memberId = memberId;
    }

    public static BoardCommentLike of(BoardComment boardComment, Long memberId) {
        return new BoardCommentLike(boardComment, memberId);
    }

    public boolean isSameMember(Long memberId) {
        return this.memberId.equals(memberId);
    }
}
