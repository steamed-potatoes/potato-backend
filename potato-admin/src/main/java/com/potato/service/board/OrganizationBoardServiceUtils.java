package com.potato.service.board;

import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardRepository;
import com.potato.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationBoardServiceUtils {

    public static OrganizationBoard findOrganizationBoardBySubDomainAndId(OrganizationBoardRepository organizationBoardRepository, String subDomain, Long organizationBoardId) {
        OrganizationBoard organizationBoard = organizationBoardRepository.findOrganizationBoardByIdAndSubDomain(organizationBoardId, subDomain);
        if (organizationBoard == null) {
            throw new NotFoundException(String.format("해당하는 그룹 (%s)의 게시물 (%s)이 존재하지 않습니다", subDomain, organizationBoardId), "해당 그룹에 존재하지 않는 게시물입니다.");
        }
        return organizationBoard;
    }

}
