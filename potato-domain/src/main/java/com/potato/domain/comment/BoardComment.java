package com.potato.domain.comment;

import com.potato.domain.BaseTimeEntity;
import com.potato.exception.ValidationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.potato.exception.ErrorCode.COMMENT_DEPTH_EXCEPTION;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    indexes = @Index(name = "idx_board_comment_1", columnList = "type,boardId")
)
public class BoardComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private BoardCommentType type;

    @Column(nullable = false)
    private Long boardId;

    @Column(nullable = false)
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_comment_id")
    private BoardComment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.PERSIST)
    private final List<BoardComment> childComments = new ArrayList<>();

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int depth;

    @Column(nullable = false)
    private boolean isDeleted;

    private BoardComment(BoardComment parentComment, BoardCommentType type, Long boardId, Long memberId, String content, int depth) {
        this.parentComment = parentComment;
        this.type = type;
        this.boardId = boardId;
        this.memberId = memberId;
        this.content = content;
        this.depth = depth;
        this.isDeleted = false;
    }

    public static BoardComment newRootComment(BoardCommentType type, Long boardId, Long memberId, String content) {
        return new BoardComment(null, type, boardId, memberId, content, 0);
    }

    public void addChildComment(Long memberId, String content) {
        if (!this.isRootComment()) {
            throw new ValidationException(String.format("댓글은 2 depth 까지 가능합니다. memberId: (%s) content: (%s)", memberId, content), COMMENT_DEPTH_EXCEPTION);
        }
        this.childComments.add(new BoardComment(this, this.type, this.boardId, memberId, content, this.depth + 1));
    }

    public boolean isRootComment() {
        return this.parentComment == null;
    }

    public void delete() {
        this.isDeleted = true;
    }

}
