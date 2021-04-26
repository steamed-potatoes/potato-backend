package com.potato.domain.board.organization.repository;

import com.potato.domain.board.organization.OrganizationBoard;

import java.time.LocalDateTime;
import java.util.List;

import java.time.LocalDate;

public interface OrganizationBoardRepositoryCustom {

    OrganizationBoard findOrganizationBoardById(Long id);

    OrganizationBoard findOrganizationBoardByIdAndSubDomain(Long organizationBoardId, String subDomain);

    List<OrganizationBoard> findBoardsOrderByDesc(int size);

    List<OrganizationBoard> findBoardsLessThanOrderByIdDescLimit(long lastOrganizationBoardId, int size);

    List<OrganizationBoard> findBetweenDateIncludeOverlapping(LocalDate startDate, LocalDate endDate);

    List<OrganizationBoard> findBetweenDateLimit(LocalDateTime startDateTime, LocalDateTime endDateTime, int size);

    List<OrganizationBoard> findPopularBoard();

}
