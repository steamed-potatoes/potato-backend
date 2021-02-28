package com.potato.service.board;

import com.potato.domain.board.BoardRepository;
import com.potato.service.board.dto.response.BoardInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public List<BoardInfoResponse> retrievePublicLatestBoardList(Long lastBoardId, int size) {
        return lastBoardId == 0 ? getLatestBoards(size) : getLatestBoardLessThanId(lastBoardId, size);
    }

    private List<BoardInfoResponse> getLatestBoards(int size) {
        return boardRepository.findPublicBoardsOrderByIdDesc(size).stream()
            .map(BoardInfoResponse::of)
            .collect(Collectors.toList());
    }

    private List<BoardInfoResponse> getLatestBoardLessThanId(Long lastBoardId, int size) {
        return boardRepository.findPublicBoardsLessThanOrderByIdDescLimit(lastBoardId, size).stream()
            .map(BoardInfoResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BoardInfoResponse getDetailBoard(Long boardId) {
        return BoardInfoResponse.of(BoardServiceUtils.findPublicBoardById(boardRepository, boardId));
    }

}
