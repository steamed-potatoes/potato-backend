package com.potato.service.board;

import com.potato.domain.board.admin.AdminBoard;
import com.potato.domain.board.admin.AdminBoardRepository;
import com.potato.service.board.dto.request.CreateAdminBoardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminBoardService {

    private final AdminBoardRepository adminBoardRepository;

    @Transactional
    public void createAdminBoard(CreateAdminBoardRequest request, Long adminMemberId) {
        AdminBoard adminBoard = request.toEntity(adminMemberId);
        adminBoardRepository.save(adminBoard);
    }

}
