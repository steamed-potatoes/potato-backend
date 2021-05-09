package com.potato.service.comment.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LikeBoardCommentRequest {

    @NotNull
    private Long boardCommentId;

}
