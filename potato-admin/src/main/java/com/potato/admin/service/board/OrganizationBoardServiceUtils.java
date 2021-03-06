package com.potato.admin.service.board;

import com.potato.domain.domain.board.organization.OrganizationBoard;
import com.potato.domain.domain.board.organization.OrganizationBoardRepository;
import com.potato.common.exception.model.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationBoardServiceUtils {

    public static OrganizationBoard findOrganizationBoardBySubDomainAndId(OrganizationBoardRepository organizationBoardRepository, String subDomain, Long organizationBoardId) {
        OrganizationBoard organizationBoard = organizationBoardRepository.findOrganizationBoardByIdAndSubDomain(organizationBoardId, subDomain);
        if (organizationBoard == null) {
            throw new NotFoundException(String.format("해당하는 그룹 (%s)의 게시물 (%s)이 존재하지 않습니다", subDomain, organizationBoardId));
        }
        return organizationBoard;
    }

}
