package com.potato.service.boardv2;

import com.potato.domain.boardV2.organization.OrganizationBoardRepository;
import com.potato.service.boardv2.dto.request.CreateOrganizationBoardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrganizationBoardService {

    private final OrganizationBoardRepository organizationBoardRepository;

    @Transactional
    public void createBoard(String subDomain, CreateOrganizationBoardRequest request, Long memberId) {
        organizationBoardRepository.save(request.toEntity(subDomain, memberId));
    }

}
