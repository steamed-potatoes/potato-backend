package com.potato.domain.domain.board.organization.repository;

import com.potato.domain.domain.board.organization.OrganizationBoard;
import com.potato.domain.domain.board.organization.OrganizationBoardCategory;
import com.potato.domain.domain.board.organization.repository.dto.BoardWithOrganizationDto;

import java.time.LocalDateTime;
import java.util.List;

import java.time.LocalDate;

public interface OrganizationBoardRepositoryCustom {

    OrganizationBoard findOrganizationBoardById(Long id);

    OrganizationBoard findOrganizationBoardByIdAndSubDomain(Long organizationBoardId, String subDomain);

    List<BoardWithOrganizationDto> findAllWithOrganizationByTypeLessThanOrderByIdDescLimit(String subDomain, OrganizationBoardCategory type, long lastOrganizationBoardId, int size);

    List<OrganizationBoard> findAllBetweenDate(LocalDate startDate, LocalDate endDate);

    List<OrganizationBoard> findAllByBetweenDateTimeWithLimit(LocalDateTime startDateTime, LocalDateTime endDateTime, int size);

    List<OrganizationBoard> findAllOrderByLikesWithLimit(int size);

}
