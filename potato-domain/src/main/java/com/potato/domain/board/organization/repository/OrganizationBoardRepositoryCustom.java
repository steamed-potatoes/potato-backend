package com.potato.domain.board.organization.repository;

import com.potato.domain.board.organization.OrganizationBoard;

import java.time.LocalDate;
import java.util.List;

public interface OrganizationBoardRepositoryCustom {

    OrganizationBoard findOrganizationBoardById(Long id);

    OrganizationBoard findOrganizationBoardByIdAndSubDomain(Long organizationBoardId, String subDomain);

    List<OrganizationBoard> findBetweenDate(LocalDate startDate, LocalDate endDate);

}
