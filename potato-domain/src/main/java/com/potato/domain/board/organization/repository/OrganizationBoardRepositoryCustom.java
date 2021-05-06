package com.potato.domain.board.organization.repository;

import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardType;
import com.potato.domain.board.organization.repository.dto.BoardWithOrganizationDto;

import java.time.LocalDateTime;
import java.util.List;

import java.time.LocalDate;

public interface OrganizationBoardRepositoryCustom {

    OrganizationBoard findOrganizationBoardById(Long id);

    OrganizationBoard findOrganizationBoardByIdAndSubDomain(Long organizationBoardId, String subDomain);

    List<BoardWithOrganizationDto> findAllWithOrganizationByTypeLessThanOrderByIdDescLimit(OrganizationBoardType type, long lastOrganizationBoardId, int size);

    List<OrganizationBoard> findAllBetweenDate(LocalDate startDate, LocalDate endDate);

    List<OrganizationBoard> findAllByBetweenDateTimeWithLimit(LocalDateTime startDateTime, LocalDateTime endDateTime, int size);

    List<OrganizationBoard> findAllOrderByLikesWithLimit(int size);

}
