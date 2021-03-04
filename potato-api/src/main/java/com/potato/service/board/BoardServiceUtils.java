package com.potato.service.board;

import com.potato.domain.board.Board;
import com.potato.domain.board.BoardRepository;
import com.potato.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardServiceUtils {

    static Board findPublicBoardById(BoardRepository boardRepository, Long boardId) {
        Board board = boardRepository.findPublicBoardById(boardId);
        if (board == null) {
            throw new NotFoundException(String.format("해당하는 게시물 (%s) 은 존재하지 않습니다", boardId));
        }
        return board;
    }

    static Board findBoardBySubDomainAndId(BoardRepository boardRepository, String subDomain, Long boardId) {
        Board board = boardRepository.findBoardByIdAndSubDomain(boardId, subDomain);
        if (board == null) {
            throw new NotFoundException(String.format("해당하는 게시물 (%s) 은 존재하지 않습니다", boardId));
        }
        return board;
    }

    static Board findFetchBoardById(BoardRepository boardRepository, Long boardId) {
        Board board = boardRepository.findFetchBoardById(boardId);
        if (board == null) {
            throw new NotFoundException(String.format("해당하는 게시물 (%s) 은 존재하지 않습니다", boardId));
        }
        return board;
    }

}
