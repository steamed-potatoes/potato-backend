package com.potato.domain.domain.image;

import com.potato.domain.domain.board.BoardType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardImageCreator {

    public static BoardImage create(Long boardId, BoardType type, String imageUrl) {
        return BoardImage.testInstance(boardId, type, imageUrl);
    }

}
