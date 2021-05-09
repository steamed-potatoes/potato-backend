package com.potato.domain.comment;

import com.potato.domain.BaseTimeEntity;
import com.potato.exception.ErrorCode;
import com.potato.exception.model.ConflictException;
import com.potato.exception.model.NotFoundException;
import com.potato.exception.model.ValidationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "boardComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<BoardCommentLike> boardCommentLikeList = new ArrayList<>();

    private int commentLikeCounts;

    private BoardComment(BoardComment parentComment, BoardCommentType type, Long boardId, Long memberId, String content, int depth) {
        this.parentComment = parentComment;
        this.type = type;
        this.boardId = boardId;
        this.memberId = memberId;
        this.content = content;
        this.depth = depth;
        this.isDeleted = false;
        this.commentLikeCounts = 0;
    }

    public static BoardComment newRootComment(BoardCommentType type, Long boardId, Long memberId, String content) {
        return new BoardComment(null, type, boardId, memberId, content, 0);
    }

    public void addChildComment(Long memberId, String content) {
        if (!this.isRootComment()) {
            throw new ValidationException(String.format("댓글은 2 depth 까지 가능합니다. memberId: (%s) content: (%s)", memberId, content), ErrorCode.VALIDATION_COMMENT_DEPTH_EXCEPTION);
        }
        this.childComments.add(new BoardComment(this, this.type, this.boardId, memberId, content, this.depth + 1));
    }

    public boolean isRootComment() {
        return this.parentComment == null;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void addLike(Long memberId) {
        if (alreadyLike(memberId)) {
            throw new ConflictException(String.format("멤버 (%s)는 (%s) 댓글에 좋아요를 누른 상태입니다.", memberId, this.id));
        }
        validateNotDeletedBoardComment();
        BoardCommentLike boardCommentLike = BoardCommentLike.of(this, memberId);
        this.boardCommentLikeList.add(boardCommentLike);
        this.commentLikeCounts++;
    }

    private boolean alreadyLike(Long memberId) {
        return this.boardCommentLikeList.stream()
            .anyMatch(boardCommentLike -> boardCommentLike.isSameMember(memberId));
    }

    public void deleteLike(Long memberId) {
        validateNotDeletedBoardComment();
        BoardCommentLike boardCommentLike = findBoardComment(memberId);
        this.boardCommentLikeList.remove(boardCommentLike);
        this.commentLikeCounts--;
    }

    private void validateNotDeletedBoardComment() {
        if (this.isDeleted) {
            throw new NotFoundException(String.format("댓글 (%s)는 삭제된 댓글입니다.", this.id));
        }
    }

    private BoardCommentLike findBoardComment(Long memberId) {
        return this.boardCommentLikeList.stream()
            .filter(boardCommentLike -> boardCommentLike.isSameMember(memberId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(String.format("(%s)가 좋아요 한 댓글 (%s)을 찾을 수 없습니다.", memberId, this.id)));
    }

}
