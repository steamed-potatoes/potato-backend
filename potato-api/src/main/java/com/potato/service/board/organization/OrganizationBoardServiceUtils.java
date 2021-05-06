package com.potato.service.board.organization;

import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.board.organization.OrganizationBoardType;
import com.potato.domain.board.organization.repository.dto.BoardWithOrganizationDto;
import com.potato.exception.model.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OrganizationBoardServiceUtils {

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

    public static List<BoardWithOrganizationDto> findOrganizationBoardWithPagination(OrganizationBoardRepository organizationBoardRepository, long lastOrganizationBoardId, int size) {
        if (lastOrganizationBoardId == 0) {
            return organizationBoardRepository.findAllBoardsOrderByDescWithLimit(size);
        }
        return organizationBoardRepository.findAllBoardsWithOrganizationLessThanIdOrderByIdDescWithLimit(lastOrganizationBoardId, size);
    }

    public static List<BoardWithOrganizationDto> findOrganizationBoardWithPaginationByType(OrganizationBoardRepository organizationBoardRepository, OrganizationBoardType type, long lastOrganizationBoardId, int size) {
        if (lastOrganizationBoardId == 0) {
            return organizationBoardRepository.findAllBoardsWithOrganizationByBoardTypeOrderByIdDesc(type, size);
        }
        return organizationBoardRepository.findBoardsByTypeLessThanOrderByIdDescLimit(type, lastOrganizationBoardId, size);
    }

    public static void validateExistsBoard(OrganizationBoardRepository organizationBoardRepository, Long organizationBoardId) {
        OrganizationBoard organizationBoard = organizationBoardRepository.findOrganizationBoardById(organizationBoardId);
        if (organizationBoard == null) {
            throw new NotFoundException(String.format("해당하는 그룹의 게시물 (%s)이 존재하지 않습니다", organizationBoardId));
        }
    }

}
