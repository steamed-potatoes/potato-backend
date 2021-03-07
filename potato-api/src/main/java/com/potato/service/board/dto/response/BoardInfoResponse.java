package com.potato.service.board.dto.response;

import com.potato.domain.board.Board;
import com.potato.domain.board.Category;
import com.potato.domain.board.Visible;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import static lombok.AccessLevel.*;

@ToString
@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class BoardInfoResponse {

    private final Long id;

    private final Visible visible;

    private final String title;

    private final String content;

    private final String imageUrl;

    private final Category category;

    private final int likesCount;

    public static BoardInfoResponse of(Board board) {
        return new BoardInfoResponse(board.getId(), board.getVisible(), board.getTitle(), board.getContent(), board.getImageUrl(), board.getCategory(), board.getLikesCount());
    }

}
