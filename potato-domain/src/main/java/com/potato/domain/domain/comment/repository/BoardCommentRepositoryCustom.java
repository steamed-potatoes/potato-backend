package com.potato.domain.domain.comment.repository;

import com.potato.domain.domain.comment.BoardComment;
import com.potato.domain.domain.board.BoardType;

import java.util.List;

public interface BoardCommentRepositoryCustom {

    BoardComment findActiveBoardCommentByIdAndMemberId(Long boardCommentId, Long memberId);

    List<BoardComment> findRootCommentByTypeAndBoardId(BoardType type, Long boardId);

}
