package com.potato.admin.service.board;

import com.potato.admin.service.board.dto.request.CreateAdminBoardRequest;
import com.potato.admin.service.board.dto.request.UpdateAdminBoardRequest;
import com.potato.admin.service.board.dto.response.AdminBoardInfoResponse;
import com.potato.domain.domain.board.admin.AdminBoard;
import com.potato.domain.domain.board.admin.AdminBoardRepository;
import com.potato.domain.domain.board.organization.DeleteOrganizationBoardRepository;
import com.potato.domain.domain.board.organization.OrganizationBoard;
import com.potato.domain.domain.board.organization.OrganizationBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminBoardService {

    private final AdminBoardRepository adminBoardRepository;

    private final OrganizationBoardRepository organizationBoardRepository;

    private final DeleteOrganizationBoardRepository deleteOrganizationBoardRepository;

    @Transactional
    public AdminBoardInfoResponse createAdminBoard(CreateAdminBoardRequest request, Long adminMemberId) {
        return AdminBoardInfoResponse.of(adminBoardRepository.save(request.toEntity(adminMemberId)));
    }

    @Transactional
    public AdminBoardInfoResponse updateAdminBoard(UpdateAdminBoardRequest request) {
        AdminBoard adminBoard = AdminBoardServiceUtils.findAdminBoardById(adminBoardRepository, request.getAdminBoardId());
        adminBoard.updateInfo(request.getTitle(), request.getContent(), request.getImageUrl(), request.getStartDateTime(), request.getEndDateTime());
        return AdminBoardInfoResponse.of(adminBoard);
    }

    @Transactional
    public void deleteOrganizationBoard(String subDomain, Long organizationBoardId, Long adminMemberId) {
        OrganizationBoard organizationBoard = OrganizationBoardServiceUtils.findOrganizationBoardBySubDomainAndId(organizationBoardRepository, subDomain, organizationBoardId);
        deleteOrganizationBoardRepository.save(organizationBoard.deleteByAdmin(adminMemberId));
        organizationBoardRepository.delete(organizationBoard);
    }

}
