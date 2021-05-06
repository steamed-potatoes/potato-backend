package com.potato.domain.comment.repository;

import com.potato.domain.comment.BoardComment;
import com.potato.domain.comment.BoardCommentType;

import java.util.List;

public interface BoardCommentRepositoryCustom {

    BoardComment findBoardCommentByIdAndMemberId(Long boardCommentId, Long memberId);

    List<BoardComment> findRootCommentByTypeAndBoardId(BoardCommentType type, Long boardId);

}
