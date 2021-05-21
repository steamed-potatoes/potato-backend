package com.potato.api.service.comment.dto.request;

import com.potato.domain.domain.board.BoardType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddBoardCommentRequest {

    @NotNull
    private BoardType type;

    @NotNull
    private Long boardId;

    private Long parentCommentId;

    @NotBlank
    private String content;

    public boolean hasParentComment() {
        return this.parentCommentId == null;
    }

    public static AddBoardCommentRequest testInstance(BoardType type, Long boardId, Long parentCommentId, String content) {
        return new AddBoardCommentRequest(type, boardId, parentCommentId, content);
    }

}
