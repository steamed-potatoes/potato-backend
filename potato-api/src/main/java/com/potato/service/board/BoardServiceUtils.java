package com.potato.service.board;

import com.potato.domain.board.Board;
import com.potato.domain.board.BoardRepository;
import com.potato.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class BoardServiceUtils {

    static Board findPublicBoardById(BoardRepository boardRepository, Long boardId) {
        Board board = boardRepository.findPublicBoardById(boardId);
        if (board == null) {
            throw new NotFoundException("존재하지 않는 게시물입니다.");
        }
        return board;
    }

    public static Board findPublicBoardBySubDomainAndId(BoardRepository boardRepository, String subDomain, Long boardId) {
        Board board = boardRepository.findBoardByIdAndSubDomain(boardId, subDomain);
        if (board == null) {
            throw new NotFoundException("존재하지 않는 게시물입니다.");
        }
        return board;
    }

}
