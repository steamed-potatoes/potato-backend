package com.potato.domain.board.organization.repository;

import com.potato.domain.board.organization.OrganizationBoard;

public interface OrganizationBoardRepositoryCustom {

    OrganizationBoard findOrganizationBoardById(Long id);

	OrganizationBoard findOrganizationBoardByIdAndSubDomain(Long organizationBoardId, String subDomain);

}
