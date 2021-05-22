package com.potato.api.service.comment.dto.response;

import com.potato.api.service.common.dto.response.BaseTimeResponse;
import com.potato.domain.domain.board.BoardType;
import com.potato.domain.domain.comment.BoardComment;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCommentResponse extends BaseTimeResponse {

    private Long id;

    private BoardType type;

    private Long boardId;

    private Long memberId;

    private String content;

    private int boardCommentLikeCounts;

    private Boolean isLike;

    private final List<BoardCommentResponse> children = new ArrayList<>();

    @Builder
    private BoardCommentResponse(Long id, BoardType type, Long boardId, Long memberId, String content, int boardCommentLikeCounts, boolean isLike) {
        this.id = id;
        this.type = type;
        this.boardId = boardId;
        this.memberId = memberId;
        this.content = content;
        this.boardCommentLikeCounts = boardCommentLikeCounts;
        this.isLike = isLike;
    }

    public static BoardCommentResponse of(BoardComment comment, Long memberId) {
        BoardCommentResponse response = activeOrInActiveComment(comment, memberId);
        List<BoardCommentResponse> childResponses = comment.getChildComments().stream()
            .map(boardComment -> BoardCommentResponse.of(boardComment, memberId))
            .collect(Collectors.toList());
        response.children.addAll(childResponses);
        response.setBaseTime(comment);
        return response;
    }

    private static BoardCommentResponse activeOrInActiveComment(BoardComment comment, Long memberId) {
        if (comment.isDeleted()) {
            return inActiveComment(comment);
        }
        return activeComment(comment, memberId);
    }

    private static BoardCommentResponse inActiveComment(BoardComment comment) {
        return BoardCommentResponse.builder()
            .id(comment.getId())
            .type(comment.getType())
            .boardId(comment.getBoardId())
            .memberId(null)
            .content("삭제된 메시지입니다")
            .boardCommentLikeCounts(0)
            .isLike(false)
            .build();
    }

    private static BoardCommentResponse activeComment(BoardComment comment, Long memberId) {
        return BoardCommentResponse.builder()
            .id(comment.getId())
            .type(comment.getType())
            .boardId(comment.getBoardId())
            .memberId(comment.getMemberId())
            .content(comment.getContent())
            .boardCommentLikeCounts(comment.getCommentLikeCounts())
            .isLike(comment.isLike(memberId))
            .build();
    }

}
