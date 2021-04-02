package com.potato.service.board;

import com.potato.domain.board.admin.AdminBoard;
import com.potato.domain.board.admin.AdminBoardRepository;
import com.potato.exception.NotFoundException;

public class AdminBoardServiceUtils {

    public static AdminBoard findAdminBoardById(AdminBoardRepository adminBoardRepository, Long adminBoardId) {
        AdminBoard adminBoard = adminBoardRepository.findAdminBoardById(adminBoardId);
        if (adminBoard == null) {
            throw new NotFoundException(String.format("해당 관리자게시글 (%s)가 없습니다.", adminBoardId), "해당 관리자 게시글이 없습니다.");
        }
        return adminBoard;
    }

}
