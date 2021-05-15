package com.potato.service.comment.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateBoardCommentRequest {

    @NotNull
    private Long boardCommentId;

    @NotBlank
    private String content;

    public static UpdateBoardCommentRequest testInstance(Long boardCommentId, String content) {
        return new UpdateBoardCommentRequest(boardCommentId, content);
    }

}
