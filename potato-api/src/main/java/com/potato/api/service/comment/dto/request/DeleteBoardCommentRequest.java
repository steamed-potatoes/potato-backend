package com.potato.api.service.comment.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeleteBoardCommentRequest {

    private Long boardCommentId;

    public static DeleteBoardCommentRequest testInstance(Long boardCommentId) {
        return new DeleteBoardCommentRequest(boardCommentId);
    }

}
