package com.potato.domain.comment;

import com.potato.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BoardCommentLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_comment_id", nullable = false)
    private BoardComment boardComment;

    @Column(nullable = false)
    private Long memberId;

    private BoardCommentLike(BoardComment boardComment, Long memberId) {
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
