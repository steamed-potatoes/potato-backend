package com.potato.domain.boardV2.organization.repository;

import com.potato.domain.boardV2.organization.OrganizationBoard;

public interface OrganizationBoardRepositoryCustom {

    OrganizationBoard findOrganizationBoardById(Long id);

	OrganizationBoard findOrganizationBoardByIdAndSubDomain(Long organizationBoardId, String subDomain);

}
