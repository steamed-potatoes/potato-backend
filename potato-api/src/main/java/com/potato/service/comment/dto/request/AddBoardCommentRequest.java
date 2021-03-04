package com.potato.service.comment.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddBoardCommentRequest {

    private Long boardId;

    private Long parentCommentId;

    private String content;

    private AddBoardCommentRequest(Long boardId, Long parentCommentId, String content) {
        this.boardId = boardId;
        this.parentCommentId = parentCommentId;
        this.content = content;
    }

    public static AddBoardCommentRequest testInstance(Long boardId, Long parentCommentId, String content) {
        return new AddBoardCommentRequest(boardId, parentCommentId, content);
    }

    public boolean hasParentComment() {
        return this.parentCommentId == null;
    }

}
