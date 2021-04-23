package com.potato.domain.comment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Test Helper Class
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCommentCreator {

    public static BoardComment createRootComment(BoardCommentType type, Long boardId, Long memberId, String content) {
        return BoardComment.newRootComment(type, boardId, memberId, content);
    }

}
