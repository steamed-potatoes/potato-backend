package com.potato.domain.board.repository;

import com.potato.domain.board.Board;

import java.util.List;

public interface BoardRepositoryCustom {

    Board findPublicBoardById(Long boardId);

    List<Board> findPublicBoardsOrderByIdDesc(int size);

    List<Board> findPublicBoardsLessThanOrderByIdDescLimit(Long lastBoardId, int size);

    Board findBoardByIdAndSubDomain(Long boardId, String subDomain);

    Board findFetchBoardById(Long boardId);

}
