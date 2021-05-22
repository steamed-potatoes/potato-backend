package com.potato.api.service.comment.dto.request;

import com.potato.domain.domain.board.BoardType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveBoardCommentsRequest {

    @NotNull
    private BoardType type;

    @NotNull
    private Long boardId;

    public static RetrieveBoardCommentsRequest testInstance(BoardType type, Long boardId) {
        return new RetrieveBoardCommentsRequest(type, boardId);
    }

}
