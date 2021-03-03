package com.potato.service.board;

import com.potato.domain.board.Board;
import com.potato.domain.board.DeletedBoard;
import com.potato.domain.board.DeletedBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeletedBoardService {

    private final DeletedBoardRepository deletedBoardRepository;

    @Transactional
    public void backUpBoard(Board board) {
        deletedBoardRepository.save(DeletedBoard.newBackUpInstance(board));
    }

}
