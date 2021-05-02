package com.potato.service.board.admin;

import com.potato.domain.board.admin.AdminBoard;
import com.potato.domain.board.admin.AdminBoardRepository;
import com.potato.exception.model.NotFoundException;

public class AdminBoardServiceUtils {

    public static void validateExistBoard(AdminBoardRepository adminBoardRepository, Long boardId) {
        AdminBoard adminBoard = adminBoardRepository.findAdminBoardById(boardId);
        if (adminBoard == null) {
            throw new NotFoundException(String.format("해당하는 관리자 게시물 (%s)이 존재하지 않습니다", boardId));
        }
    }

}
