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
    public List<BoardInfoResponse> getBoards() {
        return boardRepository.findAll().stream()
            .map(BoardInfoResponse::of)
            .collect(Collectors.toList());
    }

}
