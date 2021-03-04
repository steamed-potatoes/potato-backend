package com.potato.service.comment.dto.response;

import com.potato.domain.comment.BoardComment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCommentResponse {

    private final Long id;

    private final Long boardId;

    private final Long memberId;

    private final String content;

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
        return new BoardCommentResponse(comment.getId(), comment.getBoardId(), null, "삭제된 메시지입니다");
    }

    private static BoardCommentResponse activeComment(BoardComment comment) {
        return new BoardCommentResponse(comment.getId(), comment.getBoardId(), comment.getMemberId(), comment.getContent());
    }

}
