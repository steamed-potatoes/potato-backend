package com.potato.domain.domain.comment;

import com.potato.domain.domain.board.BoardType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Test Helper Class
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCommentCreator {

    public static BoardComment createRootComment(BoardType type, Long boardId, Long memberId, String content) {
        return BoardComment.newRootComment(type, boardId, memberId, content);
    }

}
