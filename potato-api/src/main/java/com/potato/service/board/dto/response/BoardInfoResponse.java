package com.potato.service.board.dto.response;

import com.potato.domain.board.Board;
import com.potato.domain.board.Category;
import com.potato.domain.board.Visible;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.*;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class BoardInfoResponse {

    private final Visible visible;

    private final String title;

    private final String content;

    private final String imageUrl;

    private final Category category;

    public static BoardInfoResponse of(Board board) {
        return new BoardInfoResponse(board.getVisible(), board.getTitle(), board.getContent(), board.getImageUrl(), board.getCategory());
    }

}
