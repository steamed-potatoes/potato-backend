package com.potato.domain.comment.repository;

import com.potato.domain.comment.BoardComment;

import java.util.List;

public interface BoardCommentRepositoryCustom {

    BoardComment findBoardCommentById(Long boardCommentId);

    List<BoardComment> findRootCommentByOrganizationBoardId(Long boardId);

    BoardComment findBoardCommentByIdAndMemberId(Long boardCommentId, Long memberId);

}
