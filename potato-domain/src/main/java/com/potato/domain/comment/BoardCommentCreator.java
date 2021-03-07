package com.potato.domain.comment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCommentCreator {

    public static BoardComment createRootComment(Long boardId, Long memberId, String content) {
        return BoardComment.newRootComment(boardId, memberId, content);
    }

}
