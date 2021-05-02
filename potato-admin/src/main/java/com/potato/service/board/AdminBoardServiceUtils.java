package com.potato.service.board;

import com.potato.domain.board.admin.AdminBoard;
import com.potato.domain.board.admin.AdminBoardRepository;
import com.potato.exception.model.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminBoardServiceUtils {

    public static AdminBoard findAdminBoardById(AdminBoardRepository adminBoardRepository, Long adminBoardId) {
        AdminBoard adminBoard = adminBoardRepository.findAdminBoardById(adminBoardId);
        if (adminBoard == null) {
            throw new NotFoundException(String.format("해당 관리자게시글 (%s)가 없습니다.", adminBoardId));
        }
        return adminBoard;
    }

}
