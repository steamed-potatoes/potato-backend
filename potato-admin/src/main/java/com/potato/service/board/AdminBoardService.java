package com.potato.service.board;

import com.potato.domain.board.admin.AdminBoard;
import com.potato.domain.board.admin.AdminBoardRepository;
import com.potato.domain.board.admin.DeleteAdminBoardRepository;
import com.potato.service.board.dto.request.CreateAdminBoardRequest;
import com.potato.service.board.dto.request.UpdateAdminBoardRequest;
import com.potato.service.board.dto.response.AdminBoardInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminBoardService {

    private final AdminBoardRepository adminBoardRepository;

    private final DeleteAdminBoardRepository deleteAdminBoardRepository;

    @Transactional
    public AdminBoardInfoResponse createAdminBoard(CreateAdminBoardRequest request, Long adminMemberId) {
        return AdminBoardInfoResponse.of(adminBoardRepository.save(request.toEntity(adminMemberId)));
    }

    @Transactional
    public AdminBoardInfoResponse updateAdminBoard(UpdateAdminBoardRequest request) {
        AdminBoard adminBoard = AdminBoardServiceUtils.findAdminBoardById(adminBoardRepository, request.getAdminBoardId());
        adminBoard.updateInfo(request.getTitle(), request.getContent(), request.getStartDateTime(), request.getEndDateTime());
        return AdminBoardInfoResponse.of(adminBoard);
    }

    @Transactional
    public void deleteAdminBoard(Long adminBoardId, Long adminMemberId) {
        AdminBoard adminBoard = AdminBoardServiceUtils.findAdminBoardById(adminBoardRepository, adminBoardId);
        deleteAdminBoardRepository.save(adminBoard.delete(adminMemberId));
        adminBoardRepository.delete(adminBoard);
    }

}
