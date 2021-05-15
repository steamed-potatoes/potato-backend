package com.potato.service.comment.dto.response;

import com.potato.domain.board.BoardType;
import com.potato.domain.comment.BoardComment;
import com.potato.service.common.dto.response.BaseTimeResponse;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
public class BoardCommentResponse extends BaseTimeResponse {

    private final Long id;

    private final BoardType type;

    private final Long boardId;

    private final Long memberId;

    private final String content;

    private final int boardCommentLikeCounts;

    private final List<BoardCommentResponse> children = new ArrayList<>();

    @Builder
    private BoardCommentResponse(Long id, BoardType type, Long boardId, Long memberId, String content, int boardCommentLikeCounts) {
        this.id = id;
        this.type = type;
        this.boardId = boardId;
        this.memberId = memberId;
        this.content = content;
        this.boardCommentLikeCounts = boardCommentLikeCounts;
    }

    public static BoardCommentResponse of(BoardComment comment) {
        BoardCommentResponse response = activeOrInActiveComment(comment);
        List<BoardCommentResponse> childResponses = comment.getChildComments().stream()
            .map(BoardCommentResponse::of)
            .collect(Collectors.toList());
        response.children.addAll(childResponses);
        response.setBaseTime(comment);
        return response;
    }

    private static BoardCommentResponse activeOrInActiveComment(BoardComment comment) {
        if (comment.isDeleted()) {
            return inActiveComment(comment);
        }
        return activeComment(comment);
    }

    private static BoardCommentResponse inActiveComment(BoardComment comment) {
        return BoardCommentResponse.builder()
            .id(comment.getId())
            .type(comment.getType())
            .boardId(comment.getBoardId())
            .memberId(null)
            .content("삭제된 메시지입니다")
            .boardCommentLikeCounts(0)
            .build();
    }

    private static BoardCommentResponse activeComment(BoardComment comment) {
        return BoardCommentResponse.builder()
            .id(comment.getId())
            .type(comment.getType())
            .boardId(comment.getBoardId())
            .memberId(comment.getMemberId())
            .content(comment.getContent())
            .boardCommentLikeCounts(comment.getCommentLikeCounts())
            .build();
    }

}
