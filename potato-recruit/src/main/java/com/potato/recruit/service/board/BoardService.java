package com.potato.recruit.service.board;

import com.potato.recruit.domain.board.Board;
import com.potato.recruit.domain.board.BoardRepository;
import com.potato.recruit.dto.BoardSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Long save(BoardSaveRequestDto requestDto) {
        return boardRepository.save(requestDto.entity()).getId();
    }
}
