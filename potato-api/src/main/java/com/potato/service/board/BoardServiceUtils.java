package com.potato.service.board;

import com.potato.domain.board.Board;
import com.potato.domain.board.BoardRepository;
import com.potato.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardServiceUtils {

    public static Board findBoardById(BoardRepository boardRepository, Long boardId) {
        Board board = boardRepository.findBoardById(boardId);
        if (board == null) {
            throw new NotFoundException("존재하지 않는 게시물입니다.");
        }
        return board;
    }

}
