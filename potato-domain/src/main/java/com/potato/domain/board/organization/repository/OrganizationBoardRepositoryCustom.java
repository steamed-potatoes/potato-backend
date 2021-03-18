package com.potato.domain.board.organization.repository;

import com.potato.domain.board.organization.OrganizationBoard;

import java.util.List;

import java.time.LocalDate;
import java.util.List;

public interface OrganizationBoardRepositoryCustom {

    OrganizationBoard findOrganizationBoardById(Long id);

	OrganizationBoard findOrganizationBoardByIdAndSubDomain(Long organizationBoardId, String subDomain);

    List<OrganizationBoard> findBoardsOrderByDesc(int size);

    List<OrganizationBoard> findBoardsLessThanOrderByIdDescLimit(long lastOrganizationBoardId, int size);

    List<OrganizationBoard> findBetweenDate(LocalDate startDate, LocalDate endDate);

}
