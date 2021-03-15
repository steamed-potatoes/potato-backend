package com.potato.service.boardv2;

import com.potato.domain.boardV2.organization.OrganizationBoard;
import com.potato.domain.boardV2.organization.OrganizationBoardRepository;
import com.potato.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class OrganizationBoardServiceUtils {

    static OrganizationBoard findOrganizationBoardById(OrganizationBoardRepository repository, Long organizationBoardId) {
        OrganizationBoard organizationBoard = repository.findOrganizationBoardById(organizationBoardId);
        if (organizationBoard == null) {
            throw new NotFoundException(String.format("해당하는 그룹의 게시물 (%s)이 존재하지 않습니다", organizationBoardId));
        }
        return organizationBoard;
    }

    static OrganizationBoard findOrganizationBoardByIdAndSubDomain(OrganizationBoardRepository repository, String subDomain, Long organizationBoardId) {
        OrganizationBoard organizationBoard = repository.findOrganizationBoardByIdAndSubDomain(organizationBoardId, subDomain);
        if (organizationBoard == null) {
            throw new NotFoundException(String.format("해당하는 그룹 (%s)의 게시물 (%s)이 존재하지 않습니다", subDomain, organizationBoardId));
        }
        return organizationBoard;
    }

}
