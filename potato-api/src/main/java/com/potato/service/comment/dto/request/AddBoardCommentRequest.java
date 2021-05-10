package com.potato.service.comment.dto.request;

import com.potato.domain.board.BoardType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddBoardCommentRequest {

    @NotNull
    private BoardType type;

    @NotNull
    private Long boardId;

    private Long parentCommentId;

    @NotBlank
    private String content;

    private AddBoardCommentRequest(BoardType type, Long boardId, Long parentCommentId, String content) {
        this.type = type;
        this.boardId = boardId;
        this.parentCommentId = parentCommentId;
        this.content = content;
    }

    public static AddBoardCommentRequest testInstance(BoardType type, Long boardId, Long parentCommentId, String content) {
        return new AddBoardCommentRequest(type, boardId, parentCommentId, content);
    }

    public boolean hasParentComment() {
        return this.parentCommentId == null;
    }

}
