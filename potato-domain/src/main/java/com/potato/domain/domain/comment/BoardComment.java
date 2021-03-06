package com.potato.domain.domain.comment;

import com.potato.common.exception.model.ForbiddenException;
import com.potato.domain.domain.BaseTimeEntity;
import com.potato.domain.domain.board.BoardType;
import com.potato.common.exception.ErrorCode;
import com.potato.common.exception.model.ConflictException;
import com.potato.common.exception.model.NotFoundException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private BoardType type;

    @Column(nullable = false)
    private Long boardId;

    @Column(nullable = false)
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
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

    @Column(nullable = false)
    private int commentLikeCounts;

    private BoardComment(BoardComment parentComment, BoardType type, Long boardId, Long memberId, String content, int depth) {
        this.parentComment = parentComment;
        this.type = type;
        this.boardId = boardId;
        this.memberId = memberId;
        this.content = content;
        this.depth = depth;
        this.isDeleted = false;
        this.commentLikeCounts = 0;
    }

    public static BoardComment newRootComment(BoardType type, Long boardId, Long memberId, String content) {
        return new BoardComment(null, type, boardId, memberId, content, 0);
    }

    public void addChildComment(Long memberId, String content) {
        if (!this.isRootComment()) {
            throw new ForbiddenException(String.format("댓글은 2 depth 까지 가능합니다. memberId: (%s) content: (%s)", memberId, content), ErrorCode.FORBIDDEN_COMMENT_DEPTH_EXCEPTION);
        }
        this.childComments.add(new BoardComment(this, this.type, this.boardId, memberId, content, this.depth + 1));
    }

    public boolean isRootComment() {
        return this.parentComment == null;
    }

    public void update(String content) {
        this.content = content;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void addLike(Long memberId) {
        if (isLike(memberId)) {
            throw new ConflictException(String.format("멤버 (%s)는 (%s) 댓글에 좋아요를 누른 상태입니다.", memberId, this.id));
        }
        BoardCommentLike boardCommentLike = BoardCommentLike.of(this, memberId);
        this.boardCommentLikeList.add(boardCommentLike);
        this.commentLikeCounts++;
    }

    public boolean isLike(Long memberId) {
        return this.boardCommentLikeList.stream()
            .anyMatch(boardCommentLike -> boardCommentLike.isSameMember(memberId));
    }

    public void cancelLike(Long memberId) {
        BoardCommentLike boardCommentLike = findBoardComment(memberId);
        this.boardCommentLikeList.remove(boardCommentLike);
        this.commentLikeCounts--;
    }

    private BoardCommentLike findBoardComment(Long memberId) {
        return this.boardCommentLikeList.stream()
            .filter(boardCommentLike -> boardCommentLike.isSameMember(memberId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(String.format("(%s)가 좋아요 한 댓글 (%s)을 찾을 수 없습니다.", memberId, this.id)));
    }

    List<Long> getAuthorIdsInChildren() {
        List<Long> authors = new ArrayList<>();
        authors.add(memberId);
        authors.addAll(childComments.stream()
            .map(BoardComment::getAuthorIdsInChildren)
            .flatMap(List::stream)
            .collect(Collectors.toList()));
        return authors;
    }

}
