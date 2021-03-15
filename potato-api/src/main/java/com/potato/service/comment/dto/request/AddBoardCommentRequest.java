package com.potato.service.comment.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor
public class AddBoardCommentRequest {

    @NotNull
    private Long organizationBoardId;

    @NotNull
    private Long parentCommentId;

    @NotBlank
    private String content;

    private AddBoardCommentRequest(Long organizationBoardId, Long parentCommentId, String content) {
        this.organizationBoardId = organizationBoardId;
        this.parentCommentId = parentCommentId;
        this.content = content;
    }

    public static AddBoardCommentRequest testInstance(Long organizationBoardId, Long parentCommentId, String content) {
        return new AddBoardCommentRequest(organizationBoardId, parentCommentId, content);
    }

    public boolean hasParentComment() {
        return this.parentCommentId == null;
    }

}
