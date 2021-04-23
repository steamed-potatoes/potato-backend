package com.potato.service.comment.dto.response;

import com.potato.domain.comment.BoardComment;
import com.potato.domain.comment.BoardCommentType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCommentResponse {

    private Long id;

    private BoardCommentType type;

    private Long boardId;

    private Long memberId;

    private String content;

    private final List<BoardCommentResponse> children = new ArrayList<>();

    public static BoardCommentResponse of(BoardComment comment) {
        BoardCommentResponse response = activeOrInActiveComment(comment);
        List<BoardCommentResponse> childResponses = comment.getChildComments().stream()
            .map(BoardCommentResponse::of)
            .collect(Collectors.toList());
        response.children.addAll(childResponses);
        return response;
    }

    private static BoardCommentResponse activeOrInActiveComment(BoardComment comment) {
        if (comment.isDeleted()) {
            return inActiveComment(comment);
        }
        return activeComment(comment);
    }

    private static BoardCommentResponse inActiveComment(BoardComment comment) {
        return new BoardCommentResponse(comment.getId(), comment.getType(), comment.getBoardId(), null, "삭제된 메시지입니다");
    }

    private static BoardCommentResponse activeComment(BoardComment comment) {
        return new BoardCommentResponse(comment.getId(), comment.getType(), comment.getBoardId(), comment.getMemberId(), comment.getContent());
    }

}
