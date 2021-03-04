package com.potato.domain.comment;

import com.potato.domain.BaseTimeEntity;
import com.potato.exception.ValidationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BoardComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long boardId;

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_comment_id")
    private BoardComment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.PERSIST)
    private final List<BoardComment> childComments = new ArrayList<>();

    private String content;

    private boolean isDeleted;

    private BoardComment(BoardComment parentComment, Long boardId, Long memberId, String content) {
        this.parentComment = parentComment;
        this.boardId = boardId;
        this.memberId = memberId;
        this.content = content;
        this.isDeleted = false;
    }

    public static BoardComment newRootComment(Long boardId, Long memberId, String content) {
        return new BoardComment(null, boardId, memberId, content);
    }

    public void addChildComment(Long memberId, String content) {
        if (!this.isRootComment()) {
            throw new ValidationException(String.format("댓글은 2 depth 까지 가능합니다. memberId: (%s) content: (%s)", memberId, content));
        }
        this.childComments.add(new BoardComment(this, this.boardId, memberId, content));
    }

    public boolean isRootComment() {
        return this.parentComment == null;
    }

}
