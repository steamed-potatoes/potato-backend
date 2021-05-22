package com.potato.api.service.comment.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LikeBoardCommentRequest {

    @NotNull
    private Long boardCommentId;

    public static LikeBoardCommentRequest testInstance(Long boardCommentId) {
        return new LikeBoardCommentRequest(boardCommentId);
    }

}
