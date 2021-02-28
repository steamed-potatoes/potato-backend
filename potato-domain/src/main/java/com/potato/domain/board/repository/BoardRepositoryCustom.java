package com.potato.domain.board.repository;

import com.potato.domain.board.Board;

public interface BoardRepositoryCustom {

    Board findBoardById(Long boardId);

}
