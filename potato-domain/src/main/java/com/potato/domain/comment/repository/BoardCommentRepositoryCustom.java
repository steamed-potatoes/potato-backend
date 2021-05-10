package com.potato.domain.comment.repository;

import com.potato.domain.comment.BoardComment;
import com.potato.domain.board.BoardType;

import java.util.List;

public interface BoardCommentRepositoryCustom {

    BoardComment findBoardCommentByIdAndMemberId(Long boardCommentId, Long memberId);

    List<BoardComment> findRootCommentByTypeAndBoardId(BoardType type, Long boardId);

}
