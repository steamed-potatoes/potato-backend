package com.potato.service.boardv2;

import com.potato.domain.boardV2.organization.OrganizationBoard;
import com.potato.domain.boardV2.organization.OrganizationBoardRepository;
import com.potato.service.boardv2.dto.request.CreateOrganizationBoardRequest;
import com.potato.service.boardv2.dto.response.OrganizationBoardInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrganizationBoardService {

    private final OrganizationBoardRepository organizationBoardRepository;

    @Transactional
    public OrganizationBoardInfoResponse createBoard(String subDomain, CreateOrganizationBoardRequest request, Long memberId) {
        return OrganizationBoardInfoResponse.of(organizationBoardRepository.save(request.toEntity(subDomain, memberId)));
    }

    @Transactional(readOnly = true)
    public OrganizationBoardInfoResponse retrieveBoard(Long organizationBoardId) {
        OrganizationBoard organizationBoard = OrganizationBoardServiceUtils.findOrganizationBoardById(organizationBoardRepository, organizationBoardId);
        return OrganizationBoardInfoResponse.of(organizationBoard);
    }

}
