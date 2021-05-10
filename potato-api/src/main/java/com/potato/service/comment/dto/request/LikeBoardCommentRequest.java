package com.potato.service.comment.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LikeBoardCommentRequest {

    @NotNull
    private Long boardCommentId;

}
