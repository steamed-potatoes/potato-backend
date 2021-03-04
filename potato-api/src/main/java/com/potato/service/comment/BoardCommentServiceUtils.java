package com.potato.service.comment;

import com.potato.domain.comment.BoardComment;
import com.potato.domain.comment.BoardCommentRepository;
import com.potato.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class BoardCommentServiceUtils {

    static BoardComment findBoardCommentById(BoardCommentRepository boardCommentRepository, Long boardCommentId) {
        BoardComment boardComment = boardCommentRepository.findBoardCommentById(boardCommentId);
        if (boardComment == null) {
            throw new NotFoundException(String.format("해당하는 id (%s)를 가진 댓글이 없습니다", boardCommentId));
        }
        return boardComment;
    }

}
